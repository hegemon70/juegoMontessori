package org.sfsoft.drop.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Roca extends Enemy {

	public Roca(Vector2 position, float speed,
                Texture texture) {
		super(position, speed, texture);
	}
	
	@Override
	public void update(float dt) {
		
		move(new Vector2(0, -dt));
	}
}