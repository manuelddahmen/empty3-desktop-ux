/*
 *
 *  *
 *  *  * Copyright (c) 2026. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2026 Manuel Daniel Dahmen
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

package one.empty3.apps.opad.server;

import java.util.ResourceBundle;

/*__
 * The four bonus families of the single player game, with the counts, point values
 * and colours that {@code one.empty3.apps.opad.Bonus} hard-codes in its constructor.
 *
 * <p>Reading them from the same {@code Bundle.properties} keeps the multiplayer map
 * identical to the single player one, and lets the server be tuned without touching
 * the code.</p>
 *
 * @author Manuel Dahmen dathewolf@gmail.com
 */
public enum BonusKind {

    /*__ The plain red bonus, {@code SimpleBonus}. */
    SIMPLE("bonus.size", "bonus.point", 0xFF0000),
    /*__ {@code Cheval_Licorne}, the jackpot. */
    LICORNE("licorne.size", "licorne.point", 0x0000FF),
    /*__ {@code Escargot}, worth negative points. */
    ESCARGOT("escargot.size", "escargot.point", 0x000000),
    /*__ {@code MouvementDirectionnel}, worth nothing. */
    FUITE("fuite.size", "fuite.point", 0x808080);

    /*__ Sphere radius shared by every bonus, from {@code bonus.scale}. */
    public static final String SCALE_KEY = "bonus.scale";
    /*__ Height above the ground, as used by {@code Bonus.random()}. */
    public static final double HEIGHT = 0.005d;

    private static final String BUNDLE = "one.empty3.apps.opad.Bundle";

    private final String countKey;
    private final String pointKey;
    private final int colorRgb;

    BonusKind(String countKey, String pointKey, int colorRgb) {
        this.countKey = countKey;
        this.pointKey = pointKey;
        this.colorRgb = colorRgb;
    }

    private static ResourceBundle bundle() {
        return ResourceBundle.getBundle(BUNDLE);
    }

    /*__ Sphere radius shared by every bonus. */
    public static double scale() {
        return Double.parseDouble(bundle().getString(SCALE_KEY));
    }

    /*__
     * @param name a constant name, as carried by {@link BonusState#kind}
     * @return the matching kind, or {@link #SIMPLE} for an unknown or missing name,
     * so that an unexpected server never leaves the client without a bonus to draw
     */
    public static BonusKind of(String name) {
        if (name != null) {
            for (BonusKind kind : values()) {
                if (kind.name().equalsIgnoreCase(name)) {
                    return kind;
                }
            }
        }
        return SIMPLE;
    }

    /*__ How many bonuses of this kind a map holds. */
    public int count() {
        return Integer.parseInt(bundle().getString(countKey));
    }

    /*__ Points awarded when the bonus is taken. */
    public double points() {
        return Double.parseDouble(bundle().getString(pointKey));
    }

    /*__ Sphere colour, packed as {@code 0xRRGGBB}. */
    public int colorRgb() {
        return colorRgb;
    }
}
