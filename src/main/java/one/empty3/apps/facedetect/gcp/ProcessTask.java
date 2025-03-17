package one.empty3.apps.facedetect.gcp;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProcessTask implements HttpFunction {
    private final Gson gson = new Gson();

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        // (CORS and other headers)
        // Read the task id
        BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining());
        System.out.println("Request Body: " + body);
        JsonObject jsonObject = JsonParser.parseString(body).getAsJsonObject();

        String taskId = jsonObject.get("taskId").getAsString();
        Logger.getAnonymousLogger().log(Level.INFO, "Processing Task: " + taskId);

        //Get data
        Map<String, String> data = new HashMap<>();
        if (jsonObject.has("image1") && jsonObject.get("image1") != null) {
            data.put("image1", jsonObject.get("image1").getAsString());
        }
        if (jsonObject.has("model") && jsonObject.get("model") != null) {
            data.put("model", jsonObject.get("model").getAsString());
        }
        if (jsonObject.has("image3") && jsonObject.get("image3") != null) {
            data.put("image3", jsonObject.get("image3").getAsString());
        }
        if (jsonObject.has("textFile1") && jsonObject.get("textFile1") != null) {
            data.put("textFile1", jsonObject.get("textFile1").getAsString());
        }
        if (jsonObject.has("textFile2") && jsonObject.get("textFile2") != null) {
            data.put("textFile2", jsonObject.get("textFile2").getAsString());
        }
        if (jsonObject.has("textFile3") && jsonObject.get("textFile3") != null) {
            data.put("textFile3", jsonObject.get("textFile3").getAsString());
        }
        if (jsonObject.has("hd_texture") && jsonObject.get("hd_texture") != null) {
            data.put("hd_texture", jsonObject.get("hd_texture").getAsString());
        }
        if (jsonObject.has("selected_algorithm") && jsonObject.get("selected_algorithm") != null) {
            data.put("selected_algorithm", jsonObject.get("selected_algorithm").getAsString());
        }
        if (jsonObject.has("selected_texture_type") && jsonObject.get("selected_texture_type") != null) {
            data.put("selected_texture_type", jsonObject.get("selected_texture_type").getAsString());
        }
        if (jsonObject.has("token") & jsonObject.get("token") != null) {
            data.put("token", jsonObject.get("token").getAsString());
        }
        // Do the work (e.g., processImage())
        Map<String, Object> result = processImage(data);

        // Store the result in Cloud Storage
        //...

        // Update the task status in Firestore to "complete"
        //...

        response.setStatusCode(200);
    }

    private Map<String, Object> processImage(Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();
        Map<String, byte[]> data1 = new HashMap<>();

        data.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                if(s2 instanceof String s22) {
                    data1.put(s, s22.getBytes());
                }
            }
        });
        try {
            ProcessData processData = new ProcessData(data1);
            Thread thread = new Thread(processData);
            thread.start();
            one.empty3.libs.Image result = null;
            while (processData.isRunning() && result == null) {
                result = processData.getImage();
            }

            java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();

            if (result != null) {
                javax.imageio.ImageIO.write(result, "jpg", byteArrayOutputStream);
                response.put("completion", "100");
                response.put("image", java.util.Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
            } else {
                response.put("completion", "0");
            }
        } catch (Exception e) {
            response.put("completion", "-1");
            response.put("error", e.getMessage());
        }
        return response;
    }
}