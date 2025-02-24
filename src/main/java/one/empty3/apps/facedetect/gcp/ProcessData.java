package one.empty3.apps.facedetect.gcp;

import one.empty3.library.objloader.E3Model;
import one.empty3.libs.Image;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

public class ProcessData implements Runnable {
    private final Thread runApp;
    boolean isRunning = true;
    EditPolygonsMappings editPolygonsMappings;
    /***
     * Constructor
     * @param data POST data encoded as String and Base64 for files
     */
    public ProcessData(Map<String, String> data) {
        editPolygonsMappings = new EditPolygonsMappings();
        editPolygonsMappings.loadImageData1(Base64.getDecoder().decode(data.get("image1")));
        editPolygonsMappings.loadImageData3(Base64.getDecoder().decode(data.get("image3")));
        editPolygonsMappings.model =new E3Model(new BufferedReader(new StringReader(Arrays.toString(data.get("model").getBytes()))), false, null);
        editPolygonsMappings.loadTxtData(data.get("textFile1"), 0);
        editPolygonsMappings.loadTxtData(data.get("textFile2"), 1);
        editPolygonsMappings.loadTxtData(data.get("textFile3"), 2);
        editPolygonsMappings.hdTextures = Objects.equals(data.get("hd_texture"), "true");
        switch (Integer.parseInt(data.get("selected_algorithm"))) {
            case 1:
                editPolygonsMappings.distanceABClass = DistanceProxLinear1.class;
                break;
            case 2:
                editPolygonsMappings.distanceABClass = DistanceProxLinear2.class;
                break;
            case 3:
                editPolygonsMappings.distanceABClass = DistanceProxLinear3.class;
                break;
            case 4:
                editPolygonsMappings.distanceABClass = DistanceProxLinear4.class;
                break;
            case 5:
                editPolygonsMappings.distanceABClass = DistanceProxLinear5.class;
                break;
            case 43:
                editPolygonsMappings.distanceABClass = DistanceProxLinear43.class;
                break;
            case 44:
                editPolygonsMappings.distanceABClass = DistanceProxLinear44.class;
                break;
            default:
                editPolygonsMappings.distanceABClass = DistanceIdent.class;
                break;
        }
        editPolygonsMappings.typeShape = !Objects.equals(data.get("selected_texture_type"), "quadr") ?DistanceAB.TYPE_SHAPE_QUADR:DistanceAB.TYPE_SHAPE_BEZIER;
        data.get("token");

        runApp = new Thread(editPolygonsMappings);
        runApp.start();

        while(editPolygonsMappings.zBufferImage==null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                editPolygonsMappings.isRunning = false;
                this.isRunning = false;
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        isRunning = false;
    }

    public Image getImage() {
        return new Image(editPolygonsMappings.zBufferImage);
    }
}
