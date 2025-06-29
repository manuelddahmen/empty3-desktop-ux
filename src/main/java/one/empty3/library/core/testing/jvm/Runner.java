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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/*__
 * Created by manue on 08-02-20.
 */
public class Runner {
    private List<TestObjetUx> testObjetList;
    private HashMap<String, List<Object>> defaultProperties;
    private HashMap<TestObjetUx, HashMap<String, List<Object>>> properties;

    public Runner() {
        this.testObjetList = new ArrayList<>();
        defaultProperties = new HashMap<>();
    }


    public Runner queue(TestObjetUx testObjet) {
        testObjetList.add(testObjet);
        return this;
    }

    public Runner queue(Class<? extends TestObjetUx> testObjetClass) {
        try {
            TestObjetUx TtestObjetUx = testObjetClass.newInstance();
            this.testObjetList.add(TtestObjetUx);
            return this;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Runner setDefaultProperty(String setterName, Object... values) {
        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(Arrays.asList(values));
        this.defaultProperties.put(setterName, objects);
        return this;
    }

    public Runner setProperty(TestObjetUx testObjet, String setterName, Object... values) {

        ArrayList<Object> objects = new ArrayList<>();
        objects.addAll(Arrays.asList(values));
        HashMap<String, List<Object>> stringListObjectHashMap;
        if (properties.containsKey(testObjet)) {
            stringListObjectHashMap = properties.get(testObjet);
        } else {
            stringListObjectHashMap = new HashMap<>();
            properties.put(testObjet, stringListObjectHashMap);
        }
        stringListObjectHashMap.put(setterName, objects);
        this.properties.put(testObjet, stringListObjectHashMap);
        return this;
    }

    private Runner activateproperty() {
        testObjetList.forEach(TestObjetUx -> {
            for (int i = 0; i < 2; i++) {
                HashMap<String, List<Object>> p01 = i == 0 ? defaultProperties : properties.get(TestObjetUx);
                if (p01 == null)
                    continue;
                p01.forEach((s, objects) -> {
                    Class[] parameters = new Class[defaultProperties.get(TestObjetUx).size()];
                    int i1 = 0;
                    for (Object o : defaultProperties.get(TestObjetUx)) {
                        parameters[i1] = o.getClass();
                    }
                    try {
                        Method method = TestObjetUx.getClass().getMethod(s, parameters);
                        method.invoke(TestObjetUx, defaultProperties.get(TestObjetUx));
                    } catch (NoSuchMethodException e) {

                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });


            }
        });
        return this;
    }

    public Runner run() {
        activateproperty();
        testObjetList.forEach(TestObjetUx -> new Thread(TestObjetUx).start()
        );
        return this;
    }
}
