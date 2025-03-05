package one.empty3.apps.facedetect.gcp;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import one.empty3.libs.Image;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import one.empty3.libs.Image;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageProcessor implements HttpFunction {

    private final Gson gson = new Gson();

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
            if (jsonObject.has("image1")) {
                data.put("image1", jsonObject.get("image1").getAsString());
            }
            if (jsonObject.has("model")) {
                data.put("model", jsonObject.get("model").getAsString());
            }
            if (jsonObject.has("image3")) {
                data.put("image3", jsonObject.get("image3").getAsString());
            }
            if (jsonObject.has("textFile1")) {
                data.put("textFile1", jsonObject.get("textFile1").getAsString());
            }
            if (jsonObject.has("textFile2")) {
                data.put("textFile2", jsonObject.get("textFile2").getAsString());
            }
            if (jsonObject.has("textFile3")) {
                data.put("textFile3", jsonObject.get("textFile3").getAsString());
            }
            if (jsonObject.has("hd_texture")) {
                data.put("hd_texture", jsonObject.get("hd_texture").getAsString());
            }
            if (jsonObject.has("selected_algorithm")) {
                data.put("selected_algorithm", jsonObject.get("selected_algorithm").getAsString());
            }
            if (jsonObject.has("selected_texture_type")) {
                data.put("selected_texture_type", jsonObject.get("selected_texture_type").getAsString());
            }
            if (jsonObject.has("token")) {
                data.put("token", jsonObject.get("token").getAsString());
            }

            //Process data
            Map<String, String> result = processImage(data);

            //Return Result
            response.setStatusCode(200);
            gson.toJson(result, response.getWriter());
        } catch (RuntimeException ex) {
            response.setStatusCode(500);
            StringBuilder error = new StringBuilder(" Error \n CAUSE:" + ex.getCause().toString() + "\n" + ex.getMessage());
            for (int i = 0; i < ex.getStackTrace().length; i++) {
                error.append(ex.getStackTrace()[i].toString());
            }
            Logger.getAnonymousLogger().log(Level.SEVERE, ex.getMessage()+"\n");
            gson.toJson(Map.of("error", "An unexpected error occurred. "+ex.getMessage()+error), response.getWriter());

        }
    }

    private Map<String, String> processImage(Map<String, String> data) {
        Map<String, String> response = new HashMap<>();
        try {
            ProcessData processData = new ProcessData(data);
            Thread thread = new Thread(processData);
            thread.start();
            while (processData.isRunning()) {

            }
            Image result = processData.getImage();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            ImageIO.write(result, "jpg", byteArrayOutputStream);
            response.put("completion", "0");
            response.put("image", Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public static void main(String[] args) {
        ImageProcessor imageProcessor = new ImageProcessor();

    }

    public JsonObject jsonObject;

}