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

public class ImageProcessor implements HttpFunction {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        try {
            serviceWithoutException(request, response);
            response.setStatusCode(200);
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.INFO, "Response Ok without Exception");
        }  catch (RuntimeException ex) {
            Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, "Exception in service RuntimeException", ex);
            for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
                Logger.getLogger(ImageProcessor.class.getName()).log(Level.SEVERE, stackTraceElement.toString());
            }
            response.setStatusCode(501);;
        }
    }

    public void serviceWithoutException(HttpRequest request, HttpResponse response) throws RuntimeException, IOException {
        //        try {
        // Set CORS headers to allow requests from Flutter
        response.appendHeader("Access-Control-Allow-Origin", "*"); // Replace with your Flutter app's origin in production
        response.appendHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.appendHeader("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equals(request.getMethod())) {
            // Respond to preflight requests
            response.setStatusCode(204);
            return;
        }

        if (!"POST".equals(request.getMethod())) {
            response.setStatusCode(405);
            return;
        }

        // Read the request body
        BufferedReader reader = request.getReader();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String requestBody = stringBuilder.toString();

        Logger.getLogger("Cloud function ImageProcessor");
        Logger.getLogger(this.getClass().getCanonicalName())
                .log(Level.INFO, requestBody);

        // Parse the JSON request body
        Gson gson = new Gson();
        Map<String, String> data = new HashMap<>();
        JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);

        response.setContentType("application/json");
        response.setStatusCode(500);
        response.getWriter().write("jSonObject is null in ImageProcessor" + "\n");

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
        Map<String, Object> result1 = processImage(data);

        //Return Result
        response.setContentType("application/json");
        response.setStatusCode(200);


        JsonObject jsonObject1 = new JsonObject();
        jsonObject1.addProperty("image", String.valueOf(result1.get("image"))); // Replace with valid base64 data
        jsonObject1.addProperty("completion", (int) result1.get("completion"));

        response.getWriter().write(new Gson().toJson(jsonObject1));

        BufferedWriter writer = response.getWriter();
        writer.close();
//        }catch (RuntimeException ex) {
//            response.setContentType("application/json");
//            response.setStatusCode(500);
//
//            JsonObject jsonObject1 = new JsonObject();
//            jsonObject1.addProperty("completion", -1);
//            jsonObject1.addProperty("image", "");
//            String error = "ex.getMessage()\n";
//            for (int i = 0; i < ex.getStackTrace().length; i++) {
//                error = error+(ex.getStackTrace()[i].toString()+"\n");
//            }
//            jsonObject1.addProperty("error", error);
//
//            response.getWriter().write(new Gson().toJson(jsonObject1));
//        }

    }

    private Map<String, Object> processImage(Map<String, String> data) throws RuntimeException, IOException {
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
    }

    public static void main(String[] args) {
        ImageProcessor imageProcessor = new ImageProcessor();

    }
}