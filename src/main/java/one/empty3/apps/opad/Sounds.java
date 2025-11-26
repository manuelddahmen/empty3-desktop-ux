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

package one.empty3.apps.opad;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class Sounds {

    public static synchronized void playSound(String resourcePath) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    // Essayez d'abord de charger en tant que ressource de classe
                    InputStream myStream = Sounds.class.getResourceAsStream(resourcePath);
                    
                    // Si null, essayez avec le classloader
                    if (myStream == null) {
                        myStream = Sounds.class.getClassLoader().getResourceAsStream(resourcePath);
                    }
                    
                    // Si toujours null, essayez comme fichier local
                    if (myStream == null) {
                        File file = new File(resourcePath);
                        if (file.exists()) {
                            myStream = new BufferedInputStream(new FileInputStream(file));
                        } else {
                            System.err.println("Fichier audio introuvable: " + resourcePath);
                            return;
                        }
                    }
                    
                    // À ce stade, myStream ne devrait pas être null
                    AudioInputStream audio2 = AudioSystem.getAudioInputStream(myStream);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audio2);
                    clip.start();
                } catch (Exception e) {
                    System.err.println("Erreur lors de la lecture du son '" + resourcePath + "': " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static synchronized void playSoundBonusHit() {
        playSound("resources/sounds/bonusHit.wav");
    }

    public static synchronized void playMusic() {
        playSound("resources/sounds/musicIntro.wav");
    }
}
