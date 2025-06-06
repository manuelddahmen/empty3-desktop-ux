/*
 * Copyright (c) 2024.
 *
 *
 *  Copyright 2012-2023 Manuel Daniel Dahmen
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package one.empty3.matrix;


import one.empty3.libs.Image;

public interface InterfaceMatrix {

    public void init(int columns, int lines);
//    public void init(Bitmap bitmap);
//    public void init(PixM bitmap);


    public void setCompNo(int no);

    public int getCompNo();

    public void set(int column, int line, double values);

    public void set(int column, int line, double... values);

    public double get(int column, int line);

    public double[] getValues(int column, int line);

    Image getBitmap();
}
