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

package one.empty3.apps.feature.jviolajones;


import one.empty3.apps.feature.jviolajones.Rect;
import one.empty3.apps.feature.jviolajones.Tree;

import one.empty3.library.Point;
import one.empty3.libs.*;
import java.awt.Dimension;

public class Feature {

    one.empty3.apps.feature.jviolajones.Rect[] rects;
    int nb_rects;
    float threshold;
    float left_val;
    float right_val;
    Point size;
    int left_node;
    int right_node;
    boolean has_left_val;
    boolean has_right_val;

    public Feature(float threshold, float left_val, int left_node, boolean has_left_val,
                   float right_val, int right_node, boolean has_right_val, Point size) {
        nb_rects = 0;
        rects = new one.empty3.apps.feature.jviolajones.Rect[3];
        this.threshold = threshold;
        this.left_val = left_val;
        this.left_node = left_node;
        this.has_left_val = has_left_val;
        this.right_val = right_val;
        this.right_node = right_node;
        this.has_right_val = has_right_val;
        this.size = size;
    }

    public int getLeftOrRight(int[][] grayImage, int[][] squares, int i, int j, float scale) {
        int w = (int) (scale * size.x);
        int h = (int) (scale * size.y);
        double inv_area = 1. / (w * h);
        //Logger.getAnonymousLogger().log(Level.INFO, "w2 : "+w2);
        int total_x = grayImage[i + w][j + h] + grayImage[i][j] - grayImage[i][j + h] - grayImage[i + w][j];
        int total_x2 = squares[i + w][j + h] + squares[i][j] - squares[i][j + h] - squares[i + w][j];
        double moy = total_x * inv_area;
        double vnorm = total_x2 * inv_area - moy * moy;
        vnorm = (vnorm > 1) ? Math.sqrt(vnorm) : 1;

        int rect_sum = 0;
        for (int k = 0; k < nb_rects; k++) {
            one.empty3.apps.feature.jviolajones.Rect r = rects[k];
            int rx1 = i + (int) (scale * r.x1);
            int rx2 = i + (int) (scale * (r.x1 + r.y1));
            int ry1 = j + (int) (scale * r.x2);
            int ry2 = j + (int) (scale * (r.x2 + r.y2));
            //Logger.getAnonymousLogger().log(Level.INFO, (rx2-rx1)*(ry2-ry1)+" "+r.weight);
            rect_sum += (int) ((grayImage[rx2][ry2] - grayImage[rx1][ry2] - grayImage[rx2][ry1] + grayImage[rx1][ry1]) * r.weight);
        }
        //Logger.getAnonymousLogger().log(Level.INFO, rect_sum);
        double rect_sum2 = rect_sum * inv_area;

        //Logger.getAnonymousLogger().log(Level.INFO, rect_sum2+" "+threshold*vnorm);
        return (rect_sum2 < threshold * vnorm) ? one.empty3.apps.feature.jviolajones.Tree.LEFT : Tree.RIGHT;

    }

    public void add(Rect r) {
        rects[nb_rects++] = r;
    }
}
