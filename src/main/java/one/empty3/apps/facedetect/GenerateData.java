package one.empty3.apps.facedetect;

import one.empty3.library.Point3D;
import one.empty3.library.objloader.E3Model;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class GenerateData extends IGenerateData {
    private static final int EDIT_POINT_POSITION = 1;
    private static final int SELECT_POINT_POSITION = 2;
    private static final int SELECT_POINT_VERTEX = 4;
    public static final int MULTIPLE = 1;
    private static final int SINGLE = 2;
    public BufferedImage zBufferImage;
    public int typeShape = DistanceAB.TYPE_SHAPE_QUADR;
    public boolean refineMatrix = false;
    public Dimension2D aDimReduced = new Dimension(20, 20);
    public Dimension2D bDimReduced = new Dimension(20, 20);
    public int durationMilliS = 30000;
    private int mode = EDIT_POINT_POSITION;
    int selectedPointNo = -1;
    protected E3Model model;
    protected TestHumanHeadTexturing testHumanHeadTexturing;
    boolean threadDistanceIsNotRunning = true;
    protected boolean isRunning = true;
    private Point3D pFound = null;
    private String landmarkType;
    private double u;
    private double v;
    private int selectedPointNoOut = -1;
    private Point3D selectedPointOutUv = null;
    private Point3D selectedPointVertexOut;
    TextureMorphMove iTextureMorphMove;
    boolean hasChangedAorB = true;
    boolean notMenuOpen = true;
    public HashMap<String, Point3D> pointsInModel = new HashMap<>();
    public HashMap<String, Point3D> pointsInImage = new HashMap<>();
    BufferedImage image;

    public Class<? extends DistanceAB> distanceABClass = DistanceProxLinear2.class;
    public boolean opt1 = false;
    public boolean optimizeGrid = false;
    boolean renderingStarted = false;
    boolean renderingStopped = true;
    int inImageType;
    int outTxtType;
    int inTxtType;
    File imagesDirectory;
    File txtInDirectory;
    File txtOutDirectory;

}
