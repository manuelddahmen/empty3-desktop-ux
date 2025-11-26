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
import one.empty3.apps.feature.TreeDiagram;
import one.empty3.apps.feature.TreeNodeListener;
import one.empty3.io.ProcessFile;
import one.empty3.io.ProcessNFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeNodeDiagram implements TreeNodeListener {
    protected ClassSchemaBuilder.DiagramElement element;
    protected boolean isExecuted = false;
    protected TreeNodeDiagram parentNode;
    protected File file;
    List<TreeNodeDiagram> children;
    private TreeNodeListener treeNodeListener;
    protected boolean executeIfExecuted = false;

    public TreeNodeDiagram() {
        children = new ArrayList<>();
    }

    public List<TreeNodeDiagram> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeDiagram> children) {
        this.children = children;
    }

    public ClassSchemaBuilder.DiagramElement getElement() {
        return element;
    }

    public void setElement(ClassSchemaBuilder.DiagramElement element) {
        this.element = element;
    }

    public void addToNode(List<ClassSchemaBuilder.DiagramElement> diagramElements1,
                          TreeNodeDiagram current, List<ClassSchemaBuilder.ClassElement> removed) {
        if (treeNodeListener == null)
            setTreeNodeListener(this);
        if (current != null) {
            for (int i = 0; i < diagramElements1.size(); i++) {
                ClassSchemaBuilder.ClassElement ce = (ClassSchemaBuilder.ClassElement) diagramElements1.get(i);
                if (ce.partAfter.element != null) {
                    removed.add(ce);
                    TreeNodeDiagram treeNodeDiagram = new TreeNodeDiagram();
                    treeNodeDiagram.setElement(ce);
                    treeNodeDiagram.setParentNode(this);
                    treeNodeDiagram.setTreeNodeListener(this.treeNodeListener);
                    current.getChildren().add(treeNodeDiagram);
                    diagramElements1.removeAll(removed);
                    removed.clear();
                    current.getChildren().add(treeNodeDiagram);
                    treeNodeDiagram.addToNode(diagramElements1, treeNodeDiagram, removed);
                }
            }
            diagramElements1.removeAll(removed);
        }
    }

    public TreeNodeDiagram getParentNode() {
        return parentNode;
    }

    private void setParentNode(TreeNodeDiagram treeNodeDiagram) {
        this.parentNode = treeNodeDiagram;
    }

    public boolean isExecuted() {
        return isExecuted;
    }

    public void setExecuted(boolean executed) {
        isExecuted = executed;
    }


    public void execute() {
        int count = 0;
        List<TreeNodeDiagram> activeChildren = new ArrayList<>();
        for (TreeNodeDiagram element : children) {
            if (element.isExecuted() || executeIfExecuted) {
                count++;
            } else {
                element.execute();
                count++;
            }
            activeChildren.add(element);

        }

        if (element instanceof ClassSchemaBuilder.ClassMultiInputElement) {
            Class theClass = (((ClassSchemaBuilder.ClassMultiInputElement) element).theClass);
            try {
                ProcessNFiles pnf = (ProcessNFiles) theClass.newInstance();
                List<File> files1 = new ArrayList<>();
                if (activeChildren.size() > 0) {
                    for (TreeNodeDiagram tt : activeChildren) {
                        File activeChildrenFiles = tt.file;
                        files1.addAll(Arrays.asList(activeChildrenFiles));
                    }
                }
                File[] input = new File[files1.size()];
                if (files1.size() > 0)
                    pnf.processFiles(file, input);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else if (element instanceof ClassSchemaBuilder.ClassElement) {
            Class theClass = (((ClassSchemaBuilder.ClassElement) element).theClass);
            try {
                ProcessFile pf = (ProcessFile) theClass.newInstance();
                File[] activeChildrenFiles = new File[1];
                if (activeChildren.size() == 1) {
                    List<File> files = new ArrayList<>();
                    activeChildrenFiles[0] = children.get(0).file;

                    File[] input = new File[]{activeChildrenFiles[0]};

                    pf.processFiles(file, input);
                }

            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void searchForLeaves(List<TreeNodeDiagram> leaves) {
        if (leaves == null)
            leaves = new ArrayList<>();

        if (children.size() > 0) {
            for (TreeNodeDiagram tn : children) {
                if (tn.equals(this)) {
                    throw new RuntimeException("TreeNodeDiagram children==this");
                }
                tn.searchForLeaves(leaves);
            }
        } else {
            leaves.add(this);
        }
    }

    public void addListener(TreeNodeListener treeNodeListener) {
        this.treeNodeListener = treeNodeListener;
    }

    public TreeNodeListener getTreeNodeListener() {
        return treeNodeListener;
    }

    public void setTreeNodeListener(TreeNodeListener treeNodeListener) {
        this.treeNodeListener = treeNodeListener;
    }

    @Override
    public void listen(TreeDiagram treeDiagram, TreeNodeDiagram treeNodeDiagram, int code) {

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


    @Override
    public String toString() {
        return "TreeNodeDiagram{" +
                "element=" + element +
                ", isExecuted=" + isExecuted +
                ", parentNode=" + parentNode +
                ", file=" + file +
                ", children=" + children +
                ", treeNodeListener=" + treeNodeListener +
                ", executeIfExecuted=" + executeIfExecuted +
                '}';
    }
}
