package one.empty3.apps.facedetect3;

import com.google.gson.Gson;
import one.empty3.apps.facedetect3.PointMatch;
import one.empty3.library.Point3D;
import one.empty3.library.ZBufferImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MasksRenderer {

    private final FaceDetectUI ui;
    private int selectedAlgorithm = 8; // Default to "Face on face 2 (best)"
    private boolean hdTextures = false;
    private boolean isBezier = false;

    public MasksRenderer(FaceDetectUI ui) {
        this.ui = ui;
    }

    public int getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public void setSelectedAlgorithm(int selectedAlgorithm) {
        this.selectedAlgorithm = selectedAlgorithm;
    }

    public boolean isHdTextures() {
        return hdTextures;
    }

    public void setHdTextures(boolean hdTextures) {
        this.hdTextures = hdTextures;
    }

    public boolean isBezier() {
        return isBezier;
    }

    public void setBezier(boolean bezier) {
        isBezier = bezier;
    }

    public String getLeftPointsText() {
        StringBuilder sb = new StringBuilder();
        for (PointMatch pm : ui.getPointMatchTableModel().getPointMatches()) {
            Point3D p = pm.getLeftPoint();
            if (p != null) {
                sb.append(pm.getName()).append("\n");
                sb.append(p.getX()).append("\n");
                sb.append(p.getY()).append("\n\n");
            }
        }
        return sb.toString();
    }

    public String getRightPointsText() {
        StringBuilder sb = new StringBuilder();
        for (PointMatch pm : ui.getPointMatchTableModel().getPointMatches()) {
            Point3D p = pm.getRightPoint();
            if (p != null) {
                sb.append(pm.getName()).append("\n");
                sb.append(p.getX()).append("\n");
                sb.append(p.getY()).append("\n\n");
            }
        }
        return sb.toString();
    }

    public void render(boolean isRemote) {
        ImagePanel leftPanel = ui.getImagePanel1();
        ImagePanel rightPanel = ui.getImagePanel2();

        if (leftPanel == null || leftPanel.getImage() == null) {
            JOptionPane.showMessageDialog(ui, "Please load a base image in the left panel first.", "Missing Image", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (rightPanel == null || rightPanel.getModelFile() == null) {
            JOptionPane.showMessageDialog(ui, "Please load a 3D model in the right panel first.", "Missing Model", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog progressDialog = new JDialog(ui, "Rendering Mask", true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressDialog.add(BorderLayout.CENTER, progressBar);
        progressDialog.add(BorderLayout.NORTH, new JLabel(" Processing, please wait...", JLabel.CENTER));
        progressDialog.setSize(300, 100);
        progressDialog.setLocationRelativeTo(ui);

        SwingWorker<byte[], Void> worker = new SwingWorker<>() {
            @Override
            protected byte[] doInBackground() throws Exception {
                // 1. Prepare base image bytes
                byte[] image1Bytes;
                if (leftPanel.getImageFile() != null) {
                    image1Bytes = Files.readAllBytes(leftPanel.getImageFile().toPath());
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(leftPanel.getImage(), "png", baos);
                    image1Bytes = baos.toByteArray();
                }

                // 2. Prepare model bytes
                byte[] modelBytes = Files.readAllBytes(rightPanel.getModelFile().toPath());
                byte[] modelPathBytes = rightPanel.getModelFile().getAbsolutePath().getBytes(StandardCharsets.UTF_8);

                // 3. Prepare landmarks
                byte[] text1Bytes = getLeftPointsText().getBytes(StandardCharsets.UTF_8);
                byte[] text2Bytes = getRightPointsText().getBytes(StandardCharsets.UTF_8);

                if (isRemote) {
                    return renderRemote(image1Bytes, modelBytes, modelPathBytes, text1Bytes, text2Bytes);
                } else {
                    return renderLocal(image1Bytes, modelBytes, modelPathBytes, text1Bytes, text2Bytes);
                }
            }

            @Override
            protected void done() {
                progressDialog.dispose();
                try {
                    byte[] pngBytes = get();
                    if (pngBytes != null && pngBytes.length > 0) {
                        displayResult(pngBytes);
                    } else {
                        JOptionPane.showMessageDialog(ui, "Rendering failed: returned empty result.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(ui, "Error during rendering:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
        progressDialog.setVisible(true);
    }

    private byte[] renderLocal(byte[] image1Bytes, byte[] modelBytes, byte[] modelPathBytes, byte[] text1Bytes, byte[] text2Bytes) throws Exception {
        one.empty3.apps.masks.cloud.impl.MainActivity renderer = new one.empty3.apps.masks.cloud.impl.MainActivity();
        Map<String, Object> settings = new HashMap<>();
        settings.put("userId", "default");
        settings.put("useMaxRes", "false");

        return renderer.renderImages1_1(
                image1Bytes,
                modelBytes,
                modelPathBytes,
                null, // image3Bytes
                text1Bytes,
                text2Bytes,
                null, // text3Bytes
                selectedAlgorithm,
                hdTextures,
                isBezier,
                settings,
                "default"
        );
    }

    private byte[] renderRemote(byte[] image1Bytes, byte[] modelBytes, byte[] modelPathBytes, byte[] text1Bytes, byte[] text2Bytes) throws Exception {
        RenderRequest req = new RenderRequest();
        req.image1 = Base64.getEncoder().encodeToString(image1Bytes);
        req.model = Base64.getEncoder().encodeToString(modelBytes);
        req.modelPath = Base64.getEncoder().encodeToString(modelPathBytes);
        req.text1 = Base64.getEncoder().encodeToString(text1Bytes);
        req.text2 = Base64.getEncoder().encodeToString(text2Bytes);
        req.algorithm = selectedAlgorithm;
        req.hdTextures = hdTextures;
        req.isBezier = isBezier ? "B" : "";
        req.settings = new HashMap<>();
        req.settings.put("userId", "default");

        String json = new Gson().toJson(req);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://mesh-masks-render-107769431845.europe-west1.run.app/renderImages1"))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() != 200) {
            String errorMsg = response.headers().firstValue("x-error-message").orElse(null);
            if (errorMsg == null) {
                errorMsg = new String(response.body(), StandardCharsets.UTF_8);
            }
            throw new RuntimeException("HTTP " + response.statusCode() + ": " + errorMsg);
        }

        String contentType = response.headers().firstValue("Content-Type").orElse("");
        if (contentType.contains("application/json")) {
            String bodyStr = new String(response.body(), StandardCharsets.UTF_8);
            Map<?, ?> respMap = new Gson().fromJson(bodyStr, Map.class);
            String imgBase64 = (String) respMap.get("image");
            if (imgBase64 == null) {
                throw new RuntimeException("Response JSON did not contain 'image' field.");
            }
            return Base64.getDecoder().decode(imgBase64);
        }

        return response.body();
    }

    private void displayResult(byte[] pngBytes) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(pngBytes));
            if (image == null) {
                JOptionPane.showMessageDialog(ui, "Could not decode result image bytes.", "Decoding Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFrame resultFrame = new JFrame("Rendered Mask Result");
            resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            resultFrame.setSize(800, 600);
            resultFrame.setLayout(new BorderLayout());

            JPanel imagePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                }
            };
            resultFrame.add(imagePanel, BorderLayout.CENTER);

            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveBtn = new JButton("Save Image...");
            saveBtn.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File("rendered_mask.png"));
                int ret = fileChooser.showSaveDialog(resultFrame);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    try {
                        ImageIO.write(image, "png", fileChooser.getSelectedFile());
                        JOptionPane.showMessageDialog(resultFrame, "Image saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(resultFrame, "Failed to save image:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            controlPanel.add(saveBtn);
            resultFrame.add(controlPanel, BorderLayout.SOUTH);

            resultFrame.setLocationRelativeTo(ui);
            resultFrame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(ui, "Error displaying result image:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class RenderRequest {
        String method = "renderImages1";
        String userId = "default";
        String image1;
        String model;
        String modelPath;
        String image3;
        String text1;
        String text2;
        String text3;
        int algorithm;
        boolean hdTextures;
        String isBezier;
        Map<String, String> settings;
    }
}
