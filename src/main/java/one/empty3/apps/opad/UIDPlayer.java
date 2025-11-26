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

package one.empty3.apps.opad;

import java.net.URL;
import java.util.Date;

/*__
 * Created by manuel on 19-05-17.
 */
public class UIDPlayer {
    private URL server;
    private String playerName; // Unique on @member server
    private String gameName; // Unique on @member server
    private Date gameStart; // Unique on @member server
    private long id = 0 ;

    public String generateUIDFromFields() {
        return server.toString()+"|"+playerName+"|"+gameName+"|"+gameStart.toInstant().toString()+"|"+id;
    }
}
