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


import one.empty3.feature.PixM;
import one.empty3.io.ProcessFile;
import org.apache.commons.net.ftp.FTPFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProcessBean extends Thread {
    final FTPFile ftpFile;
    private List<ProcessFile> processInstances = new ArrayList<ProcessFile>();
    private List<File> files = new ArrayList<>();
    private File file;
    boolean ftp = false;

    public ProcessBean(File file) {
        this.ftp = false;
        this.file = file;

        ftpFile = null;
    }

    public void addProcess(Object o) {
        if (o instanceof ProcessFile) {
            processInstances.add((ProcessFile) o);
        }
    }

    public ProcessBean(FTPFile file) {
        this.ftp = true;
        this.ftpFile = file;

    }

    @Override
    public void run() {

    }

    public static List<ProcessBean> processBeanList(FTPFile[] files1) {
        List<ProcessBean> beans = new ArrayList<>();
        for (int i = 0; i < files1.length; i++) {
            ProcessBean processBean = new ProcessBean(files1[i]);
            beans.add(processBean);
        }
        return beans;
    }

    public static List<ProcessBean> processBeanList(File[] list) {
        List<ProcessBean> beans = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            if (list[i].exists() && list[i].isDirectory()) {
                for (int j = 0; j < list[i].length(); j++) {
                    String[] list1 = list[i].list();
                    File[] list2 = new File[list1.length];
                    for (int k = 0; k < list1.length; k++) {
                        File file = new File(list[j].getAbsolutePath() +
                                File.separator + list1[k]);
                        list2[k] = file;

                    }
                    beans.addAll(processBeanList(list2));
                }
            }
            ProcessBean processBean = new ProcessBean(list[i]);
            beans.add(processBean);
        }
        return beans;
    }


    public PixM getStack(int i) {
        return listImage.get(i);
    }

    public void setImage(File fo) {
        try {
            listImage.add(new PixM(ImageIO.read(fo)));
        } catch (Exception ex) {
        }

    }

    List<PixM> listImage = new ArrayList<>();


    public List<File> files() {
        return files;
    }

    public void config(SetSettings setSettings) {

    }
}
