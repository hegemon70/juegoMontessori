package org.sfsoft.drop;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import org.sfsoft.drop.characters.*;
import org.sfsoft.drop.characters.Roca;
import org.sfsoft.drop.managers.BackgroundManager;
import org.sfsoft.drop.managers.ResourceManager;
import org.sfsoft.drop.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Pantalla del juego, donde el usuario juega la partida
 * @author Santiago Faci
 *
 */
public class GameScreen implements Screen, InputProcessor {

	final Drop juego;
	
	// Texturas e im�genes para los elementos del juego
	//TEXTURAS QUE DAN PUNTOS
	Texture spriteMoneda;
	Texture spriteBillete;
	Texture spriteSobre;
	
	//TEXTURA DEL JUGADOR
	Texture spriteJugador;
	
	//TEXTURA QUE QUITAN PUNTOS
	Texture spriteJuez;
	Texture spritePolicia;
	
	Sound sonidoCargar;
	Music musicaFondo;
	Sound sonidoRoca;
    Sound sonidoExplota;
	
	
	//FONDO QUE SE MUEVE
	BackgroundManager backgroundManager;
	
	/*
	 * Representaci�n de los elementos del juego como rect�ngulos
	 * Se utilizan para comprobar las colisiones entre los mismos
	 */
	Player naveRoja;
	Array<Enemy> enemies;
	Array<Item> items;
	
	// Controla a que ritmo van apareciendo las gotas y las rocas
	long lastItem;
	long lastEnemy;
	float tiempoJuego;
	float velocidad;

	// Indica si el juego est� en pausa
	boolean pausa = false;

    //DEPURACION ACTIVA
    boolean dep = true; //TODO cambiar a false cuando no estemos en depuracion
	boolean dado;

	OrthographicCamera camara;
	
	public GameScreen(Drop juego) {
		this.juego = juego;
		this.velocidad = 2;


        juego.nivel = 1;
        juego.vidas = 3;
        if(dep){
            juego.energia = 50;
            // Duraci�n fija de la partida
            tiempoJuego = 100;
        } else{
            juego.energia = 100;
            // Duraci�n fija de la partida
            tiempoJuego = 50;
        }

        ResourceManager.loadGameResources();
		
		//backgroundManager = new BackgroundManager(ResourceManager.textures.get("mountain"));
        backgroundManager = new BackgroundManager(ResourceManager.textures.get("fondo"));
		
		Texture.setEnforcePotImages(false);
		// Carga las im�genes del juego

		naveRoja = new Player(new Vector2(-Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT/2),
			200f, new Texture(Gdx.files.internal("plane.png")));
		
		// Carga los sonidos del juego
		sonidoCargar = Gdx.audio.newSound(Gdx.files.internal("cargar.mp3"));
		//musicaFondo = Gdx.audio.newMusic(Gdx.files.getFileHandle("backgroundmusic.wav", FileType.Internal));
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("backgroundmusic.mp3"));
		sonidoRoca = Gdx.audio.newSound(Gdx.files.internal("choque.mp3"));
        sonidoExplota = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));

		// Inicia la m�sica de fondo del juego en modo bucle
		musicaFondo.setLooping(true);
		
		// Representa el naveRoja en el juego
		// Hay que tener el cuenta que la imagen del naveRoja es de 64x64 px
		
		
		// Genera la lluvia
		items = new Array<Item>();
		generarItems();
		
		// Comienza lanzando la primera roca
		enemies = new Array<Enemy>();
		generarEnemigos();
		
		// Crea la c�mara y define la zona de visi�n del juego (toda la pantalla)
		camara = new OrthographicCamera();
        camara.setToOrtho(false, 1024, 768);
        //camara.setToOrtho(false, 4096, 1024);
		//camara.setToOrtho(false, 1024, 768);
		
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void render(float dt) {
		/*// Pinta el fondo de la pantalla de azul oscuro (RGB + alpha)
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		// Limpia la pantalla
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);*/
		
		// Actualiza la c�mara
		camara.update();
		
		/* Comprueba la entrada del usuario, actualiza
		 * la posici�n de los elementos del juego y
		 * dibuja en pantalla
		 */
		if (!pausa) {
			comprobarInput(dt);
			actualizar(dt);
		}
		// La pantalla debe se redibujada aunque el juego est� pausado
		dibujar(dt);
	}
	
	/*
	 * Comprueba la entrada del usuario (teclado o pantalla si est� en el m�vil)
	 */
	private void comprobarInput(float dt) {
		
		/*
		 * Mueve el naveRoja pulsando en la pantalla
		 */
		if (Gdx.input.isTouched()) {
			Vector3 posicion = new Vector3();
			posicion.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			/*
			 * Transforma las coordenadas de la posici�n
			 * al sistema de coordenadas de la c�mara
			 */
			//camara.unproject(posicion);
			naveRoja.moveTo(new Vector2(posicion.x - 64 / 2,
                    posicion.y));
		}
		
		/*
		 * Mueve el naveRoja pulsando las teclas LEFT y RIGHT
		 */
		if (Gdx.input.isKeyPressed(Keys.LEFT)){
			naveRoja.move(new Vector2(-dt*velocidad, 0));
			naveRoja.setTexture(new Texture(Gdx.files.internal("plane_2.png")));
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)){
			naveRoja.move(new Vector2(dt*velocidad, 0));
			naveRoja.setTexture(new Texture(Gdx.files.internal("plane.png")));
		}
		if (Gdx.input.isKeyPressed(Keys.UP))
			naveRoja.move(new Vector2(0, dt*velocidad));
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			naveRoja.move(new Vector2(0, -dt*velocidad));

        /*
		 * Si el juego está en pausa no se refresca la lógica
		 */
        if (!juego.paused) {
            actualizar(dt);
            //juego.getShip().update(dt, juego);
        }

        handleKeyboard();


    }
	
	/*
	 * Actualiza la posici�n de todos los elementos
	 * del juego
	 */
	private void actualizar(float dt) {
			
		/*
		 * Comprueba que el naveRoja no se salga de los
		 * l�mites de la pantalla
		 */
		if (naveRoja.position.x < 0)
			naveRoja.position.x = 0;
		if (naveRoja.position.x > Constants.SCREEN_WIDTH - 64)
			naveRoja.position.x = Constants.SCREEN_WIDTH - 64;

        if (naveRoja.position.y < 0)
            naveRoja.position.y = 0;
        if (naveRoja.position.y > Constants.SCREEN_HEIGHT - 64)
            naveRoja.position.y = Constants.SCREEN_HEIGHT - 64;
		
		/*
		 * Genera nuevas gotas dependiendo del tiempo que ha
		 * pasado desde la �ltima
		 */
		if (TimeUtils.nanoTime() - lastItem > 1000000000)
			generarItems();
		
		/*
		 * Genera nuevas rocas
		 */
		if (TimeUtils.nanoTime() - lastEnemy > 1000000000)
			generarEnemigos();
		
		/*
		 * Actualiza las posiciones de las gotas
		 * Si la gota llega al suelo se elimina
		 * Si la gota toca el naveRoja suena y se elimina
		 */
		Iterator<Item> iter = items.iterator();
		while (iter.hasNext()) {
			Item item = iter.next();
			item.move(new Vector2(-dt, 0));
			if (item.position.y + 64 < 0)
				iter.remove();
			if (item.rect.overlaps(naveRoja.rect)) {
				
				iter.remove();
				
				switch (item.getClass().getSimpleName()) {
				case "Moneda":
                    sonidoCargar.play();
					juego.energia += 100;
                    juego.vidas+=1;
					if (velocidad == 2){
						
					}else if (velocidad <2){
						velocidad += (float) 0.5;
					}
					break;
				case "Billete":
					sonidoCargar.play();
					break;
				default:
				}
				
				juego.gotasRecogidas += item.score;
			}
		}

		/*
		 * Actualiza las posiciones de las rocas
		 * Si la roca llega al suelo se elimina
		 * Si la roca toca el naveRoja lo rompe y termina la partida
		 */


		Iterator<Enemy> iterEnemies = enemies.iterator();
		while (iterEnemies.hasNext()) {
			Enemy enemy = iterEnemies.next();
			enemy.move(new Vector2(-dt, 0));
			if (enemy.position.y + 50 < 0)
				iterEnemies.remove();
            // Comprueba si la nave colisiona con algún enemigo
            for (int i = enemies.size - 1; i >= 0; i--) {
                enemy = enemies.get(i);

                if (enemy.rect.overlaps(naveRoja.rect)) {

                    //Explosion explosion = new Explosion(enemy.getX(), enemy.getY());
                    //explosions.add(explosion);
                    //if (juego.configurationManager.isSoundEnabled())
                    //    ResourceManager.getSound("explosion").play();

                    if (enemy.getClass().getSimpleName().equals("Roca")) {
                        // El usuario muere al chocar con una roca
                        //juego.score = ship.getScore();
                        juego.vidas-=1;
                        enemies.removeIndex(i);
                        sonidoRoca.play();
                    }

                    if (enemy.getClass().getSimpleName().equals("NaveEnemiga")) {
                        // El usuario muere al chocar con una roca
                        //juego.score = ship.getScore();
                        juego.setScreen(new GameOverScreen(juego));
                        sonidoExplota.play();
                    }

                    if (juego.vidas == 0) {
                        juego.setScreen(new GameOverScreen(juego));
                    }
                }
            }

		}

		
		// Actualiza el tiempo de juego
		tiempoJuego -= Gdx.graphics.getDeltaTime();
		
		if ((int)juego.energia == 0){
			velocidad=(float) 0.5;
            juego.energia = 0;
		} else if((int)juego.energia <200){
			velocidad=(float) 1;
            juego.energia -= 0.07;
		} else{
            juego.energia -= 0.07;
        }

        if(juego.vidas==0){
            juego.setScreen(new GameOverScreen(juego));
        }

		if (tiempoJuego < 0) {
			dispose();
			juego.setScreen(new GameScreen2(juego));
		}

	}
	
	/*
	 * Dibuja los elementos del juego en pantalla
	 */
	private void dibujar(float dt) {
		
		// Pinta la im�genes del juego en la pantalla
		/* setProjectionMatrix hace que el objeto utilice el 
		 * sistema de coordenadas de la c�mara, que 
		 * es ortogonal
		 * Es recomendable pintar todos los elementos del juego
		 * entras las mismas llamadas a begin() y end()
		 */

        juego.spriteBatch.begin();

        //FONDO
        backgroundManager.render(juego.spriteBatch, dt);

		//juego.spriteBatch.setProjectionMatrix(camara.combined);
		naveRoja.render(juego.spriteBatch);

		for (Item item : items)
			item.render(juego.spriteBatch);
		for (Enemy enemy : enemies)
			enemy.render(juego.spriteBatch);
        juego.fuente.setColor(Color.RED);
		juego.fuente.draw(juego.spriteBatch, "Puntos:  " + juego.gotasRecogidas, 1024 - 100, 768 - 50);
		juego.fuente.draw(juego.spriteBatch, "Tiempo:  " + (int) (tiempoJuego), 1024 - 100, 768 - 80);
		juego.fuente.draw(juego.spriteBatch, "Energia:  " + (int) juego.energia , 1024 - 100, 768 - 100);
        juego.fuente.draw(juego.spriteBatch, "NIVEL:  " + juego.nivel , 1024 - 100, 768 - 120);
        juego.fuente.draw(juego.spriteBatch, "VIDAS:  " + juego.vidas , 1024 - 100, 768 - 140);
		juego.spriteBatch.end();
	}
	
	/**
	 * Genera una gota de lluvia en una posici�n aleatoria
	 * de la pantalla y anota el momento de generarse
	 */
	private void generarItems() {
		
		int n = MathUtils.random(0, 100);
		Item item = null;
		int y = MathUtils.random(0, Constants.SCREEN_WIDTH);
		
		if (n < 35) {
			item = new Moneda(new Vector2(Constants.SCREEN_HEIGHT,y),
				350f, new Texture(Gdx.files.internal("moneda.png")), 5);
		}
		else{
			item = new Billete(new Vector2(Constants.SCREEN_HEIGHT, y),
				200F, new Texture(Gdx.files.internal("billete.png")), 1);
		}
		
		items.add(item);
		lastItem = TimeUtils.nanoTime();
	}
	
	/**
	 * Genera una roca y la deja caer
	 */
	private void generarEnemigos() {
		
		//GENERO ROCAS
		int y = MathUtils.random(0, Constants.SCREEN_WIDTH);
		Enemy enemy = new Roca(new Vector2(Constants.SCREEN_WIDTH, y),
			250f, new Texture(Gdx.files.internal("rock.png")));
		enemies.add(enemy);
		
		//GENERO NAVES
		y = MathUtils.random(0, Constants.SCREEN_WIDTH);
		enemy = new NaveEnemiga(new Vector2(Constants.SCREEN_WIDTH, y),
			300f, new Texture(Gdx.files.internal("nave.png")));
		enemies.add(enemy);
		
		lastEnemy = TimeUtils.nanoTime();
	}
	
	/*
	 * M�todo que se invoca cuando esta pantalla es
	 * la que se est� mostrando
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		musicaFondo.play();
	}

	/*
	 * M�todo que se invoca cuando est� pantalla
	 * deja de ser la principal
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		musicaFondo.stop();
	}
	
	@Override
	public void dispose() {
		// Libera los recursos utilizados
		sonidoCargar.dispose();
		musicaFondo.dispose();
		sonidoRoca.dispose();
        sonidoExplota.dispose();
		
		items.clear();
		enemies.clear();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		pausa = true;
	}

	@Override
	public void resume() {
		pausa = false;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		// Pone el juego en pausa
		if (keycode == Keys.P)
			pausa = !pausa;
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

    private void handleKeyboard() {

		/*
		 * Si el usuario pulsa la tecla ESCAPE se muestra un menú durante el juego
		 */
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            juego.setScreen(new MenuPausa(juego, this));
        }
    }
}
