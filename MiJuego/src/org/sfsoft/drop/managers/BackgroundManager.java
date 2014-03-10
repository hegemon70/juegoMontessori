package org.sfsoft.drop.managers;

import org.sfsoft.drop.util.Constants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackgroundManager {

    Texture texture;
    float scrollTimer;
    float speed;
    Sprite sprite;
    int PAUSE;

    public BackgroundManager(Texture texture) {
        this.texture = texture;
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.scrollTimer = 0f;
        sprite = new Sprite(texture, 0, 0, texture.getWidth(), texture.getHeight());
        sprite.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        this.speed = 0.1f;
        this.PAUSE = 1;
    }

    public void render(SpriteBatch batch, float delta) {
        scrollTimer += delta * speed * PAUSE;
        if(scrollTimer>1f)
            scrollTimer = 0f;

        sprite.setU(scrollTimer);
        sprite.setU2(scrollTimer+1f);

        sprite.draw(batch);
    }

    public void stop() {
        PAUSE = 0;
    }

    public void start() {
        PAUSE = 1;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
