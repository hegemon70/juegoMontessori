package org.sfsoft.drop;

import org.sfsoft.drop.Drop;
import org.sfsoft.drop.util.Constants;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Clase principal de la versión de escritorio (PC) del juego
 * @author Santiago Faci
 *
 */
public class DesktopDrop {

	public static void main(String[] args) {
		LwjglApplicationConfiguration configuracion = new LwjglApplicationConfiguration();
		configuracion.title = "Drop";
		configuracion.width = Constants.SCREEN_WIDTH;
		configuracion.height = Constants.SCREEN_HEIGHT;
				
		new LwjglApplication(new Drop(), configuracion);
	}
}
