///*
// *
// *  * Copyright (c) 2024. Manuel Daniel Dahmen
// *  *
// *  *
// *  *    Copyright 2024 Manuel Daniel Dahmen
// *  *
// *  *    Licensed under the Apache License, Version 2.0 (the "License");
// *  *    you may not use this file except in compliance with the License.
// *  *    You may obtain a copy of the License at
// *  *
// *  *        http://www.apache.org/licenses/LICENSE-2.0
// *  *
// *  *    Unless required by applicable law or agreed to in writing, software
// *  *    distributed under the License is distributed on an "AS IS" BASIS,
// *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  *    See the License for the specific language governing permissions and
// *  *    limitations under the License.
// *
// *
// */
//
//package one.empty3.matrix;
//
//import one.empty3.feature.FilterPixM;
//import one.empty3.feature.M3;
//import one.empty3.library.Serialisable;
//import org.jetbrains.annotations.NotNull;
//
//import one.empty3.libs.*;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class PixM extends M implements Serializable, Serialisable {
//
//    public PixM(int l, int c) {
//        super(l, c);
//    }
//
//    public PixM(@NotNull Image image) {
//        super(image);
//    }
//    public PixM(@NotNull BufferedImage image) {
//        super(new Image(image));
//    }
//
//    public PixM(double[][] distances) {
//        super(distances);
//    }
//
//    public static one.empty3.feature.PixM getPixM(Image image, double maxRes) {
//        double f = (double)1.0F;
//        if (maxRes < (double)image.getWidth() && maxRes < (double)image.getHeight()) {
//            f = (double)1.0F / (double)Math.max(image.getWidth(), image.getHeight()) * maxRes;
//        }
//
//        if (maxRes == (double)0.0F) {
//            f = (double)1.0F;
//        }
//
//        double columns2 = (double)1.0F * (double)image.getWidth() * f;
//        double lines2 = (double)1.0F * (double)image.getHeight() * f;
//        one.empty3.feature.PixM pixM = new one.empty3.feature.PixM((int)columns2, (int)lines2);
//
//        for(int i = 0; i < (int)columns2; ++i) {
//            for(int j = 0; j < (int)lines2; ++j) {
//                int rgb = image.getRgb((int)((double)1.0F * (double)i / columns2 * (double)image.getWidth()), (int)((double)1.0F * (double)j / lines2 * (double)image.getHeight()));
//                pixM.set(pixM.index(i, j), rgb);
//            }
//        }
//
//        return pixM;
//    }
//
//    public static PixM getPixM(File in, double res) {
//        Image image = null;
//        try {
//            image = new Image(ImageIO.read(in));
//        } catch (IOException e) {
//            Logger.getLogger(PixM.class.getName()).log(Level.SEVERE,
//                    "Error constructing PixM: IOException", e);
//        }
//        assert image != null;
//        return getPixM(image, res);
//    }
//
//    public static PixM getPixM(BufferedImage img, double res) {
//        Image image = new Image(img);
//        return getPixM(image, res);
//    }
//
//
//    public void colorsRegion(int x, int y, int w, int h, double[] comps) {
//        for(int i = x; i < x + w; ++i) {
//            for(int j = y; j < y + h; ++j) {
//                for(int c = 0; c < comps.length; ++c) {
//                    this.setCompNo(c);
//                    this.set(i, j, comps[c]);
//                }
//            }
//        }
//
//    }
//
//    public  one.empty3.feature.PixM getColorsRegion(int x, int y, int w, int h, int sizeX, int sizeY) {
//         one.empty3.feature.PixM subimage = new  one.empty3.feature.PixM(sizeX, sizeY);
//
//        for(int i = x; i < x + w; ++i) {
//            for(int j = y; j < y + h; ++j) {
//                for(int c = 0; c < this.getCompCount(); ++c) {
//                    this.setCompNo(c);
//                    subimage.setCompNo(c);
//                    double v = this.get(i, j);
//                    subimage.set((int)((double)1.0F * (double)(x + w - i) / (double)w * (double)subimage.getColumns()), (int)((double)1.0F * (double)(y + h - j) / (double)h * (double)subimage.getLines()), v);
//                    this.set(i, j, v);
//                }
//            }
//        }
//
//        return subimage;
//    }
//
//    public void colorsRegion(int x, int y, int w, int h,  one.empty3.feature.PixM subimage, int subImageCopyMode) {
//        for(int i = x; i < x + w; ++i) {
//            for(int j = y; j < y + h; ++j) {
//                for(int c = 0; c < this.getCompCount(); ++c) {
//                    this.setCompNo(c);
//                    subimage.setCompNo(c);
//                    double v = subimage.get((int)((double)1.0F * (double)(x + w - i) / (double)w * (double)subimage.getColumns()), (int)((double)1.0F * (double)(y + h - j) / (double)h * (double)subimage.getLines()));
//                    this.set(i, j, v);
//                }
//            }
//        }
//
//    }
//
//    public one.empty3.feature.PixM applyFilter(FilterPixM filter) {
//        one.empty3.feature.PixM c = new one.empty3.feature.PixM(this.getColumns(), this.getLines());
//
//        for(int comp = 0; comp < this.getCompCount(); ++comp) {
//            this.setCompNo(comp);
//            c.setCompNo(comp);
//            filter.setCompNo(comp);
//
//            for(int i = 0; i < this.getColumns(); ++i) {
//                for(int j = 0; j < this.getLines(); ++j) {
//                    c.set(i, j, (double)0.0F);
//                    double sum = (double)0.0F;
//
//                    for(int u = -filter.getColumns() / 2; u <= filter.getLines() / 2; ++u) {
//                        for(int v = -filter.getLines() / 2; v <= filter.getLines() / 2; ++v) {
//                            double filterUVvalue = filter.get(u + filter.getColumns() / 2, v + filter.getLines() / 2);
//                            double vAtUv = this.get(i + u, j + v);
//                            if (vAtUv != noValue) {
//                                c.set(i, j, c.get(i, j) + filterUVvalue * vAtUv);
//                                sum += filterUVvalue;
//                            }
//                        }
//                    }
//
//                    c.set(i, j, c.get(i, j) / sum);
//                }
//            }
//        }
//
//        return c;
//    }
//    public  one.empty3.feature.PixM pasteSubImage(int x, int y, int w, int h) {
//         one.empty3.feature.PixM p2 = new  one.empty3.feature.PixM(w, h);
//
//        for(int i = x; i < x + w; ++i) {
//            for(int j = y; j < y + h; ++j) {
//                for(int c = 0; c < this.getCompCount(); ++c) {
//                    this.setCompNo(c);
//                    p2.setCompNo(c);
//                    double v = this.get(i, j);
//                    this.set(i - x, j - y, v);
//                }
//            }
//        }
//
//        return p2;
//    }
//
//    public PixM getMatrix(int ii, int ij) {
//        PixM matrix = new PixM(columns, lines);
//        for (int i = 0; i < columns; i++) {
//            for (int j = 0; j < lines; j++) {
//                for (int c = 0; c < getCompCount(); c++) {
//                    setCompNo(c);
//                    matrix.setCompNo(c);
//                    //matrix.set(i, j, get(i, j, ii, ij));
//                }
//            }
//        }
//        return matrix;
//    }
//
//
//    public one.empty3.feature.PixM normalize(double min, double max) {
//        double[] maxRgbai = new double[3];
//        double[] meanRgbai = new double[3];
//        double[] minRgbai = new double[3];
//        double minA = (double)0.0F;
//        double maxA = (double)1.0F;
//        if (min != (double)-1.0F || max != (double)-1.0F) {
//            minA = min;
//            maxA = max;
//        }
//
//        for(int i = 0; i < 3; ++i) {
//            maxRgbai[i] = maxA;
//            minRgbai[i] = minA;
//            meanRgbai[i] = (double)0.0F;
//        }
//
//        for(int i = 0; i < this.getColumns(); ++i) {
//            for(int j = 0; j < this.getLines(); ++j) {
//                for(int comp = 0; comp < this.getCompCount(); ++comp) {
//                    this.setCompNo(comp);
//                    double valueAt = this.get(i, j);
//                    if (Double.isNaN(valueAt) && Double.isInfinite(valueAt)) {
//                        valueAt = (double)0.0F;
//                        this.set(i, j, valueAt);
//                    } else {
//                        if (valueAt > maxRgbai[comp]) {
//                            maxRgbai[comp] = valueAt;
//                        }
//
//                        if (valueAt < minRgbai[comp]) {
//                            minRgbai[comp] = valueAt;
//                        }
//                    }
//
//                    meanRgbai[comp] += valueAt / (double)(this.getLines() * this.getColumns());
//                }
//            }
//        }
//
//        one.empty3.feature.PixM image = new one.empty3.feature.PixM(this.getColumns(), this.getLines());
//
//        for(int i = 0; i < image.getColumns(); ++i) {
//            for(int j = 0; j < image.getLines(); ++j) {
//                for(int comp = 0; comp < this.getCompCount(); ++comp) {
//                    this.setCompNo(comp);
//                    image.setCompNo(this.compNo);
//                    float value = (float)((this.get(i, j) - minRgbai[comp]) / (maxRgbai[comp] - minRgbai[comp]));
//                    image.set(i, j, (double)value);
//                }
//            }
//        }
//
//        return image;
//    }
//
//    public  one.empty3.feature.PixM normalize(double inMin, double inMax, double min, double max) {
//        double[] maxRgbai = new double[3];
//        double[] meanRgbai = new double[3];
//        double[] minRgbai = new double[3];
//        double minA = (double)0.0F;
//        double maxA = (double)1.0F;
//        if (min != (double)-1.0F || max != (double)-1.0F) {
//            ;
//        }
//
//        for(int i = 0; i < this.getCompCount(); ++i) {
//            maxRgbai[i] = (double)255.0F;
//            minRgbai[i] = (double)0.0F;
//        }
//
//         one.empty3.feature.PixM image = new  one.empty3.feature.PixM(this.getColumns(), this.getLines());
//
//        for(int i = 0; i < image.getColumns(); ++i) {
//            for(int j = 0; j < image.getLines(); ++j) {
//                for(int comp = 0; comp < this.getCompCount(); ++comp) {
//                    this.setCompNo(comp);
//                    image.setCompNo(this.compNo);
//                    float value = (float)((this.get(i, j) - minRgbai[comp]) / (maxRgbai[comp] - minRgbai[comp]));
//                    image.set(i, j, (double)value);
//                }
//            }
//        }
//
//        return image;
//    }
//
//    public one.empty3.feature.PixM subSampling(double div) {
//        double columns2 = (double)1.0F * (double)this.getColumns() / div;
//        double lines2 = (double)1.0F * (double)this.getLines() / div;
//        double cli2 = (double)1.0F / div;
//        one.empty3.feature.PixM pixM = new one.empty3.feature.PixM((int)columns2, (int)lines2);
//
//        for(int c = 0; c < this.getCompCount(); ++c) {
//            this.setCompNo(c);
//            pixM.setCompNo(c);
//
//            for(int i = 0; i < (int)columns2; ++i) {
//                for(int j = 0; j < (int)lines2; ++j) {
//                    double m = this.mean((int)((double)i * div), (int)((double)j * div), (int)(cli2 * div), (int)(cli2 * div));
//                    pixM.set(i, j, m);
//                }
//            }
//        }
//
//        return pixM;
//    }
//
//    public double mean(int i, int j, int w, int h) {
//        double m = (double)0.0F;
//        int p = 0;
//
//        for(int a = i; a < i + w; ++a) {
//            for(int b = j; b < j + h; ++b) {
//                m += this.get(a, b);
//                ++p;
//            }
//        }
//
//        return m / (double)p;
//    }
//    public one.empty3.feature.PixM copySubImage(int x, int y, int w, int h) {
//        one.empty3.feature.PixM p2 = new one.empty3.feature.PixM(w, h);
//
//        for(int i = x; i <= x + w; ++i) {
//            for(int j = y; j <= y + h; ++j) {
//                for(int c = 0; c < this.getCompCount(); ++c) {
//                    this.setCompNo(c);
//                    p2.setCompNo(c);
//                    p2.set(i - x, j - y, this.get(i, j));
//                }
//            }
//        }
//
//        return p2;
//    }
//
//    public void colorsRegionWithMask(int x, int y, int w, int h,  one.empty3.feature.PixM subimage,  one.empty3.feature.PixM addMask) {
//        for(int i = x; i < x + w; ++i) {
//            for(int j = y; j < y + h; ++j) {
//                for(int c = 0; c < this.getCompCount(); ++c) {
//                    this.setCompNo(c);
//                    subimage.setCompNo(c);
//                    addMask.setCompNo(c);
//                    double compOrigi = this.get(i, j);
//                    double compPaste = subimage.get((int)((double)1.0F * (double)(i - x) / (double)w * (double)subimage.getColumns()), (int)((double)1.0F * (double)(j - y) / (double)h * (double)subimage.getLines()));
//                    double compMask = addMask.get((int)((double)1.0F * (double)(i - x) / (double)w * (double)subimage.getColumns()), (int)((double)1.0F * (double)(j - y) / (double)h * (double)subimage.getLines()));
//                    double v = compOrigi * ((double)1.0F - compMask) + compPaste * compMask;
//                    this.set(i, j, v);
//                }
//            }
//        }
//
//    }
//
//    public double luminance(int x, int y) {
//        double l = (double)0.0F;
//        this.setCompNo(0);
//        l += 0.2126 * this.get(x, y);
//        this.setCompNo(1);
//        l += 0.7152 * this.get(x, y);
//        this.setCompNo(2);
//        l += 0.722 * this.get(x, y);
//        return l;
//    }
//
//    public double norme(int x, int y) {
//        double l = (double)0.0F;
//        this.setCompNo(0);
//        l += this.get(x, y);
//        this.setCompNo(1);
//        l += this.get(x, y);
//        this.setCompNo(2);
//        l += this.get(x, y);
//        return l;
//    }
//
//    public Image getImage2() {
//        return getImage();
//    }
//
//    public Image getImage() {
//        Image image = new Image(this.getColumns(), this.getLines(), BufferedImage.TYPE_INT_RGB);
//
//        for(int i = 0; i < image.getWidth(); ++i) {
//            for(int j = 0; j < image.getHeight(); ++j) {
//                image.setRgb(i, j, this.getInt(i, j));
//            }
//        }
//
//        return image;
//    }
//    public void setRegionCopy(one.empty3.feature.M3 original, int ii, int ij, int iStart, int jStart, int iEnd, int jEnd,
//                              one.empty3.feature.PixM pixM, int iPaste, int jPaste) {
//        for (int c = 0; c < getCompCount(); c++) {
//            original.setCompNo(c);
//            pixM.setCompNo(c);
//            int x = 0;
//            for (int i = iStart; i < iEnd; i++) {
//                int y = 0;
//                for (int j = jStart; j < jEnd; j++) {
//                    double v = original.get(i, j, ii, ij);
//                    pixM.set(iPaste + x, jPaste + y, v);
//                    y++;
//                }
//                x++;
//            }
//
//
//        }
//    }
//    @Override
//    public one.empty3.feature.PixM copy() {
//        one.empty3.feature.PixM pixM = new one.empty3.feature.PixM(this.getColumns(), this.getLines());
//
//        for(int i = 0; i < this.getColumns(); ++i) {
//            for(int j = 0; j < this.getLines(); ++j) {
//                pixM.set(this.index(i, j), this.getInt(i, j));
//            }
//        }
//
//        return pixM;
//    }
//
//    public PixM applyFilter(one.empty3.import one.empty3.apps.feature.FilterMatPixM;
import one.empty3.feature.FilterPixM;

 function) {
//        return null;
//    }
//}
//
////
////public static final int COMP_RED = 0;
////public static final int COMP_GREEN = 1;
////public static final int COMP_BLUE = 2;
////public static final int COMP_ALPHA = 3;
////public static final int COMP_INTENSITY = 4;
////private int MAX_DISTANCE_ITERATIONS = 100;
////
////public PixM(int l, int c) {
////    super(l, c);
////}
////
////public PixM(@NotNull BufferedImage image) {
////    super(image.getWidth(), image.getHeight());
////    for (int i = 0; i < image.getWidth(); i++) {
////        for (int j = 0; j < image.getHeight(); j++) {
////            int rgb = image.getRGB(i, j);
////            set(i, j, rgb);
////        }
////    }
////}
////
////public PixM(double[][] distances) {
////    super(distances.length, distances[0].length);
////    setCompNo(0);
////    for (int i = 0; i < getColumns(); i++)
////        for (int j = 0; j < getLines(); j++)
////            set(i, j, distances[i][j]);
////}
////
////public Point3D getRgb(int i, int j) {
////    setCompNo(0);
////    double dr = get(i, j);
////    setCompNo(1);
////    double dg = get(i, j);
////    setCompNo(2);
////    double db = get(i, j);
////    return new Point3D(dr, dg, db);
////}
////
////public static PixM getPixM(BufferedImage image, double maxRes) {
////    double f = 1.0;
////    if (maxRes < image.getWidth() && maxRes < image.getHeight())
////        f = 1.0 / Math.max(image.getWidth(), image.getHeight()) * maxRes;
////    if (maxRes == 0) {
////        f = 1.0;
////    }
////    double columns2 = 1.0 * image.getWidth() * f;
////    double lines2 = 1.0 * image.getHeight() * f;
////    //Logger.getAnonymousLogger().log(Level.INFO, "PixM resizing init  --> (" + maxRes + ", " + maxRes + ")  (" + columns2 + ", " + lines2 + ")");
////    PixM pixM = new PixM((int) (columns2), ((int) lines2));
////
////
////    for (int i = 0; i < (int) columns2; i++) {
////        for (int j = 0; j < (int) lines2; j++) {
////
////
////            int rgb = image.getRGB(
////                    (int) (1.0 * i / columns2 * image.getWidth())
////                    , (int) (1.0 * j / lines2 * image.getHeight()));
////            pixM.set(pixM.index(i, j), rgb);
////
////        }
////
////    }
////    return pixM;
////
////
////}
////
////public PixM applyFilter(FilterPixM filter) {
////    PixM c = new PixM(columns, lines);
////    double sum;
////    for (int comp = 0; comp < getCompCount(); comp++) {
////
////        setCompNo(comp);
////        c.setCompNo(comp);
////        filter.setCompNo(comp);
////
////
////        for (int i = 0; i < columns; i++) {
////            for (int j = 0; j < lines; j++) {
////                c.set(i, j, 0.0); //???
////                sum = 0.0;
////                for (int u = -filter.getColumns()
////                        / 2; u <= filter.getLines() / 2; u++) {
////                    for (int v = -filter.getLines() / 2; v <= filter.getLines() / 2; v++) {
////
////
////                        double filterUVvalue = filter.get(u + filter.getColumns()
////                                        / 2,
////                                v + filter.getLines() / 2);
////                        double vAtUv = get(i + u, j + v);
////                        if (!(vAtUv == noValue)) {
////
////                            c.set(i, j, c.get(i, j) + filterUVvalue * vAtUv);
////                            sum += filterUVvalue;
////
////                        }
////
////
////                    }
////                }
////                c.set(i, j, c.get(i, j) / sum);
////            }
////        }
////    }
////    return c;
////}
////
////public one.empty3.apps.feature.V derivative(int x, int y, int order, one.empty3.apps.feature.V originValue) {
////    if (originValue == null) {
////        originValue = new V(2, 1);
////        originValue.set(0, 0, get(x, y));
////        originValue.set(1, 0, get(x, y));
////    }
////    originValue.set(0, 0, -get(x + 1, y) + 2 * get(x, y) - get(x - 1, y));
////    originValue.set(1, 0, -get(x, y + 1) + 2 * get(x, y) - get(x, y - 1));
////    if (order > 0) {
////        derivative(x, y, order - 1, originValue);
////    }
////
////    return originValue;
////}
////
////
////public void plotCurve(ParametricCurve curve, ITexture texture) {
////    double INCR_T = curve.getIncrU().getElem();
////
////    float[] rgba = new float[getCompCount()];
////    for (double t = 0; t < 1.0; t += INCR_T) {
////        rgba = new Color((texture != null ? texture : curve.texture()).getColorAt(t, 0.5)).getColorComponents(rgba);
////        Point3D p = curve.calculerPoint3D(t);
////        for (int c = 0; c < 3; c++) {
////            setCompNo(c);
////            set((int) (double) p.getX(), (int) (double) p.getY(), rgba[c]);
////        }
////    }
////
////}
////
////double INCR_T = 0.0001;
////
////public void plotCurveRaw(ParametricCurve curve, ITexture texture) {
////    INCR_T = curve.getIncrU().getElem();
////    float[] rgba = new float[getCompCount()];
////    Point3D p0 = null;
////    for (double t = 0; t < 1.0; t += INCR_T) {
////        rgba = new Color(curve.texture().getColorAt(t, 0.5)).getColorComponents(rgba);
////        Point3D p = curve.calculerPoint3D(t);
////        for (int c = 0; c < 3; c++) {
////            setCompNo(c);
////            set((int) (double) p.getX(), (int) (double) p.getY(), rgba[c]);
////        }
////    }
////}
////
/////*
////
////    public void plotCurve(ParametricCurve curve, Color color, int x, int y) {
////
////        float[] rgba = new float[getCompCount()];
////        color.getColorComponents(rgba);
////        for (double t = 0; t < 1.0; t += 0.001) {
////            for (int c = 0; c < 3; c++) {
////                Point3D p = curve.calculerPoint3D(t);
////                setCompNo(c);
////                set((int) (double) p.getX(), (int) (double) p.getY(), rgba[c]);
////            }
////        }
////    }
////*/
////
////public void fillIn(ParametricCurve curve, ITexture texture, ITexture borderColor) {
////    int[] columnIn = new int[getLines()];
////    int[] columnOut = new int[getLines()];
////    for (int i = 0; i < getLines(); i++) {
////        columnIn[i] = -1;
////        columnOut[i] = -1;
////    }
////    float[] rgba = new float[getCompCount()];
////    float[] rgbaBorder = new float[getCompCount()];
////
////
////    Point3D old = Point3D.O0;
////    double incrRef = (1.0 / getColumns());
////    Point3D p0 = curve.calculerPoint3D(curve.getStartU());
////    double deltaT = incrRef;
////    for (double t = curve.getStartU() - (double) (curve.getIncrU().getElem()); t < curve.getEndU() + (double) curve.getIncrU().getElem();
////         t += incrRef) {
////
////        Point3D p = curve.calculerPoint3D(t);
/////*
////            double incrRef0 = incrRef;
////            do {
////                p0 = p;
////
////                deltaT = p.moins(p0).norme();
////
////
////                if (deltaT > 1.5) {
////                    incrRef /= 1.5;
////                }
////                if (deltaT < 0.1) {
////                    incrRef *= 2;
////                }
////
////                p = curve.calculerPoint3D(t);
////            } while (p.moins(p0).norme() > 1.5 || p.moins(p0).norme() < 0.1);
////
////            t = t - incrRef0 + incrRef;
////*/
////        int x = (int) (double) p.get(0);
////        int y = (int) (double) p.get(1);
////        int xOld = (int) (double) old.get(0);
////        int yOld = (int) (double) old.get(1);
////
////
////        if (y >= columnIn.length || y < 0)
////            continue;
////
////        int abs = Math.abs(columnIn[y] - x);
////
////        if (x >= 0 && x < getColumns() && y >= 0 && y < getLines()
////                && abs > 2 && (columnIn[y] == -1 || columnOut[y] == -1)) {
////
////
////            if (columnIn[y] == -1) {
////                columnIn[y] = x;
////            } else if (columnOut[y] == -1 || columnOut[y] != x) {// ADDED columnOut[y] != x
////                if (columnIn[y] > x) {
////                    columnOut[y] = columnIn[y];
////                    columnIn[y] = x;
////                } else {
////                    columnOut[y] = x;
////                }
////            }
////        }
////        old = p;
////        p0 = curve.calculerPoint3D(curve.getStartU());
////    }
////
/////*
////        for (int i = 0; i < columnIn.length; i++) {
////            System.out.printf("LinesIn [ i %d ] = %d\n", i, columnIn[i]);
////            System.out.printf("LinesOut[ i %d ] = %d\n", i, columnOut[i]);
////        }
////*/
////
////
////    for (int y = 0; y < columnIn.length; y++) {
////        if (columnIn[y] != -1 && columnOut[y] != -1) {
////            plotCurve(new LineSegment(Point3D.n(columnIn[y], y, 0), Point3D.n(columnOut[y], y, 0)),
////                    texture);
////        }
////        if (columnIn[y] != -1)
////            setValues(columnIn[y], y, Lumiere.getDoubles(
////                    borderColor.getColorAt(1.0 * columnIn[y] / getColumns(), 1.0 * y / getLines())));
////        if (columnOut[y] != -1)
////            setValues(columnOut[y], y, Lumiere.getDoubles(
////                    borderColor.getColorAt(1.0 * columnIn[y] / getColumns(), 1.0 * y / getLines())));
////
////
////    }
////
////
////}
////
////public PixM normalize(final double min, final double max) {
////
////    double[] maxRgbai = new double[compCount];
////    double[] meanRgbai = new double[compCount];
////    double[] minRgbai = new double[compCount];
////    double minA = 0.0;
////    double maxA = 1.0;
////    if (min != -1 || max != -1) {
////        minA = min;
////        maxA = max;
////    }
////    for (int i = 0; i < 3; i++) {
////        maxRgbai[i] = maxA;
////        minRgbai[i] = minA;
////        meanRgbai[i] = 0.0;
////    }
////    for (int i = 0; i < columns; i++) {
////        for (int j = 0; j < lines; j++) {
////            for (int comp = 0; comp < getCompCount(); comp++) {
////                setCompNo(comp);
////                double valueAt = get(i, j);
////                if (!Double.isNaN(valueAt) || !Double.isInfinite(valueAt)) {
////                    if (valueAt > maxRgbai[comp]) {
////                        maxRgbai[comp] = valueAt;
////                    }
////                    if (valueAt < minRgbai[comp]) {
////                        minRgbai[comp] = valueAt;
////                    }
////                } else {
////                    valueAt = 0.0;
////                    set(i, j, valueAt);
////                }
////                meanRgbai[comp] += valueAt / (lines * columns);
////            }
////        }
////    }
////    PixM image = new PixM(columns, lines);
////    for (int i = 0; i < image.getColumns(); i++) {
////        for (int j = 0; j < image.getLines(); j++) {
////            double[] values = getValues(i, j);
////            double [] valuesOut = new double[3];
////            for (int k = 0; k < 3; k++) {
////                float value0 = (float) values[k];
////                valuesOut [k]=  ((get(i, j) - minRgbai[k]) / (maxRgbai[k] - minRgbai[k]));
////
////            }
////            image.setValues(i, j, valuesOut);
////        }
////    }
////    return image;
////}
////
////public PixM normalize(final double inMin, final double inMax, final double min, final double max) {
////
////    double[] maxRgbai = new double[3];
////    double[] meanRgbai = new double[3];
////    double[] minRgbai = new double[3];
////    double minA = 0.0;
////    double maxA = 1.0;
////    if (min != -1 || max != -1) {
////        minA = min;
////        maxA = max;
////    }
////    for (int i = 0; i < getCompCount(); i++) {
////        maxRgbai[i] = 255;
////        minRgbai[i] = 0;
////        //meanRgbai[i] = (inMax + inMin) / 2;
////    }
////    PixM image = new PixM(columns, lines);
////    for (int i = 0; i < image.getColumns(); i++) {
////        for (int j = 0; j < image.getLines(); j++) {
////            double[] values = getValues(i, j);
////            double [] valuesOut = new double[3];
////            for (int k = 0; k < 3; k++) {
////                float value0 = (float) values[k];
////                valuesOut [k]=  ((get(i, j) - minRgbai[k]) / (maxRgbai[k] - minRgbai[k]));
////
////            }
////            image.setValues(i, j, valuesOut);
////        }
////    }
////    return image;
////}
////
////public PixM subSampling(double div) {
////    double columns2 = 1.0 * columns / div;
////    double lines2 = 1.0 * lines / div;
////    double cli2 = 1.0 * 1 / div;
////    PixM pixM = new PixM((int) (columns2), ((int) lines2));
////    for (int c = 0; c < getCompCount(); c++) {
////        setCompNo(c);
////        pixM.setCompNo(c);
////        for (int i = 0; i < (int) columns2; i++)
////            for (int j = 0; j < (int) lines2; j++) {
////                double m = mean((int) (i * div), (int) (j * div), (int) (cli2 * div),
////                        (int) (cli2 * div));
////                pixM.set(i, j, m);
////            }
////    }
////    return pixM;
////}
////
////public double mean(int i, int j, int w, int h) {
////    double m = 0.0;
////    int p = 0;
////    for (int a = i; a < i + w; a++)
////        for (int b = j; b < j + h; b++) {
////            m += get(a, b);
////            p++;
////        }
////    return m / p;
////}
////
////
////public PixM copy() {
////
////
////    PixM pixM = new PixM(columns, lines);
////
////    for (int i = 0; i < columns; i++) {
////        for (int j = 0; j < lines; j++) {
////            pixM.set(index(i, j), getInt(i, j));
////
////        }
////
////    }
////
////    return pixM;
////}
////
////private int get(int i) {
////    int j = i % columns;
////    return index(i, j);
////}
////
////public double distance(ParametricCurve curve, Point3D p) {
////    double dI, dist = 10000;
////    double j = -1;
////    for (int i = 0; i < MAX_DISTANCE_ITERATIONS; i++)
////        if ((dI = Point3D.distance(curve.calculerPoint3D(1.0 / i), p)) < dist) {
////            dist = dI;
////            j = 1. / i;
////        }
////    return j;
////}
////
////public double distance(PixM p2) {
////    double d = 0.0;
////
////
////    double div = 1.0;
////    double columns2 = 1.0 * columns / div;
////    double lines2 = 1.0 * lines / div;
////    double cli2 = 1.0 * 1 / div;
////    PixM pixM = new PixM((int) (columns2), ((int) lines2));
////    for (int c = 0; c < getCompCount(); c++) {
////        setCompNo(c);
////        pixM.setCompNo(c);
////        for (int i = 0; i < (int) columns2; i++)
////            for (int j = 0; j < (int) lines2; j++) {
////                double m = mean((int) (i * div), (int) (j * div), (int) (cli2 * div),
////                        (int) (cli2 * div));
////                double m2 = p2.mean((int) (i * div), (int) (j * div), (int) (cli2 * div),
////                        (int) (cli2 * div));
////                d += Math.abs(m - m2);
////            }
////    }
////    return d / columns / lines;
////}
////
////
////public void colorsRegion(int x, int y, int w, int h, double[] comps) {
////    for (int i = x; i < x + w; i++)
////        for (int j = y; j < y + h; j++)
////            for (int c = 0; c < comps.length; c++) {
////                setCompNo(c);
////                set(i, j, comps[c]);
////            }
////}
////
////public PixM getColorsRegion(int x, int y, int w, int h, int sizeX, int sizeY) {
////    PixM subimage = new PixM(sizeX, sizeY);
////    for (int i = x; i < x + w; i++)
////        for (int j = y; j < y + h; j++)
////            for (int c = 0; c < getCompCount(); c++) {
////                setCompNo(c);
////                subimage.setCompNo(c);
////                double v = get(i, j);
////                subimage.set((int) (1.0 * (x + w - i) / w * subimage.getColumns()
////                ), (int) (1.0 * (y + h - j) / h * subimage.getLines()), v);
////                set(i, j, v);
////            }
////    return subimage;
////}
////
////public void colorsRegion(int x, int y, int w, int h, PixM subimage, int subImageCopyMode) {
////    for (int i = x; i < x + w; i++)
////        for (int j = y; j < y + h; j++)
////            for (int c = 0; c < getCompCount(); c++) {
////                setCompNo(c);
////                subimage.setCompNo(c);
////                double v = subimage.get((int) (1.0 * (x + w - i) / w * subimage.getColumns()
////                ), (int) (1.0 * (y + h - j) / h * subimage.getLines()));
////                set(i, j, v);
////            }
////}
////
////public PixM pasteSubImage(int x, int y, int w, int h) {
////    PixM p2 = new PixM(w, h);
////    for (int i = x; i < x + w; i++)
////        for (int j = y; j < y + h; j++)
////            for (int c = 0; c < getCompCount(); c++) {
////                setCompNo(c);
////                p2.setCompNo(c);
////                double v = get(i, j);
////                set(i - x, j - y, v);
////            }
////    return p2;
////}
////
////public PixM copySubImage(int x, int y, int w, int h) {
////    PixM p2 = new PixM(w, h);
////    for (int i = x; i <= x + w; i++)
////        for (int j = y; j <= y + h; j++)
////            for (int c = 0; c < getCompCount(); c++) {
////                setCompNo(c);
////                p2.setCompNo(c);
////                p2.set(i - x, j - y, get(i, j));
////            }
////    return p2;
////}
////
/////*
//// * Default paste: mask comp = alpha value for chanel
//// * @param x ordX in original space
//// * @param y ordY in original space
//// * @param w width in original space
//// * @param h height in original space
//// * @param subimage image to paste
//// * @param addMask transparency mask for components: 0->original pixel 1->paste pixel
//// */
////public void colorsRegionWithMask(int x, int y, int w, int h, PixM subimage, PixM addMask) {
////    for (int i = x; i < x + w; i++)
////        for (int j = y; j < y + h; j++)
////            for (int c = 0; c < getCompCount(); c++) {
////                setCompNo(c);
////                subimage.setCompNo(c);
////                addMask.setCompNo(c);
////                double compOrigi = get(i, j);
////                double compPaste = subimage.get((int) (1.0 * (i - x) / w * subimage.getColumns()
////                ), (int) (1.0 * (j - y) / h * subimage.getLines()));
////                double compMask = addMask.get((int) (1.0 * (i - x) / w * subimage.getColumns()
////                ), (int) (1.0 * (j - y) / h * subimage.getLines()));
////                double v = compOrigi * (1 - compMask) + compPaste * compMask;
////                set(i, j, v);
////            }
////}
////
////public double luminance(int x, int y) {
////    double l = 0.0;
////    setCompNo(0);
////    l += 0.2126 * get(x, y);
////    setCompNo(1);
////
////    l += 0.7152 * get(x, y);
////    setCompNo(2);
////    //l += 0.0722 * get(x, y);
////    l += 0.722 * get(x, y);
////
////    return l;
////}
////
////public double norme(int x, int y) {
////    double l = 0.0;
////    setCompNo(0);
////    l += get(x, y);
////    setCompNo(1);
////
////    l += get(x, y);
////    setCompNo(2);
////    l += get(x, y);
////
////    return l;
////}
////
////public int getColumns() {
////    return columns;
////}
////
////public int getLines() {
////    return lines;
////}
////
////public void paintAll(double[] doubles) {
////    for (int i = 0; i < getColumns(); i++)
////        for (int j = 0; j < getLines(); j++)
////            for (int c = 0; c < 3; c++) {
////                setCompNo(c);
////                set(i, j, doubles[c]);
////            }
////
////}
////
////public PixM replaceColor(double[] doubles, double[] doubles1, double delta) {
////    for (int i = 0; i < getColumns(); i++)
////        for (int j = 0; j < getLines(); j++) {
////            double[] values = getValues(i, j);
////            int k = 0;
////            for (int c = 0; c < 3; c++) {
////                if (doubles[c] - delta < values[c] && doubles[c] + delta > values[c])
////                    k++;
////            }
////
////            if (k == 3)
////                setValues(i, j, doubles1);
////
////        }
////
////
////    return this;
////}
////
////public void pasteSubImage(PixM copySubImage, int i, int j, int w, int h) {
////    for (int x = i; x < i + w; x++) {
////        for (int y = j; y < j + h; y++) {
////            for (int c = 0; c < 3; c++) {
////                int x0 = (int) (1.0 * (x - i) / w * copySubImage.getColumns());
////                int y0 = (int) (1.0 * (y - j) / h * copySubImage.getLines());
////                setCompNo(c);
////                copySubImage.setCompNo(c);
////                set(x, y, copySubImage.get(x0, y0));
////            }
////        }
////
////    }
////}
////
////public double difference(PixM other, double precision) {
////    if (precision == 0.0) {
////        precision = Math.max(Math.max(this.getColumns()
////                        , other.getColumns()
////                ),
////                Math.max(this.getLines(), other.getLines()));
////    }
////    double diff = 0.0;
////    for (double x = 0; x < precision; x += 1.) {
////        for (double y = 0; y < precision; y += 1.) {
////            for (int c = 0; c < 3; c++) {
////                int i1 = (int) (1. / precision * this.getColumns()
////                );
////                int j1 = (int) (1. / precision * this.getLines());
////                int i2 = (int) (1. / precision * other.getColumns()
////                );
////                int j2 = (int) (1. / precision * other.getLines());
////                other.setCompNo(c);
////                diff += Math.abs(get(i1, j1) - other.get(i2, j2));
////            }
////        }
////    }
////    return diff / (this.getColumns()
////            * this.getLines() * other.getColumns()
////            * other.getLines());
////}
