package one.empty3.apps.facedetect.gcp;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.*;
import one.empty3.libs.Image;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageProcessor implements HttpFunction {

    private final Gson gson = new Gson();

    String stringify(JsonArray array) {
        StringBuilder str = new StringBuilder();
        int i=0;
        for (JsonElement jsonElement : array) {
            if (!(jsonElement instanceof JsonNull)&& !jsonElement.isJsonNull()) {
                String ch = jsonElement.toString();
                if(ch.getBytes()[0]!=22) {
                    str.append(ch);
                    i++;
                }
            }
        }
        return str.toString();
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        // Set CORS headers to allow requests from Flutter
        response.appendHeader("Access-Control-Allow-Origin", "*"); // Replace with your Flutter app's origin in production
        response.appendHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.appendHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json"); // Ensure content type is set to JSON

        if ("OPTIONS".equals(request.getMethod())) {
            // Respond to preflight requests
            response.setStatusCode(204);
            return;
        }

        if (!"POST".equals(request.getMethod())) {
            response.setStatusCode(405);
            gson.toJson(Map.of("error", "Method Not Allowed. Use POST."), response.getWriter());
            return;
        }

        // Parse the JSON request body
        Map<String, String> data = new HashMap<>();
        JsonObject jsonObject;
        try {
            jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
            if (jsonObject == null) {
                response.setStatusCode(500);
                gson.toJson(Map.of("error", "jSonObject is null in ImageProcessor"), response.getWriter());
                return;
            }
        } catch (JsonParseException e) {
            response.setStatusCode(500);
            gson.toJson(Map.of("error", "Invalid JSON format: " + e.getMessage()), response.getWriter());
            return;
        }
        try {
            if (jsonObject.has("image1") && jsonObject.get("image1") != null) {
                data.put("image1", stringify(jsonObject.getAsJsonArray("image1")));
            }
            if (jsonObject.has("model") && jsonObject.get("model") != null) {
                data.put("model", stringify(jsonObject.getAsJsonArray("model")));
            }
            if (jsonObject.has("image3") && jsonObject.get("image3") != null) {
                data.put("image3", stringify(jsonObject.getAsJsonArray("image3")));
            }
            if (jsonObject.has("textFile1") && jsonObject.get("textFile1") != null) {
                data.put("textFile1", stringify(jsonObject.getAsJsonArray("textFile1")));
            }
            if (jsonObject.has("textFile2") && jsonObject.get("textFile2") != null) {
                data.put("textFile2", stringify(jsonObject.getAsJsonArray("textFile2")));
            }
            if (jsonObject.has("textFile3") && jsonObject.get("textFile3") != null) {
                data.put("textFile3", stringify(jsonObject.getAsJsonArray("textFile3")));
            }
            if (jsonObject.has("hd_texture") && jsonObject.get("hd_texture") != null) {
                data.put("hd_texture", stringify(jsonObject.getAsJsonArray("hd_texture")));
            }
            if (jsonObject.has("selected_algorithm") && jsonObject.get("selected_algorithm") != null) {
                data.put("selected_algorithm", stringify(jsonObject.getAsJsonArray("selected_algorithm")));
            }
            if (jsonObject.has("selected_texture_type") && jsonObject.get("selected_texture_type") != null) {
                data.put("selected_texture_type", stringify(jsonObject.getAsJsonArray("selected_texture_type")));
            }
            if (jsonObject.has("token") & jsonObject.get("token") != null) {
                data.put("token", stringify(jsonObject.getAsJsonArray("token")));
            }

            // Process data
            Map<String, Object> result = processImage(data);

            // Return Result
            response.setStatusCode(200);
            gson.toJson(result, response.getWriter());
        } catch (RuntimeException ex) {
            response.setStatusCode(500);
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "An unexpected error occurred: " + ex.getMessage());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ex.printStackTrace(new PrintStream(byteArrayOutputStream));
                errorMap.put("stacktrace", byteArrayOutputStream.toString());
            } catch (Exception e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Error writing stacktrace");
                e.printStackTrace();
            }
            gson.toJson(errorMap, response.getWriter());

            // Log exception.
            Logger.getAnonymousLogger().log(Level.SEVERE, ex.getMessage() + "\n");
            for (int i = 0; i < ex.getStackTrace().length; i++) {
                Logger.getAnonymousLogger().log(Level.SEVERE, ex.getStackTrace()[i].toString() + "\n");
            }
        }
    }

    private Map<String, Object> processImage(Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            ProcessData processData = new ProcessData(data);
            Thread thread = new Thread(processData);
            thread.start();
            Image result = null;
            while (processData.isRunning() && result == null) {
                result = processData.getImage();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            if (result != null) {
                ImageIO.write(result, "jpg", byteArrayOutputStream);
                response.put("completion", "100");
                response.put("image", Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
            } else {
                response.put("completion", "0");
            }
        } catch (Exception e) {
            response.put("completion", "-1");
            response.put("error", e.getMessage());
        }
        return response;
    }

    public static void main(String[] args) {
        ImageProcessor imageProcessor = new ImageProcessor();
    }
}