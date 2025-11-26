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
import one.empty3.library.Representable;
import one.empty3.library.core.tribase.TRISphere;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLogic {


 private double hauteur = 0.01;
 protected Point3D positionOrigine = Point3D.O0.plus(hauteur);
 protected Point3D position = positionOrigine;
 protected double positionIncrement = .01;
 protected double directionNorme = 1;
 protected Point3D directionOrigine = Point3D.Z;
 protected Point3D direction = directionOrigine;
 protected double angle = 0.0;
 private int score;
 private Bonus ennemi;
 protected static final int STATE_GAME_IN_PROGRESS = 1;
 private boolean gagne = false;

 public GameLogic() {
 }

 public void update() {
 }

 public void acc() {

 position = position.plus(direction.mult(positionIncrement));
 }

 public void dec() {
 position = position.moins(direction.mult(positionIncrement));
 }

 public int state() {
 return STATE_GAME_IN_PROGRESS;
 }

 public GameObject[] getObjects() {
 return null;
 }

 public Point3D position() {
 return position;
 }

 public Point3D direction() {
 return direction;
 }

 public void rotationGauche() {
 angle = angle + Math.PI * 2 / 360 * 10;
 direction =
 new Point3D(
 directionNorme * Math.sin(angle),
 hauteur,
 directionNorme * Math.cos(angle));
 }

 public void rotationDroite() {
 angle = angle - Math.PI * 2 / 360 * 10;
 direction =
 new Point3D(
 directionNorme * Math.sin(angle),
 hauteur,
 directionNorme * Math.cos(angle));

 }

 public void testCollision() {
 Iterator<Representable> it = ennemi.iterator();

 while (it.hasNext()) {
 Representable r = it.next();

 if (r instanceof TRISphere) {
 if (Point3D.distance(((TRISphere) r).getCentre(), position) < 0.1) {
 int points = 10;
 //Logger.getAnonymousLogger().log(Level.INFO, "POINTS" + points);

 score += points;

 Logger.getAnonymousLogger().log(Level.INFO, ""+score);

 if(ennemi.removeBonus(r))
 win();

 break;
 }
 }

 }

 }

 public void ennemi(Bonus e) {
 this.ennemi = e;
 }

 public int score() {
 return score;
 }

 private void win() {
 gagne = true;
 }

 public boolean estGagnant()
 {
 return gagne;
 }

}
