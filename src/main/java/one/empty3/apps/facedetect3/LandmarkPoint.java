package one.empty3.apps.facedetect3;

import one.empty3.library.Point3D;

public class LandmarkPoint {
    private String name;
    private Point3D point;

    public LandmarkPoint(String name, Point3D point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point3D getPoint() {
        return point;
    }

    public void setPoint(Point3D point) {
        this.point = point;
    }
}
