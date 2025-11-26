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

package one.empty3.apps.atlasgen;

import one.empty3.library.*;

/*__
 * Created by manue on 10-05-19.
 */
public class TextureOpSphere extends TextureOp2D {
    public TextureOpSphere(ITexture up) {
        setUpText(up);
    }

    @Override
    public void iterate() throws EOFVideoException {

    }

    @Override
    public void timeNext() {

    }

    @Override
    public void timeNext(long milli) {

    }

    @Override
    public StructureMatrix getDeclaredProperty(String name) {
        return upText;
    }

    @Override
    public MatrixPropertiesObject copy() throws CopyRepresentableError, IllegalAccessException, InstantiationException {
        return null;
    }

    @Override
    public int getColorAt(double u, double v) {
        return upText.getElem().getColorAt(u, 1 - v);
    }
}
