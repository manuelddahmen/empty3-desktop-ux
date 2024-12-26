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

import one.empty3.library.Cube;
import one.empty3.library.Point2D;
import one.empty3.library.Point3D;

/*__
 *
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public interface PositionUpdate extends Runnable {

    void update();

    void
    acc(long timeMillis);

    void dec(long timeMillis);

    boolean plot(Point3D position, Point3D deplacement, Cube plottee);

    int state();


    void rotationGauche(long timeMillis);

    void rotationDroite(long timeMillis);

    void testCollision(PositionMobile positionMobile);

    void ennemi(Bonus e);

    int score();

    boolean estGagnant();

    Point3D calcCposition();

    Point3D calcDirection();

    Point3D calculerPositionAuSol(Point2D position2D);

    Point3D calculerPositionModule(Point2D position2D);


    int STATE_GAME_IN_PROGRESS();

    Circuit getCircuit();

    double energy();

    Terrain getTerrain();

    void setTerrain(Terrain t);

    double getAngle();

    Point2D directionY();

    void moveUp(long timeKeyPress);

    void moveDown(long timeKeyPress);

    Plotter3D getPlotter3D();

    void setPlotter3D(Plotter3D plotter3D);

    PositionMobile getPositionMobile();

    Point3D getVecDir2D();

    Path getPath();

    void setPath(Path path);

    void setMain(DarkFortressGUI darkFortressGUI);

    DarkFortressGUI getMain();
}
