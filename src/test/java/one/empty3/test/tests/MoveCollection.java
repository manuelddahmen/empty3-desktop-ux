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

package one.empty3.test.tests;

import java.util.Collection;
import java.util.HashMap;

public class MoveCollection {
    private HashMap<String, Move> moves;
    private HashMap<String,Double> tStart = new HashMap<>();
    private HashMap<String,Double> tEnd = new HashMap<>();
    public MoveCollection(double tStart, double tEnd) {
        this.moves = new HashMap<>();
    }

    public void addAll(String humanWalks, double tStart, double tEnd,
                       Move... movesAdd) {
        for (Move move : movesAdd) {
            moves.put(move.getMoveName(), move);
            this.tStart.put(humanWalks, tStart);
            this.tEnd.put(humanWalks, tEnd);
        }
    }

    public Collection<Move> getMoves() {
        return moves.values();
    }
}
