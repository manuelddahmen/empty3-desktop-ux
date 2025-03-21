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

package one.empty3.apps.feature;

import one.empty3.feature.*;


import one.empty3.apps.feature.DataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Cluster {

    public List<DataPoint> points;
    public DataPoint centroid;
    public int id;

    public Cluster(int id) {
        this.id = id;
        this.points = new ArrayList<DataPoint>();
        this.centroid = null;
    }

    public List<DataPoint> getPoints() {
        return points;
    }

    public void addPoint(DataPoint point) {
        points.add(point);
        point.setCluster(id);
    }

    public void setPoints(List points) {
        this.points = points;
    }

    public DataPoint getCentroid() {
        return centroid;
    }

    public void setCentroid(DataPoint centroid) {
        this.centroid = centroid;
    }

    public int getId() {
        return id;
    }

    public void clear() {
        points.clear();
    }

    public void plotCluster() {
        Logger.getAnonymousLogger().log(Level.INFO, "[Cluster: " + id + "]");
        Logger.getAnonymousLogger().log(Level.INFO, "[Centroid: " + centroid + "]");
        Logger.getAnonymousLogger().log(Level.INFO, "[Points: \n");
        for (DataPoint p : points) {
            Logger.getAnonymousLogger().log(Level.INFO, "" + p);
        }
        Logger.getAnonymousLogger().log(Level.INFO, "]");
    }

}