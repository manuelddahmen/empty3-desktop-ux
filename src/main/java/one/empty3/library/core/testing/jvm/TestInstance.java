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

/*__
 * *
 * Global license : * Microsoft Public Licence
 * <p>
 * author Manuel Dahmen _manuel.dahmen@gmx.com_
 * <p>
 * *
 */
package one.empty3.library.core.testing.jvm;

import java.util.ArrayList;

/*__
 * Meta Description missing
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public abstract class TestInstance {

    protected TestObjetUx test;

    public abstract Parameter getDynParameter(String name);

    public abstract ArrayList<Parameter> getDynParameters();

    public abstract ArrayList<Parameter> getInitParameters();

    public abstract boolean newInstance(ArrayList<Parameter> parameter);

    public abstract boolean setDynParameter(Parameter parameter);

    public void theTest(TestObjetUx test) {
        this.test = test;
    }

    public class Parameter {

        public String name;
        public Class zclass;
        public Object value;
    }

}
