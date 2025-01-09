package one.empty3.apps.facedetect;

import one.empty3.library.Point3D;
import one.empty3.libs.Image;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

public class ConvexHull {
    private final List<Point3D> list;
    private final boolean[][] mask;
    List<Point3D> p = new ArrayList<>();
    private Image image;

    public ConvexHull(List<Point3D> list, Dimension2D dimension2D) {
        this.list = list;
        this.mask = new boolean[(int) dimension2D.getWidth()][(int) dimension2D.getHeight()];

        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).multDot(new Point3D(dimension2D.getWidth(), dimension2D.getHeight(), 0.0)));
        }

        for (int i = 0; i < mask.length; i++) {
            for (int j = 0; j < mask[i].length; j++) {
                mask[i][j] = false;
            }
        }
        createConvexHull();
    }

    public void createConvexHull() {
        double maxY = 0;
        Point3D first = null;
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getY()>maxY) {
                maxY = list.get(i).getY();
                first = list.get(i);
            } else if(list.get(i).getY()==maxY &&(first==null|| list.get(i).getX()>first.getY())) {
                first = list.get(i);
            }
        }
        p.add(first);

        Point3D c = first;
        double a=0; // a vers -2*PI
        double angleCurrent = a;
        while(!list.isEmpty()) {
            Point3D current = null;
            angleCurrent = Math.PI*2;
            for (int i = 0; i < list.size(); i++) {
                if(!p.contains(list.get(i))) {
                    Point3D t = list.get(i).moins(current==null?c:current).norme1();
                    double at = Math.atan2(t.getY(),t.getX());
                    if(at>a&&at<angleCurrent) {
                        a = at;
                        angleCurrent = at;
                        current = t;
                    }
                } else
                    break;
            }
            if(current==first ||current==null)
                break;
            p.add(current);
        }
        Image image = new Image(mask.length, mask[0].length);


        int [] xPoints = new int[p.size()];
        int [] yPoints = new int[p.size()];
        for(int i=0; i<p.size(); i++) {
            xPoints[i] = (int) p.get(i).getX();
            yPoints[i] = (int) p.get(i).getY();
        }

        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillPolygon(xPoints, yPoints, p.size());

        this.image = image;
    }

    public boolean testIfIn(int x, int y) {
        if(x>=0&&x<image.getWidth()&&y>=0&&y<image.getHeight()) {
            return image.getRgb(x, y)==0xFFFFFFFF;
        }
        return false;
    }
}
