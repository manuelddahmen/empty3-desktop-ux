package one.empty3.apps.facedetect.gcp;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jogamp.nativewindow.awt.DirectDataBufferInt;
import one.empty3.libs.Image;
import org.apache.http.util.ByteArrayBuffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;

public class ImageProcessor implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        try {

            // Set CORS headers to allow requests from Flutter
            response.appendHeader("Access-Control-Allow-Origin", "*"); // Replace with your Flutter app's origin in production
            response.appendHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
            response.appendHeader("Access-Control-Allow-Headers", "Content-Type");
/*
        if ("OPTIONS".equals(request.getMethod())) {
            // Respond to preflight requests
            response.setStatusCode(204);
            return;
        }

        if (!"POST".equals(request.getMethod())) {
            response.setStatusCode(405);
            return;
        }
*/
            // Read the request body
            BufferedReader reader = request.getReader();
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String requestBody = stringBuilder.toString();

            // Parse the JSON request body
            Gson gson = new Gson();
            Map<String, String> data = new HashMap<>();
            JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);

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
            Map<String, Object> result = processImage(data);

            //Return Result
            response.setContentType("application/json");
            response.setStatusCode(200);
            BufferedWriter writer = response.getWriter();
            writer.write(gson.toJson(result));
            writer.close();
        }catch (RuntimeException ex) {
            response.setContentType("application/json");
            response.setStatusCode(500);
        }
    }

    private Map<String, Object> processImage(Map<String, String> data) {
        //Simulate processing
        String token = data.get("token");
        System.out.println("Token : " + token);
        System.out.println("Image1 received");
        System.out.println("Processing...");
        // Placeholder for your actual image processing logic
        Map<String, Object> response = new HashMap<>();
        String finalImageBase64;

        int completionCode = 0; // Start at 0%

        Image result = new Image(100, 100);
        try {
            ProcessData processData = new ProcessData(data);
            Thread thread = new Thread(processData);
            thread.start();
            while (processData.isRunning()) {

            }
            result = processData.getImage();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(result, "jpg", byteArrayOutputStream);
            response.put("completion", completionCode);
            response.put("image", Base64.getEncoder().encode(byteArrayOutputStream.toByteArray()));
            return response;
        } catch (Exception e) {
            response.put("completion", -1);
            return response;
        }
    }
}