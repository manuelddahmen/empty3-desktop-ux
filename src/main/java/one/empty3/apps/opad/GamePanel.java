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

import one.empty3.library.Point3D;

/*__
 * Created by Manuel Dahmen on 31-10-18.
 */
public class GamePanel {
    private Class map;

    public class PlayerOnMap {

        public PlayerOnMap(Player player, Point3D position, long score, int energy, int life, boolean isDisposable, boolean isPlaying) {
            this.player = player;
            this.position = position;
            this.score = score;
            this.energy = energy;
            this.life = life;
            this.isDisposable = isDisposable;
            this.isPlaying = isPlaying;
        }

        Player player;
        Point3D position;
        long score;
        int energy;
        int life;
        boolean isDisposable;
        boolean isPlaying;

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public Point3D getPosition() {
            return position;
        }

        public void setPosition(Point3D position) {
            this.position = position;
        }

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }

        public int getEnergy() {
            return energy;
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }

        public int getLife() {
            return life;
        }

        public void setLife(int life) {
            this.life = life;
        }

        public boolean isDisposable() {
            return isDisposable;
        }

        public void setDisposable(boolean disposable) {
            isDisposable = disposable;
        }

        public boolean isPlaying() {
            return isPlaying;
        }

        public void setPlaying(boolean playing) {
            isPlaying = playing;
        }
    }


}
