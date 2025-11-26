/*
 *
 *  *
 *  *  * Copyright (c) 2025. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *  *
 *
 *
 *
 *  * Created by $user $date
 *
 *
 */

package one.empty3.apps.facedetect;

import com.formdev.flatlaf.json.Json;
import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.google.api.services.vision.v1.model.*;


import com.google.gson.Gson;
// ... existing code ...
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
// ... existing code ...
// Supprimer ces imports, ils ne seront plus utilisés :
// import com.google.api.services.vision.v1.model.FaceAnnotation;
// import com.google.gson.reflect.TypeToken;
// import com.google.api.services.vision.v1.model.*;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class LoadTxtFromImage {

    // Supprimer ce champ qui ne sera plus utilisé :
    // public List<FaceAnnotation> lastFaces;

    public String loadText(BufferedImage image) {
        if (image == null) {
            throw new IllegalStateException("Aucune image chargée dans LoadTxtFromImage (image == null)");
        }

        try {
            // 1. Convertir l'image en bytes (JPEG)
            ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
            if (!ImageIO.write(image, "jpg", imageOut)) {
                throw new IOException("Impossible d'encoder l'image en JPEG");
            }
            byte[] imageBytes = imageOut.toByteArray();

            // 2. Encoder en Base64
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // 3. Construire le JSON attendu par la Cloud Function
            String jsonPayload = "{\"image\":\"" + base64Image + "\"}";

            // 4. Préparer la connexion HTTP POST
            URL url = new URL("https://us-central1-meshmasks-1dd55.cloudfunctions.net/gen-text");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setConnectTimeout(30_000);
            connection.setReadTimeout(60_000);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // 5. Envoyer le corps JSON
            byte[] payloadBytes = jsonPayload.getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Length", String.valueOf(payloadBytes.length));
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payloadBytes);
                os.flush();
            }

            // 6. Lire la réponse
            int status = connection.getResponseCode();
            InputStream responseStream = (status >= 200 && status < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();

            if (responseStream == null) {
                throw new IOException("Flux de réponse HTTP nul (code=" + status + ")");
            }

            StringBuilder responseBuilder = new StringBuilder();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = responseStream.read(buffer)) != -1) {
                responseBuilder.append(new String(buffer, 0, read, StandardCharsets.UTF_8));
            }

            String responseBody = responseBuilder.toString();

            System.out.println("Réponse de la Cloud Function : " + responseBody);

            if (status < 200 || status >= 300) {
                throw new IOException("Erreur HTTP " + status + " : " + responseBody);
            }
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(responseBody, JsonObject.class);

            // Récupérer le tableau "faces" sans utiliser FaceAnnotation
            JsonArray facesArray = null;
            if (root != null && root.has("faces") && !root.get("faces").isJsonNull()) {
                facesArray = root.getAsJsonArray("faces");
            }
            assert facesArray != null;
            String s = computeText(image, facesArray);
            assert s != null && !s.isEmpty();
            System.out.println(s);
            return s;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'appel à la Cloud Function gen-text", e);
        }
    }

    /***
     * Construit le texte des landmarks à partir du JSON.
     * Retourne une chaîne vide si facesArray est null ou vide.
     */
    private String computeText(BufferedImage image, JsonArray facesArray) {
        StringBuilder sb = new StringBuilder();

        int width;// = image.getWidth();
        int height;// = image.getHeight();

        width=1;
        height=1;

        if (facesArray == null || facesArray.isEmpty()) {
            // Aucun visage détecté : c'est un cas normal, on retourne simplement ""
            return sb.toString();
        }

        if (facesArray.size() > 0) {
            // Process the first face object, can be extended if multiple faces are expected
            JsonObject faceObject = facesArray.get(0).getAsJsonObject();
            JsonArray landmarksArray = faceObject.getAsJsonArray("landmarks");

            for (int i = 0; i < landmarksArray.size(); i++) {
                JsonObject lmObj = landmarksArray.get(i).getAsJsonObject();

                // position.{x,y}
                double xNorm = 0.0;
                double yNorm = 0.0;


                String type = lmObj.get("type").getAsString();
                if (lmObj.has("position") && lmObj.get("position").isJsonObject()) {
                    JsonObject pos = lmObj.getAsJsonObject("position");
                    double x = pos.has("x") && !pos.get("x").isJsonNull() ? pos.get("x").getAsDouble() : 0.0;
                    double y = pos.has("y") && !pos.get("y").isJsonNull() ? pos.get("y").getAsDouble() : 0.0;

                    xNorm = x / width;
                    yNorm = y / height;
                }

                sb.append(type).append("\n");
                sb.append(xNorm).append("\n");
                sb.append(yNorm).append("\n");
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}