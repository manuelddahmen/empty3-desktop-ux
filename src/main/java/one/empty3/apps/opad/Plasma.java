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

import one.empty3.library.Point2D;

import one.empty3.library.Point;
import one.empty3.libs.*;

import java.awt.Dimension;

public class Plasma {
    public static double scale;
    public static double t_factor = 0.01;

    public static double f(double x, double y, double t) {
        return Math.sin(
                new Point2D(x, y).
                        distance(
                                new Point2D(
                                        (Math.sin(-t * t_factor)),
                                        (Math.cos(-t * t_factor)))
                        ) / scale
        );

    }

    public static Color color(double x, double y, double t) {
        return new Color(new java.awt.Color(
                (float) ((Math.cos(Math.PI * f(x, y, t) + 0.5 + t * t_factor) + 1) / 2),
                (float) ((Math.cos(Math.PI * f(x, y, t) + 1.0 + t * t_factor + Math.PI / 2) + 1) / 2),
                (float) ((Math.cos(Math.PI * f(x, y, t) + 1.5 + t * t_factor + Math.PI) + 1) / 2)
        ));
    }
}
