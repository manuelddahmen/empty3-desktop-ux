package one.empty3.apps.facedetect3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FaceDetectView {
    private String name;
    private PanelType panelType = PanelType.IMAGE;
    private BufferedImage image;
    private File imageFile;
    private OBJModel objModel;
    private File modelFile;
    private final List<LandmarkPoint> landmarkPoints = new ArrayList<>();

    public FaceDetectView(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PanelType getPanelType() {
        return panelType;
    }

    public void setPanelType(PanelType panelType) {
        this.panelType = panelType;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public OBJModel getObjModel() {
        return objModel;
    }

    public void setObjModel(OBJModel objModel) {
        this.objModel = objModel;
    }

    public File getModelFile() {
        return modelFile;
    }

    public void setModelFile(File modelFile) {
        this.modelFile = modelFile;
    }

    public List<LandmarkPoint> getLandmarkPoints() {
        return landmarkPoints;
    }

    @Override
    public String toString() {
        return name;
    }
}
