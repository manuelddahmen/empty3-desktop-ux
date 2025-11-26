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

package one.empty3.apps.opad.models;

import one.empty3.library.LineSegment;
import one.empty3.library.Point3D;

/*__
 * Created by Win on 17-10-18.
 */
public class HumanoidModel extends Model {
    private long timeStart;
    private long animationTime = 0;
    private Point3D position;
    private Point3D vectorVectical;
    private double feetSize, legSize,
        armSize, bodySize, headSize
        ;


    public HumanoidModel()
    {
        timeStart = System.nanoTime();
    }

    private long realTimeNano()
    {
        return System.nanoTime()
                - timeStart; // TODO
    }
    private long animationTimeNano(long timeEllapsed)
    {
        animationTime += timeEllapsed;
        return animationTime;
    }
    public class Arete
    {
        LineSegment segmentDroite;

    }
    public class BodyPart
    {

    }

    public class BodyLink
    {

    }
    public class Movement
    {

    }

}
