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

package one.empty3.apps.feature.tryocr;

import one.empty3.apps.feature.shape.Rectangle;

public class Rectangle2 {
    private int x, y, w, h;

    public Rectangle2(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Rectangle2(Rectangle rectangle) {
        this.x = (int) rectangle.getX();
        this.y = (int) rectangle.getY();
        this.w = (int) rectangle.getWidth();
        this.h = (int) rectangle.getHeight();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public boolean isValid() {
        return getX() >= 0 && getW() >= 0 &&
                getY() >= 0 && getH() >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rectangle2 that = (Rectangle2) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (w != that.w) return false;
        return h == that.h;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + w;
        result = 31 * result + h;
        return result;
    }

    public boolean includes(Rectangle2 rectangle) {
        int x2this = x + w - 1;
        int y2this = y + h - 1;
        int x2that = rectangle.y + rectangle.w - 1;
        int y2that = rectangle.y + rectangle.h - 1;

        if (x <= rectangle.x && y <= rectangle.y && x2this >= x2that && y2this >= x2that) {
            return true;
        }
        return false;
    }
}
