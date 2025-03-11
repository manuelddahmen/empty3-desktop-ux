package one.empty3.apps.facedetect.gcp;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.*;
import one.empty3.libs.Image;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ImageProcessor implements HttpFunction {

    private final Gson gson = new Gson();

    String stringify(JsonElement array) {
        StringBuilder str = new StringBuilder();
        AtomicInteger i = new AtomicInteger();
        if (array instanceof JsonArray array1) {
            array1.forEach(element -> {
                if (element != null && !(element instanceof JsonNull) && !element.isJsonNull()) {
                    byte b = element.getAsByte();
                    String ch = element.toString();
                    if (ch.getBytes(StandardCharsets.UTF_8)[0] != 22 && ch.getBytes(StandardCharsets.UTF_8)[0] != 0x5d) {
                        str.append(b);
                        i.getAndIncrement();
                    }
                }
            });
        }
        return str.toString();
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {

        try {
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
            // Read the request body as a string
            BufferedReader reader = request.getReader();
            String body = reader.lines().collect(Collectors.joining());
            System.out.println("Request Body: " + body);

            // Parse the JSON
            JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

            // Extract each byte array
            Map<String, byte[]> byteArrays = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                if(entry.getValue() instanceof JsonNull jsonNull) {

                } else if(entry.getValue() instanceof JsonArray jsonArray) {
                     jsonArray = entry.getValue().getAsJsonArray();
                     byte[] byteArray = jsonArrayToByteArray(jsonArray);
                     if(byteArray!=null && byteArray.length>0) {
                         byteArrays.put(key, byteArray);
                         System.out.println("Receive : " + key);
                     }
                 } else if(entry instanceof JsonElement jsonElement){
                     byte[] byteArray = jsonElement.getAsString().getBytes(StandardCharsets.UTF_8);
                     if(byteArray!=null && byteArray.length>0) {
                         byteArrays.put(key, byteArray);
                         System.out.println("Receive : " + key);
                     }

                 }

            }

            // Process data
            Map<String, String> result = processImage(byteArrays);

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
                errorMap.put("error", byteArrayOutputStream.toString());
            } catch (Exception e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Error writing stacktrace");
                e.printStackTrace();
            }

            // Log exception.
            Logger.getAnonymousLogger().log(Level.SEVERE, ex.getMessage() + "\n");
            for (int i = 0; i < ex.getStackTrace().length; i++) {
                Logger.getAnonymousLogger().log(Level.SEVERE, ex.getStackTrace()[i].toString() + "\n");
            }
            gson.toJson(errorMap, response.getWriter());
        }
    }


    private Map<String, String> processImage(Map<String, byte[]> data) {
        Map<String, String> response = new HashMap<>();
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


    // Helper method to convert JsonArray to byte[]
    private byte[] jsonArrayToByteArray(JsonArray jsonArray) {
        if(jsonArray.size()>0 && jsonArray.get(0).isJsonNull() || jsonArray.size()==0) {
            byte[] byteArray = new byte[jsonArray.size()];
            for (int i = 0; i < jsonArray.size(); i++) {
                byteArray[i] = jsonArray.get(i).getAsByte();
            }
            return byteArray;
        } else
            return null;

    }
}