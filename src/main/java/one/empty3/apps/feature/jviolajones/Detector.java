/*
 *
 *  * Copyright (c) 2024. Manuel Daniel Dahmen
 *  *
 *  *
 *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 *
 */

package one.empty3.apps.feature.jviolajones;

import one.empty3.apps.feature.jviolajones.Feature;
import one.empty3.apps.feature.jviolajones.Rect;
import one.empty3.apps.feature.jviolajones.Stage;
import one.empty3.apps.feature.jviolajones.Tree;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import javax.imageio.ImageIO;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.*;


public class Detector {
    /*
     * The list of classifiers that the test image should pass to be considered as an image.
     */
    List<one.empty3.apps.feature.jviolajones.Stage> stages;
    Point size;

    /*
     * Detector constructor.
     * Builds, from a XML file, the corresponding Haar cascade.
     *
     * @param filename The XML file (generated by OpenCV) describing the Haar cascade.
     */
    public Detector(String filename) throws java.io.FileNotFoundException {
        this(new FileInputStream(filename));
    }

    public Detector(InputStream input) {
        Document document = null;
        Element racine;
        stages = new LinkedList<one.empty3.apps.feature.jviolajones.Stage>();
        SAXBuilder sxb = new SAXBuilder();
        try {
            //On cr�e un nouveau document JDOM avec en argument le fichier XML
            //Le parsing est termin� ;)
            document = sxb.build(input);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //On initialise un nouvel element racine avec l'element racine du document.
        racine = (Element) document.getRootElement().getChildren().get(0);
        Scanner scanner = new Scanner(racine.getChild("size").getText());
        size = new Point(scanner.nextInt(), scanner.nextInt());
        Iterator it = racine.getChild("stages").getChildren("_").iterator();
        while (it.hasNext()) {
            Element stage = (Element) it.next();
            float thres = Float.parseFloat(stage.getChild("stage_threshold").getText());
            //Logger.getAnonymousLogger().log(Level.INFO, thres);
            Iterator it2 = stage.getChild("trees").getChildren("_").iterator();
            one.empty3.apps.feature.jviolajones.Stage st = new one.empty3.apps.feature.jviolajones.Stage(thres);
            while (it2.hasNext()) {
                Element tree = ((Element) it2.next());
                one.empty3.apps.feature.jviolajones.Tree t = new Tree();
                Iterator it4 = tree.getChildren("_").iterator();
                while (it4.hasNext()) {
                    Element feature = (Element) it4.next();
                    float thres2 = Float.parseFloat(feature.getChild("threshold").getText());
                    int left_node = -1;
                    float left_val = 0;
                    boolean has_left_val = false;
                    int right_node = -1;
                    float right_val = 0;
                    boolean has_right_val = false;
                    Element e;
                    if ((e = feature.getChild("left_val")) != null) {
                        left_val = Float.parseFloat(e.getText());
                        has_left_val = true;
                    } else {
                        left_node = Integer.parseInt(feature.getChild("left_node").getText());
                        has_left_val = false;
                    }

                    if ((e = feature.getChild("right_val")) != null) {
                        right_val = Float.parseFloat(e.getText());
                        has_right_val = true;
                    } else {
                        right_node = Integer.parseInt(feature.getChild("right_node").getText());
                        has_right_val = false;
                    }
                    one.empty3.apps.feature.jviolajones.Feature f = new Feature(thres2, left_val, left_node, has_left_val, right_val, right_node, has_right_val, size);
                    Iterator it3 = feature.getChild("feature").getChild("rects").getChildren("_").iterator();
                    while (it3.hasNext()) {
                        String s = ((Element) it3.next()).getText().trim();
                        //Logger.getAnonymousLogger().log(Level.INFO, s);
                        one.empty3.apps.feature.jviolajones.Rect r = Rect.fromString(s);
                        f.add(r);
                    }

                    t.addFeature(f);
                }
                st.addTree(t);
                //Logger.getAnonymousLogger().log(Level.INFO, "Number of nodes in tree "+t.features.size());
            }
            //Logger.getAnonymousLogger().log(Level.INFO, "Number of trees : "+ st.trees.size());
            stages.add(st);
        }
        //Logger.getAnonymousLogger().log(Level.INFO, stages.size());
    }

    public org.jdom2.Element getChild(Element element, String childName) {
        return null;
    }

    public List<Rectangle> getFaces(String filename, float baseScale, float scale_inc, float increment, int min_neighbors, boolean doCannyPruning) throws java.io.FileNotFoundException, java.io.IOException {
        return getFaces(new FileInputStream(filename), baseScale, scale_inc, increment, min_neighbors, doCannyPruning);
    }

    public List<Rectangle> getFaces(InputStream input, float baseScale, float scale_inc, float increment, int min_neighbors, boolean doCannyPruning) throws java.io.FileNotFoundException, java.io.IOException {
        return getFaces(ImageIO.read(input), baseScale, scale_inc, increment, min_neighbors, doCannyPruning);
    }

    /*
     * Returns the list of detected objects in an image applying the Viola-Jones algorithm.
     * <p>
     * The algorithm tests, from sliding windows on the image, of variable size, which regions should be considered as searched objects.
     * Please see Wikipedia for a description of the algorithm.
     *
     * @param image     bufferedimage input
     * @param baseScale The initial ratio between the window size and the Haar classifier size (default 2).
     * @param scale_inc The scale increment of the window size, at each step (default 1.25).
     * @param increment The shift of the window at each sub-step, in terms of percentage of the window size.
     * @return the list of rectangles containing searched objects, expressed in pixels.
     */
    public List<Rectangle> getFaces(BufferedImage image, float baseScale, float scale_inc, float increment, int min_neighbors, boolean doCannyPruning) {
        List<Rectangle> ret = new ArrayList<Rectangle>();
        int width = image.getWidth();
        int height = image.getHeight();
        float maxScale = (float) Math.min((width + 0.f) / size.x, (height + 0.0f) / size.y);
        int[][] grayImage = new int[width][height];
        int[][] img = new int[width][height];
        int[][] squares = new int[width][height];
        for (int i = 0; i < width; i++) {
            int col = 0;
            int col2 = 0;
            for (int j = 0; j < height; j++) {
                int c = image.getRGB(i, j);
                int red = (c & 0x00ff0000) >> 16;
                int green = (c & 0x0000ff00) >> 8;
                int blue = c & 0x000000ff;
                int value = (30 * red + 59 * green + 11 * blue) / 100;
                img[i][j] = value;
                grayImage[i][j] = (i > 0 ? grayImage[i - 1][j] : 0) + col + value;
                squares[i][j] = (i > 0 ? squares[i - 1][j] : 0) + col2 + value * value;
                col += value;
                col2 += value * value;
            }
        }
        int[][] canny = null;
        if (doCannyPruning)
            canny = getIntegralCanny(img);
        for (float scale = baseScale; scale < maxScale; scale *= scale_inc) {
            int step = (int) (scale * 24 * increment);
            int size = (int) (scale * 24);
            for (int i = 0; i < width - size; i += step) {
                for (int j = 0; j < height - size; j += step) {
                    if (doCannyPruning) {
                        int edges_density = canny[i + size][j + size] + canny[i][j] - canny[i][j + size] - canny[i + size][j];
                        int d = edges_density / size / size;
                        if (d < 20 || d > 100)
                            continue;
                    }
                    boolean pass = true;
                    int k = 0;
                    for (Stage s : stages) {

                        if (!s.pass(grayImage, squares, i, j, scale)) {
                            pass = false;
                            //Logger.getAnonymousLogger().log(Level.INFO, "Failed at Stage "+k);
                            break;
                        }
                        k++;
                    }
                    if (pass) ret.add(new Rectangle(i, j, size, size));
                }
            }
        }
        return merge(ret, min_neighbors);
    }

    public int[][] getIntegralCanny(int[][] grayImage) {
        int[][] canny = new int[grayImage.length][grayImage[0].length];
        for (int i = 2; i < canny.length - 2; i++)
            for (int j = 2; j < canny[0].length - 2; j++) {
                int sum = 0;
                sum += 2 * grayImage[i - 2][j - 2];
                sum += 4 * grayImage[i - 2][j - 1];
                sum += 5 * grayImage[i - 2][j + 0];
                sum += 4 * grayImage[i - 2][j + 1];
                sum += 2 * grayImage[i - 2][j + 2];
                sum += 4 * grayImage[i - 1][j - 2];
                sum += 9 * grayImage[i - 1][j - 1];
                sum += 12 * grayImage[i - 1][j + 0];
                sum += 9 * grayImage[i - 1][j + 1];
                sum += 4 * grayImage[i - 1][j + 2];
                sum += 5 * grayImage[i + 0][j - 2];
                sum += 12 * grayImage[i + 0][j - 1];
                sum += 15 * grayImage[i + 0][j + 0];
                sum += 12 * grayImage[i + 0][j + 1];
                sum += 5 * grayImage[i + 0][j + 2];
                sum += 4 * grayImage[i + 1][j - 2];
                sum += 9 * grayImage[i + 1][j - 1];
                sum += 12 * grayImage[i + 1][j + 0];
                sum += 9 * grayImage[i + 1][j + 1];
                sum += 4 * grayImage[i + 1][j + 2];
                sum += 2 * grayImage[i + 2][j - 2];
                sum += 4 * grayImage[i + 2][j - 1];
                sum += 5 * grayImage[i + 2][j + 0];
                sum += 4 * grayImage[i + 2][j + 1];
                sum += 2 * grayImage[i + 2][j + 2];

                canny[i][j] = sum / 159;
                //Logger.getAnonymousLogger().log(Level.INFO, canny[i][j]);
            }
        int[][] grad = new int[grayImage.length][grayImage[0].length];
        for (int i = 1; i < canny.length - 1; i++)
            for (int j = 1; j < canny[0].length - 1; j++) {
                int grad_x = -canny[i - 1][j - 1] + canny[i + 1][j - 1] - 2 * canny[i - 1][j] + 2 * canny[i + 1][j] - canny[i - 1][j + 1] + canny[i + 1][j + 1];
                int grad_y = canny[i - 1][j - 1] + 2 * canny[i][j - 1] + canny[i + 1][j - 1] - canny[i - 1][j + 1] - 2 * canny[i][j + 1] - canny[i + 1][j + 1];
                grad[i][j] = Math.abs(grad_x) + Math.abs(grad_y);
                //Logger.getAnonymousLogger().log(Level.INFO, grad[i][j]);
            }
        //JFrame f = new JFrame();
        //f.setContentPane(new DessinChiffre(grad));
        //f.setVisible(true);
        for (int i = 0; i < canny.length; i++) {
            int col = 0;
            for (int j = 0; j < canny[0].length; j++) {
                int value = grad[i][j];
                canny[i][j] = (i > 0 ? canny[i - 1][j] : 0) + col + value;
                col += value;
            }
        }
        return canny;
    }

    public List<Rectangle> merge(List<Rectangle> rects, int min_neighbors) {
        List<Rectangle> retour = new LinkedList<Rectangle>();
        int[] ret = new int[rects.size()];
        int nb_classes = 0;
        for (int i = 0; i < rects.size(); i++) {
            boolean found = false;
            for (int j = 0; j < i; j++) {
                if (equals(rects.get(j), rects.get(i))) {
                    found = true;
                    ret[i] = ret[j];
                }
            }
            if (!found) {
                ret[i] = nb_classes;
                nb_classes++;
            }
        }
        //Logger.getAnonymousLogger().log(Level.INFO, Arrays.toString(ret));
        int[] neighbors = new int[nb_classes];
        Rectangle[] rect = new Rectangle[nb_classes];
        for (int i = 0; i < nb_classes; i++) {
            neighbors[i] = 0;
            rect[i] = new Rectangle(0, 0, 0, 0);
        }
        for (int i = 0; i < rects.size(); i++) {
            neighbors[ret[i]]++;
            rect[ret[i]].x += rects.get(i).x;
            rect[ret[i]].y += rects.get(i).y;
            rect[ret[i]].height += rects.get(i).height;
            rect[ret[i]].width += rects.get(i).width;
        }
        for (int i = 0; i < nb_classes; i++) {
            int n = neighbors[i];
            if (n >= min_neighbors) {
                Rectangle r = new Rectangle(0, 0, 0, 0);
                r.x = (rect[i].x * 2 + n) / (2 * n);
                r.y = (rect[i].y * 2 + n) / (2 * n);
                r.width = (rect[i].width * 2 + n) / (2 * n);
                r.height = (rect[i].height * 2 + n) / (2 * n);
                retour.add(r);
            }
        }
        return retour;
    }

    public boolean equals(Rectangle r1, Rectangle r2) {
        int distance = (int) (r1.width * 0.2);

        /*return r2.x <= r1.x + distance &&
               r2.x >= r1.x - distance &&
               r2.y <= r1.y + distance &&
               r2.y >= r1.y - distance &&
               r2.width <= (int)( r1.width * 1.2 ) &&
               (int)( r2.width * 1.2 ) >= r1.width;*/
        if (r2.x <= r1.x + distance &&
                r2.x >= r1.x - distance &&
                r2.y <= r1.y + distance &&
                r2.y >= r1.y - distance &&
                r2.width <= (int) (r1.width * 1.2) &&
                (int) (r2.width * 1.2) >= r1.width) return true;
        if (r1.x >= r2.x && r1.x + r1.width <= r2.x + r2.width && r1.y >= r2.y && r1.y + r1.height <= r2.y + r2.height)
            return true;
        return false;
    }
}