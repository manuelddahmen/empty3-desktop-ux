# FaceDetect3 Application

## Description

This document provides instructions for compiling and running the `facedetect3` application. `facedetect3` is a desktop application for selecting and matching points between an image and a 3D model.

## Features

*   **Panel Type Selection:** Choose between "Image" or "OBJ Model" for both the left and right panels.
*   **Load Image/Model:** Load an image or an OBJ model into the selected panel. The OBJ model will be rendered using a Z-buffer in a separate thread to keep the UI responsive, and the camera will automatically adjust to fit the model in the panel.
*   **Point Selection:** Click on either the image or the model to add a point.
*   **Save/Load Points:** Save the selected points to a file and load them back later.
*   **Clear Points:** Clear all points from both the image and the model.
*   **Remove Last Point:** Remove the last point added to the active panel.
    The u,v of models are included in zBuffer.ime.uMap and zBuffer.ime.vMap If I click on a object, you should add to model's points list a entry. In case of image u,v are representing x/w and y/h ratio cordinates. In addition to the 2 panel, there will be a 3 columns list with matched-a-b points, and in 3rd column the name of the pair. Unassociated points should be unique on line.
## Compilation

To compile the application, navigate to the `src/main/java` directory and run the following command:

```bash
javac one/empty3/apps/facedetect3/Main.java
```

## Execution

After successful compilation, you can run the application using the following command from the `src/main/java` directory:

```bash
java one.empty3.apps.facedetect3.Main
```

This will launch the graphical user interface for the application.
