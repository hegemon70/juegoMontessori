package org.sfsoft.drop.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    public static Map<String, Animation> animations = new HashMap<String, Animation>();
    public static Map<String, Texture> textures = new HashMap<String, Texture>();
    public static Map<String, Sound> sounds = new HashMap<String, Sound>();
    public static Map<String, Music> songs = new HashMap<String, Music>();

    public static void loadMenuResources() {

    }

    public static void loadCharSelectResources() {
        //Cursor, icons and backgrounds
        loadResource("grid select", new Texture(Gdx.files.internal("images/main_menu/char_grid.png")));
        loadResource("reimu select", new Texture(Gdx.files.internal("images/main_menu/reimu_grid.png")));
        loadResource("reimu effect", new Texture(Gdx.files.internal("images/main_menu/reimu_effect.png")));
        loadResource("marisa select", new Texture(Gdx.files.internal("images/main_menu/marisa_grid.png")));
        loadResource("marisa effect", new Texture(Gdx.files.internal("images/main_menu/marisa_effect.png")));
        loadResource("miko select", new Texture(Gdx.files.internal("images/main_menu/miko_grid.png")));
        loadResource("miko effect", new Texture(Gdx.files.internal("images/main_menu/miko_effect.png")));
        loadResource("cursor", new Texture(Gdx.files.internal("images/main_menu/cursor.png")));
        loadResource("bg select", new Texture(Gdx.files.internal("images/main_menu/bg_select.png")));
        loadResource("bg right", new Texture(Gdx.files.internal("images/main_menu/bg_right.png")));
        loadResource("bg left", new Texture(Gdx.files.internal("images/main_menu/bg_left.png")));
        loadResource("text reimu", new Texture(Gdx.files.internal("images/text/reimu.png")));
        loadResource("text miko", new Texture(Gdx.files.internal("images/text/miko.png")));
        loadResource("text marisa", new Texture(Gdx.files.internal("images/text/marisa.png")));
        loadResource("text back", new Texture(Gdx.files.internal("images/text/back_window.png")));

        //Animations
        //loadResource("reimu idle", new AnimarTexturas("images/reimu/idle/idle.pack", 0.07f, false).cargarTexturas());
        //loadResource("miko idle", new AnimarTexturas("images/miko/idle/idle.pack", 0.07f, false).cargarTexturas());
        //loadResource("marisa idle", new AnimarTexturas("images/witch/idle/idle.pack", 0.07f, false).cargarTexturas());

        //Sounds
        loadResource("cursor move", Gdx.audio.newSound(Gdx.files.internal("sounds/cursor_move.wav")));
        loadResource("menu ok", Gdx.audio.newSound(Gdx.files.internal("sounds/menu_ok.wav")));
    }

    public static void loadGameResources() {
      
        //Textures for backgrounds
        //loadResource("fondo", new Texture(Gdx.files.internal("assets/background.png")));
        loadResource("fondo", new Texture(Gdx.files.internal("assets/pantalla1.png")));
        loadResource("fondo2", new Texture(Gdx.files.internal("assets/pantalla2.png")));
        //loadResource("bamboo", new Texture(Gdx.files.internal("images/background/bamboo.png")));
        //loadResource("desert", new Texture(Gdx.files.internal("images/background/desert.png")));

    }

    public static void loadResource(String name, Animation animation) {
        animations.put(name, animation);
    }

    public static void loadResource(String name, Texture texture) {
        textures.put(name, texture);
    }

    public static void loadResource(String name, Sound sound) {
        sounds.put(name, sound);
    }

    public static void loadResource(String name, Music song) {
        songs.put(name, song);
    }

}
