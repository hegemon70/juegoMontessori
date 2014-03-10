package org.sfsoft.drop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.sfsoft.drop.util.Constants;

/**
 * Clase principal de la versi�n de escritorio (PC) del juego
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
