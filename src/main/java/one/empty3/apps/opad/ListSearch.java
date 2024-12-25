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
/*__

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

 * Created by manuel on 19-05-17.
 */
/*
public class ListSearch<E> extends ArrayList<E> {
    public E search(String fieldOrGetter, Object value) throws IllegalAccessException {
        Iterator<E> it = this.iterator();

        boolean found = false;

        while(it.hasNext() && found==false) {
            E element = it.next();

            Field f = null;
            Object invoke = null;
            try {
                f = element.getClass().getField(fieldOrGetter);
            } catch (NoSuchFieldException e) {
                try {
                    Method m = element.getClass().getMethod(fieldOrGetter);
                    try {
                        invoke = m.invoke(element, (Object)null);
                    } catch (InvocationTargetException e1) {
                        e1.printStackTrace();
                    }
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                }
            }

            if(value.equals(f.get(element)) || value.equals(invoke)) {
                return element;
            }


        }

        return null;
    }
}
*/