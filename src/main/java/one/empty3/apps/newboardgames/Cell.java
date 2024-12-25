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

package one.empty3.apps.newboardgames;

import one.empty3.apps.feature.app.replace.java.awt.Color;
import one.empty3.library.*;

public class Cell extends RepresentableConteneur {
    private Board board;

    public Cell(Board board, int x, int y) {
        this.board = board;
        Point3D[] point3DS = {
                Point3D.X.mult(x).plus(Point3D.Y.mult(y)),
                Point3D.X.mult(x + 1).plus(Point3D.Y.mult(y)),
                Point3D.X.mult(x + 1).plus(Point3D.Y.mult(y + 1)),
                Point3D.X.mult(x).plus(Point3D.Y.mult(y + 1))
        };
        Polygon polygon = new Polygon(point3DS, new ColorTexture(new Color(1f / (x + 1), 1f / (y + 1), 0.5f).getRGB()));
        add(polygon);
    }

}
