package one.empty3.apps.facedetect;

import one.empty3.apps.feature.app.replace.javax.imageio.ImageIO;
import one.empty3.library.Point3D;
import one.empty3.libs.Image;
import one.empty3.libs.Images;

import java.awt.*;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConvexHull {
    private final List<Point3D> list;
    private final boolean[][] mask;
    List<Point3D> p = new ArrayList<>();
    private Image image;

    public ConvexHull(List<Point3D> list, Dimension2D dimension2D) {
        List<Point3D> list1 = new ArrayList<>();
        this.mask = new boolean[(int) dimension2D.getWidth()][(int) dimension2D.getHeight()];

        for (Point3D point3D : list) {
            list1.add(point3D.multDot(new Point3D(dimension2D.getWidth(), dimension2D.getHeight(), 0.0)));
        }
        this.list = list1;

        for (int i = 0; i < mask.length; i++) {
            Arrays.fill(mask[i], false);
        }
        createConvexHull();
    }

    public void createConvexHull() {
        Logger.getAnonymousLogger().log(Level.INFO, "ConvexHull started");
        double maxX = 0;
        Point3D first = null;
        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getX()>maxX) {
                maxX = list.get(i).getX();
                first = list.get(i);
            } else if(list.get(i).getY()==maxX &&(first==null|| list.get(i).getY()>first.getY())) {
                first = list.get(i);
            }
        }

        if(first==null) {
            Logger.getAnonymousLogger().log(Level.INFO , "ConvexHull first =="+first);
            return;
        }
        p.add(first);

        double a=Math.PI/2; // a vers -2*PI
        double angleCurrent = a;
        Point3D current = first;
        angleCurrent = 0;
        double at = 0;
        Point3D t;
        while(!list.isEmpty()) {
            Point3D selPoint = null;
            for (Point3D point3D : list) {
                if (!p.contains(point3D)) {
                    t = point3D.moins(current).norme1();
                    at = Math.atan2(t.getY(), t.getX());
                    if (at<=a && at >= angleCurrent) {
                        angleCurrent = at;
                        selPoint = point3D;
                    }
                }
            }
            if(selPoint==null||(selPoint==first&&p.size()>1))
                break;
            current = selPoint;

            p.add(current);
            a = angleCurrent;
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

        image.saveToFile("convexHull.jpg");

        Logger.getAnonymousLogger().log(Level.INFO, "ConvexHull done" + p.size()+"/"+list.size());
   }

    public boolean testIfIn(int x, int y) {
        if(x>=0&&x<image.getWidth()&&y>=0&&y<image.getHeight()) {
            return Point3D.distance(Point3D.fromColor(new one.empty3.libs.Color(image.getRgb(x,y))), new Point3D(1.,1.,1.))<0.1;
        }
        return false;
    }
}
