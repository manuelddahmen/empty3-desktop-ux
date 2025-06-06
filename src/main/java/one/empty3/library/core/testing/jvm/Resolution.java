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

package one.empty3.library.core.testing.jvm;

/*__
 * @author Se7en
 */
public class Resolution {
    public static final Resolution VGARESOLUTION = new Resolution(640, 480);
    public static final Resolution XVGARESOLUTION = new Resolution(1280, 764);
    public static final Resolution HD1080RESOLUTION = new Resolution(1920, 1080);
    public static final Resolution K4RESOLUTION = new Resolution(4096, 2160);
    protected int x;
    protected int y;

    public Resolution(int xv, int yv) {
        this.x = xv;
        this.y = yv;

    }

    public void x(int v) {
        this.x = v;
    }

    public void y(int v) {
        this.y = v;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Resolution))
            return false;
        return x==((Resolution)obj).x&&y==((Resolution)obj).y;
    }
}
