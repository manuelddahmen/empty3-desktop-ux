package one.empty3.library.core;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class ECBufferedImage extends BufferedImage {

    public ECBufferedImage(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public ECBufferedImage(int width, int height, int imageType, IndexColorModel cm) {
        super(width, height, imageType, cm);
    }

    public ECBufferedImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
        super(cm, raster, isRasterPremultiplied, properties);
    }
}
