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

package one.empty3.apps.newboardgames;

import one.empty3.library.*;

import java.util.List;


public abstract class Board extends Representable {


    protected int dimX;
    protected int dimY;
    protected int heightView;
    protected int medianDistView;
    protected Camera camera;

    public Camera camera() {
        return camera;
    }

    public abstract Point2D getSize2D();

    public abstract Point3D getSize3D();

    protected abstract List<Character> getCharacters();

    public Representable cellAt(int i, int j) {
        return new Cell(this, i, j);
    }
}
