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

package one.empty3.apps.feature;
import one.empty3.feature.*;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamLockException;
import one.empty3.apps.feature.ClassSchemaBuilder;
import one.empty3.apps.feature.RunEffect;
import one.empty3.apps.feature.gui.LiveEffect;
import one.empty3.feature.Motion;
import one.empty3.library.Config;

import javax.imageio.ImageIO;
import javax.swing.*;
import one.empty3.library.Point;
import one.empty3.libs.*;
import one.empty3.libs.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ThreadEffectDisplay extends Thread {
    private static ThreadEffectDisplay uniqueObject;
    public Webcam webcam;
    public Motion motion = null;
    private BufferedImage image;
    private JPanel jPanel;
    private one.empty3.apps.feature.ClassSchemaBuilder main;
    private BufferedImage imageIn;
    private BufferedImage imageMotion;
    private LiveEffect directEffect;
    private one.empty3.apps.feature.RunEffect runEffect;
    private String tempDir;
    private BufferedImage imageIn2;
    private boolean motionActive = true;
    private boolean effectActive = true;

    public ThreadEffectDisplay() {

        //ResourceBundle globalSettings = ResourceBundle.getBundle("settings.properties");
        Properties globalSettings = new Properties();
        try {
            globalSettings.load(new FileInputStream("settings.properties"));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tempDir = globalSettings.getProperty("tempDir") + File.separator;
        new File(tempDir).mkdirs();

        if (uniqueObject == null)
            uniqueObject = this;
        else {
            if (uniqueObject != this)
                System.err.println("Error duplicate class viewer (?)");
            //System.exit(-1);
        }
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    @Override
    public void run() {

        init();


        do {
            File fileOrigin = new File(new Config().getDefaultFileOutput() + File.separator + "FeaturesVideo" + File.separator + "webcam.jpg");
            new File(new Config().getDefaultFileOutput() + File.separator + "FeaturesVideo" ).mkdirs();


            main.files.clear();
            main.files.add(new File[]{fileOrigin});

            image = webcam.getImage();
            if(image!=null) {
                try {
                    ImageIO.write(image, "jpg", fileOrigin);
                } catch (IOException e) {
                    e.printStackTrace();
                    //throw new RuntimeException(e);
                }
            }
            if (image != null) {

                System.err.println("File written");

                main.setMaxRes(Math.max(image.getWidth(), image.getHeight()));

                main.buttonGOActionPerformed(null);

                while ((imageIn2 = getImageIn()) == null) {
                    /*try {
                        Thread.sleep(20);
                    } catch (InterruptedException ignored) {

                    }*/
                    main.buttonGOActionPerformed(null);
                }

                if (imageIn2 != null) {
                    jPanel.setMinimumSize(new Dimension(imageIn2.getWidth(), imageIn2.getHeight()));
                    Graphics graphics = jPanel.getGraphics();
                    graphics.drawImage(imageIn2, 0, 0, jPanel.getWidth(), jPanel.getHeight(), null);
                } else {
                    Logger.getAnonymousLogger().log(Level.INFO, "No image to display: " + imageIn2);
                }
            }
        } while (directEffect.isVisible());

    }

    private void init() {
        if (webcam != null && webcam.isOpen())
            Webcam.getDefault().close();

        webcam = Webcam.getDefault();
        while (directEffect == null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!webcam.isOpen()) {
            webcam.setViewSize(new Dimension(directEffect.viewSizes[directEffect.viewSizes.length - 1]));
            try {
                webcam.open();
            } catch (WebcamLockException exception) {
                exception.printStackTrace();
            }
        }
    }

    private boolean isMotionActive() {
        return motionActive;
    }

    public void setMotionActive(boolean b) {
        this.motionActive = b;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.imageIn = image;
    }

    public void setJpanel(JPanel panel1) {
        this.jPanel = panel1;
    }

    public void setMain(ClassSchemaBuilder main) {
        this.main = main;
    }

    public void setImageMotion(BufferedImage process) {
        imageMotion = process;
    }

    public void setDirectEffect(LiveEffect liveEffect) {
        this.directEffect = liveEffect;
    }

    public void setRunEffect(RunEffect runEffect) {
        this.runEffect = runEffect;
    }

    public synchronized BufferedImage getImageIn() {
        return imageIn;
    }

    public synchronized void setImageIn(BufferedImage read) {
        imageIn = read;
    }

    public void setEffectActive(boolean b) {
        this.effectActive = b;
    }

    public void stop1() {
        this.interrupt();
    }
}
