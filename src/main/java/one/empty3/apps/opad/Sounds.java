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

package one.empty3.apps.opad;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

public class Sounds {

    public static synchronized void playSound(String resourcePath) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
/*
                    BufferedInputStream myStream = new BufferedInputStream(new FileInputStream(new File(resourcePath)));
                    AudioInputStream audio2 = AudioSystem.getAudioInputStream(myStream);
                   */
                    InputStream myStream = getClass().getResourceAsStream(resourcePath);
                    AudioInputStream audio2 = AudioSystem.getAudioInputStream(myStream);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audio2);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static synchronized void playSoundBonusHit() {
        playSound("/resources/sounds/bonusHit.wav");
    }

    public static synchronized void playMusic() {
        playSound("/resources/sounds/musicIntro.wav");
    }
}
