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

package one.empty3.apps.opad.objloader;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import one.empty3.apps.opad.*;
import one.empty3.library.LineSegment;
import one.empty3.library.Point2D;


@SuppressWarnings("serial")
public class Example extends JoglDrawer implements GLEventListener {
	
	private static int width;
	private static int height;
    private FPSAnimator animator;
	
	private GLModel chairModel = null;

	public Example(DarkFortressGUI darkFortressGUI) {
        super(darkFortressGUI);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glLoadIdentity();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		gl.glTranslatef(0,0,-1);
		gl.glScalef(0.01f, 0.01f, 0.01f);
		chairModel.opengldraw(gl);

		gl.glFlush();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		
		if (!loadModels(gl)) {
			System.exit(1);
		}
		
		setLight(gl);

		glu.gluPerspective(1, (double) width/ height, 0.3, 50);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	private void setLight(GL2 gl) {
		
		gl.glEnable(GL2.GL_LIGHTING);
		
		float SHINE_ALL_DIRECTIONS = 1;
		float[] lightPos = { -30, 30, 30, SHINE_ALL_DIRECTIONS };
		float[] lightColorAmbient = { 0.02f, 0.02f, 0.02f, 1f };
		float[] lightColorSpecular = { 0.9f, 0.9f, 0.9f, 1f };

		// Set light parameters.
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightColorAmbient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightColorSpecular, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightColorSpecular, 0);
		gl.glEnable(GL2.GL_LIGHT1);
		
	}

	private Boolean loadModels(GL2 gl) {
		chairModel = ModelLoaderOBJ.LoadModel("resources/models/c.obj",
                "resources/models/c.mtl", gl);
		if (chairModel == null) {
			return false;
		}
		return true;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();

		glu.gluPerspective(100, (double) width/ height, 0.1, 100);
		gl.glMatrixMode(GL2.GL_MODELVIEW);

	}

	public static void main(String[] args) {
        DarkFortressGUI darkFortressGUI = new DarkFortressGUI(Example.class);

        new Example(darkFortressGUI);

        darkFortressGUI.setVisible(true);

	}

    @Override
    public void setLogic(PositionUpdate l) {

    }

    @Override
    public LineSegment click(Point2D p) {
        return null;
    }
}
