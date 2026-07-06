/*
 *
 *  *
 *  *  * Copyright (c) 2026. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2026 Manuel Daniel Dahmen
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

package one.empty3.apps.facedetect3;

import one.empty3.library.Point3D;

public class PointMatch implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Point3D leftPoint;
    private Point3D rightPoint;
    private String name;

    public PointMatch(Point3D leftPoint, Point3D rightPoint, String name) {
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;
        this.name = name;
    }

    public Point3D getLeftPoint() {
        return leftPoint;
    }

    public void setLeftPoint(Point3D leftPoint) {
        this.leftPoint = leftPoint;
    }

    public Point3D getRightPoint() {
        return rightPoint;
    }

    public void setRightPoint(Point3D rightPoint) {
        this.rightPoint = rightPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}