package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Monster extends BaseUnit{
    private float x;
    private float y;

    public Monster(GameScreen gameScreen, Map map, TextureRegion original, float x, float y) {
        super(gameScreen, map, original, 100, 120.0f,1.0f, 35, x, y, 100, 100);
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(float dt) {
        if (hp == 0){
            restartMonster();
        }
        fire(dt, false);
        if (Math.abs(gameScreen.getHero().getPosition().x - position.x) > 100.0f) {
            if (gameScreen.getHero().getPosition().x < position.x) {
                moveLeft();
            }
            if (gameScreen.getHero().getPosition().x > position.x) {
                moveRight();
            }
        }
        super.update(dt);
        if (Math.abs(gameScreen.getHero().getPosition().x - position.x) > 100.0f) {
            if (Math.abs(velocity.x) < 0.1f) {
                jump();
            }
        }
        //Change
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(Color.RED);
        super.render(batch);
        batch.setColor(Color.WHITE);
    }
    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        font.draw(batch, "Monster HP: " + hp + " / " + maxHp, 20, 650);
    }
    public void restartMonster(){
        position.set(x, y);
        hp = 100;
    }
}
