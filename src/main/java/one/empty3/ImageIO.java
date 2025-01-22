package one.empty3;


import one.empty3.libs.commons.IImageMp;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import one.empty3.libs.Image;

public class ImageIO {

    public static @NotNull Image read(@NotNull File in) {
        Image i = new Image(null);
        i.loadFile(in);
        return i;
    }


    public static void write(@NotNull Image bitmap, String jpg, File out) {
        if (!out.getParentFile().exists())
            out.getParentFile().mkdirs();

        new Image(bitmap).saveFile(out);
    }

    public static void write(@NotNull Image bitmap, String jpg, File out, boolean shouldOverwrite) {
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
            new Image(bitmap).saveFile(out);
        }
    }
}
