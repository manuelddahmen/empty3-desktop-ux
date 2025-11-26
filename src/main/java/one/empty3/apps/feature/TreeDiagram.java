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

package one.empty3.apps.feature;

import one.empty3.feature.*;


import one.empty3.apps.feature.ClassSchemaBuilder;
import one.empty3.apps.feature.TreeNodeDiagram;
import one.empty3.apps.feature.TreeNodeListener;

import java.util.ArrayList;
import java.util.List;

public class TreeDiagram implements TreeNodeListener {
    protected TreeNodeDiagram head = null;
    private TreeNodeListener treeNodeListener;


    /**
     * Constructs tree of element, attached files and nodes' childre
     * from head (last process, output=resulting image) to upper
     * processes.
     *
     * @param diagramElements COPY of the diagram element List.
     */
    public TreeDiagram(List<ClassSchemaBuilder.DiagramElement> diagramElements) {
        constructTreeReverseProcesses(diagramElements);
    }

    public void constructTreeReverseProcesses(List<ClassSchemaBuilder.DiagramElement> diagramElements) {
        List<ClassSchemaBuilder.DiagramElement> diagramElements2 = new ArrayList<>();

        diagramElements2.addAll(diagramElements);

        List<List<ClassSchemaBuilder.ClassElement>> heads = new ArrayList<>();

        List<ClassSchemaBuilder.DiagramElement> diagramElements1 = new ArrayList<>();

        for (ClassSchemaBuilder.DiagramElement classElement : diagramElements2) {
            if (classElement instanceof ClassSchemaBuilder.ClassElement) {
                heads.add(new ArrayList<>());
                heads.get(heads.size() - 1).add((ClassSchemaBuilder.ClassElement) classElement);
            }
            diagramElements1.add(classElement);
        }

        head = null;

        for (int i = 0; i < heads.size(); i++) {
            ClassSchemaBuilder.ClassElement classElement = (ClassSchemaBuilder.ClassElement) (heads.get(i).get(0));
            if (classElement != null) {
                if (classElement.partAfter.element == null || classElement.partAfter.element.equals(null)) {
                    head = new TreeNodeDiagram();
                    head.setElement(classElement);
                    diagramElements1.remove(head.getElement());
                }
            }
        }

        addToNode(diagramElements1, head, new ArrayList<>());
        // Noeuds à tête children>=1
    }

    private void addToNode(List<ClassSchemaBuilder.DiagramElement> diagramElements1, TreeNodeDiagram current, List<ClassSchemaBuilder.ClassElement> removed) {

        if (current != null) {
            for (int i = 0; i < diagramElements1.size(); i++) {
                ClassSchemaBuilder.ClassElement ce = (ClassSchemaBuilder.ClassElement) diagramElements1.get(i);
                if (ce.partAfter.element != null) {
                    removed.add(ce);
                    TreeNodeDiagram treeNodeDiagram = new TreeNodeDiagram();
                    treeNodeDiagram.setElement(ce);
                    treeNodeDiagram.addListener(treeNodeListener);
                    diagramElements1.removeAll(removed);
                    removed.clear();
                    current.getChildren().add(treeNodeDiagram);
                    treeNodeDiagram.addToNode(diagramElements1, treeNodeDiagram, removed);
                }
            }
            diagramElements1.removeAll(removed);
        }
    }

    public void executeOneStart() {
        ClassSchemaBuilder.DiagramElement element = head.getElement();
    }

    public void run() {

        // Trouver les fichiers attachés.
        // "webcam"
        // Pictures Folder
        // Pictures
        // Picture
        // Movie

    }

    public void addListener(TreeNodeListener treeNodeListener) {
        this.treeNodeListener = treeNodeListener;
    }

    @Override
    public void listen(TreeDiagram treeDiagram, TreeNodeDiagram treeNodeDiagram, int code) {

    }

    public TreeNodeListener getTreeNodeListener() {
        return treeNodeListener;
    }

    public void setTreeNodeListener(TreeNodeListener treeNodeListener) {
        this.treeNodeListener = treeNodeListener;
    }

    @Override
    public String toString() {
        return "TreeDiagram{" +
                "head=" + head +
                ", treeNodeListener=" + treeNodeListener +
                '}';
    }
}
