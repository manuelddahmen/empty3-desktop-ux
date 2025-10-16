package one.empty3.apps.facedetect.gcp;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import one.empty3.library.Point3D;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FaceDetectAppHttp implements HttpFunction {
    private static final String APPLICATION_NAME = "MeshMask";
    private static final int MAX_RESULTS = 10;
    private final Vision vision;
    private final Gson gson = new Gson();
    private String[][][] landmarks;
    private PrintWriter dataWriter;

    public FaceDetectAppHttp() {
        try {
            this.vision = getVisionService();
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public FaceDetectAppHttp(Vision visionService) {
        this.vision = visionService;
    }

    private void writeFaceData(FaceAnnotation faceAnnotation, int w, int h) {

        for (int i = 0; i < landmarks.length; i++) {
            for (int j = 0; j < landmarks[i].length; j++) {
                for (int k = 0; k < landmarks[i][j].length; k++) {
                    int finalI = i;
                    int finalJ = j;
                    int finalK = k;
                    Optional<Landmark> landmark1 = faceAnnotation.getLandmarks().stream().filter(landmark ->
                            landmark.getType() != null && landmark.getType().equals(landmarks[finalI][finalJ][finalK])).findFirst();
                    if (landmark1.isPresent()) {
                        Landmark landmark2 = landmark1.get();
                        dataWriter.println(landmark2.getType());
                        double x = landmark2.getPosition().getX();
                        double y = landmark2.getPosition().getY();
                        double normalizedX = Math.max(0.0, Math.min(1.0, x / w));
                        double normalizedY = Math.max(0.0, Math.min(1.0, y / h));
                        dataWriter.println(normalizedX);
                        dataWriter.println(normalizedY);
                        dataWriter.println();
                    }
                }
            }
        }
    }
    /**
     * Connects to the Vision API using Application Default Credentials.
     */
    public static Vision getVisionService() throws IOException, GeneralSecurityException {
        GoogleCredentials credential =
                GoogleCredentials.getApplicationDefault().createScoped(VisionScopes.all());
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        return new Vision.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                jsonFactory,
                new HttpCredentialsAdapter(credential))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    public List<FaceAnnotation> detectFaces(byte[] read, int maxResults, BufferedImage img) throws IOException {
        AnnotateImageRequest request =
                new AnnotateImageRequest()
                        .setImage(new Image().encodeContent(read))
                        .setFeatures(
                                com.google.common.collect.ImmutableList.of(
                                        new Feature().setType("FACE_DETECTION").setMaxResults(maxResults)));
        Vision.Images.Annotate annotate =
                vision
                        .images()
                        .annotate(new BatchAnnotateImagesRequest().setRequests(com.google.common.collect.ImmutableList.of(request)));
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotate.setDisableGZipContent(true);

        BatchAnnotateImagesResponse batchResponse = annotate.execute();
        assert batchResponse.getResponses().size() == 1;
        AnnotateImageResponse response = batchResponse.getResponses().get(0);
        response.getFaceAnnotations().forEach(new Consumer<FaceAnnotation>() {
            @Override
            public void accept(FaceAnnotation faceAnnotation) {
                faceAnnotation.getLandmarks().forEach(
                        new Consumer<Landmark>() {
                            @Override
                            public void accept(Landmark landmark) {
                                Float x = landmark.getPosition().getX();
                                Float y = landmark.getPosition().getY();
                                Float z = landmark.getPosition().getZ();
                                Point3D point3D = new Point3D((double) x / img.getWidth(), (double) y / img.getHeight(), (double) z);
                                landmark.getPosition().setX((float) point3D.getX());
                                landmark.getPosition().setY((float) point3D.getY());
                                landmark.getPosition().setZ((float) (double) point3D.getZ());
                            }
                        });
            }
        });

        if (response.getFaceAnnotations() == null) {
            throw new IOException(
                    response.getError() != null
                            ? response.getError().getMessage()
                            : "Unknown error getting image annotations");
        }
        return response.getFaceAnnotations();
    }

    public void initStructurePolygons() {
        landmarks = new String[][][]{
                {
                        {"LEFT_EAR_TRAGION", "CHIN_LEFT_GONION", "CHIN_GNATHION", "LEFT_CHEEK_CENTER"},
                        {"MOUTH_LEFT", "UPPER_LIP", "MOUTH_RIGHT", "MOUTH_CENTER"},
                        {"LEFT_EYE_LEFT_CORNER", "LEFT_EYE_TOP_BOUNDARY", "LEFT_EYE_RIGHT_CORNER", "LEFT_EYE_BOTTOM_BOUNDARY"},
                        {"LEFT_OF_LEFT_EYEBROW", "LEFT_EYEBROW_UPPER_MIDPOINT", "RIGHT_OF_LEFT_EYEBROW"},
                        {"MIDPOINT_BETWEEN_EYES", "NOSE_TIP", "NOSE_BOTTOM_LEFT"},
                },
                {
                        {"RIGHT_EAR_TRAGION", "CHIN_RIGHT_GONION", "CHIN_GNATHION", "RIGHT_CHEEK_CENTER"},
                        {"MOUTH_LEFT", "LOWER_LIP", "MOUTH_RIGHT", "MOUTH_CENTER"},
                        {"RIGHT_EYE_LEFT_CORNER", "RIGHT_EYE_TOP_BOUNDARY", "RIGHT_EYE_RIGHT_CORNER", "RIGHT_EYE_BOTTOM_BOUNDARY"},
                        {"LEFT_OF_RIGHT_EYEBROW", "RIGHT_EYEBROW_UPPER_MIDPOINT", "RIGHT_OF_RIGHT_EYEBROW"},
                        {"MIDPOINT_BETWEEN_EYES", "NOSE_TIP", "NOSE_BOTTOM_RIGHT"},
                }, {
                {"NOSE_TIP", "NOSE_BOTTOM_RIGHT", "NOSE_BOTTOM_CENTER", "NOSE_BOTTOM_LEFT"}

        }
        };
    }
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        //Set headers
        httpResponse.appendHeader("Access-Control-Allow-Origin", "*");
        httpResponse.appendHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        httpResponse.appendHeader("Access-Control-Allow-Headers", "Content-Type");
        httpResponse.setContentType("application/json");

        if ("OPTIONS".equals(httpRequest.getMethod())) {
            httpResponse.setStatusCode(204);
            return;
        }
        if(!"POST".equals(httpRequest.getMethod())) {
            httpResponse.setStatusCode(405);
            gson.toJson(Map.of("error", "Method Not Allowed. Use POST."), httpResponse.getWriter());
            return;
        }
        JsonObject jsonObject;
        try {
            jsonObject = gson.fromJson(httpRequest.getReader(), JsonObject.class);
            if (jsonObject == null) {
                httpResponse.setStatusCode(500);
                gson.toJson(Map.of("error", "jSonObject is null in ImageProcessor"), httpResponse.getWriter());
                return;
            }
        } catch (JsonParseException e) {
            httpResponse.setStatusCode(500);
            gson.toJson(Map.of("error", "Invalid JSON format: " + e.getMessage()), httpResponse.getWriter());
            return;
        }
        String imageString = null;
        imageString = jsonObject.get("image").getAsString();

        try {

            byte[] decode = Base64.getDecoder().decode(imageString);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(decode));
            int w  = img.getWidth();
            int h  = img.getHeight();
            if (img == null) {
                httpResponse.setStatusCode(500);
                gson.toJson(Map.of("error", "Error reading image."), httpResponse.getWriter());
                return;
            }
            FaceDetectAppHttp app = new FaceDetectAppHttp(getVisionService());
            List<FaceAnnotation> faces = app.detectFaces(decode, MAX_RESULTS, img);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            app.dataWriter = new PrintWriter(byteArrayOutputStream);
            app.initStructurePolygons();
            faces.forEach(faceAnnotation -> {
                app.writeFaceData(faceAnnotation, w, h);
            });
            // Process data
            Map<String, Object> result = new HashMap<>();

            result.put("completion", "100");
            result.put("faces", faces);
            result.put("image", Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
            app.dataWriter.close();
            httpResponse.setStatusCode(200);
            gson.toJson(result, httpResponse.getWriter());

        } catch (Exception ex) {
            httpResponse.setStatusCode(500);
            gson.toJson(Map.of("error", "Error decoding or processing image." + ex.getMessage()), httpResponse.getWriter());

        }

    }

}