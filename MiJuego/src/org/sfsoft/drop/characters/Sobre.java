package org.sfsoft.drop.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Sobre extends Item {

	public Sobre(Vector2 position, float speed, 
		Texture texture, int score) {
		super(position, speed, texture, score);
	}

	@Override
	public void update(float dt) {
		
		move(new Vector2(0, -dt));
	}
}
