/*
 *
 *  *
 *  *  * Copyright (c) 2025. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *  *
 *
 *
 *
 *  * Created by $user $date
 *
 *
 */

package one.empty3.apps.facedetect2;


import one.empty3.library.*;
import one.empty3.library.core.tribase.Plan3D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class FrameEncoding {
    private final TextureMorphMoveImageAimagesB texture;
    private EncodeVideo encodeVideo;
    private BufferedImage frame1;
    private BufferedImage frame2;
    private HashMap<String, Point3D> importA;
    private HashMap<String, Point3D> importB;
    private File out;

    public FrameEncoding(EncodeVideo encodeVideo, BufferedImage frame1, BufferedImage frame2, HashMap<String, Point3D> importA,
                         HashMap<String, Point3D> importB, File out, TextureMorphMoveImageAimagesB texture) {
        this.encodeVideo = encodeVideo;
        this.frame1 = frame1;
        this.frame2 = frame2;
        this.importA = importA;
        this.importB = importB;
        this.out = out;
        this.texture = texture;
    }

    public BufferedImage execute() {
        //TextureMorphMoveImageAimagesB textureMorphMoveImageAimagesB = new TextureMorphMoveImageAimagesB(frame1, frame2,
        //        DistanceApproxLinear.class, new Resolution(frame2.getWidth(), frame2.getHeight()), importA.values().stream().toList(), importB.values().stream().toList());
        BufferedImage bufferedImage = new BufferedImage(frame2.getWidth(), frame2.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        ZBufferImpl instance = ZBufferFactory.instance(frame2.getWidth(), frame2.getHeight());
        instance.setDisplayType(ZBufferImpl.SURFACE_DISPLAY_TEXT_QUADS);
        Plan3D plane = new Plan3D(Point3D.O0, Point3D.X.mult(frame2.getWidth()), Point3D.Y.mult(frame2.getHeight()));

        Point3D center = plane.getOrig().plus(plane.getvX().getElem().mult(0.5)).plus(plane.getvY().getElem()).mult(0.5);
        Camera camera = new Camera(center.plus(Point3D.Z.mult(-Math.max(frame2.getHeight(), frame2.getWidth()) / 2.0)).mult(1.0), center, Point3D.Y);

        plane.texture(texture);

        Scene scene = new Scene();

        scene.add(plane);
        scene.cameraActive(camera);
        instance.scene(scene);
        scene.cameraActive(camera);

        instance.draw();

        return instance.image2();
    }


}
