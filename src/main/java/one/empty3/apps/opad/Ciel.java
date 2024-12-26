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

/*__
Global license : 

 CC Attribution

 author Manuel Dahmen _manuel.dahmen@gmx.com_

 ***/


package one.empty3.apps.opad;

import one.empty3.library.*;

import javax.imageio.ImageIO;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public class Ciel {
    private final Sphere bleu;

    public Ciel() {
        bleu = new Sphere(new Point3D(0.5, 0.5, 0.0), 2);

        try {
            URL resource = getClass().getResource("one.empty3.library/apps/darz/marssurface.jpg");
            Logger.getAnonymousLogger().log(Level.INFO, resource.toExternalForm());
            BufferedImage read = ImageIO.read(resource);
            Image ecBufferedImage = new Image(read);
            bleu.texture(new ImageTexture(ecBufferedImage));

        } catch (Exception ex) {
            bleu.texture(new ColorTexture(new Color(Color.BLUE)));
        }
    }

    public Sphere getBleu() {
        return bleu;
    }


}
