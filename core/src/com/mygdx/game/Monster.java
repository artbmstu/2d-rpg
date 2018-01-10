package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Monster extends BaseUnit{
    private float x;
    private float y;
    private boolean isActive;

    public Monster(GameScreen gameScreen, Map map, TextureRegion original, float x, float y) {
        super(gameScreen, map, original, 100, 120.0f,1.0f, 35, x, y, 100, 100);
        this.x = x;
        this.y = y;
    }

    public boolean isActive() {
        return isActive;
    }

    public void update(float dt, boolean isActive) {
        this.isActive = isActive;
        if (hp == 0){
            this.restart();
        }
        fire(dt, false);
        if (Math.abs(gameScreen.getHero().getHitArea().x - hitArea.x) > 100.0f) {
            if (gameScreen.getHero().getHitArea().x < hitArea.x) {
                moveLeft();
            }
            if (gameScreen.getHero().getHitArea().x > hitArea.x) {
                moveRight();
            }
        }
        super.update(dt);
        if (Math.abs(gameScreen.getHero().getHitArea().x - hitArea.x) > 100.0f) {
            if (Math.abs(velocity.x) < 0.1f) {
                jump();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setColor(Color.RED);
        super.render(batch);
        batch.setColor(Color.WHITE);
    }
    public void renderGUI(SpriteBatch batch, BitmapFont font, int hpPos) {
        font.draw(batch, "Monster HP: " + hp + " / " + maxHp, 20, 650 - hpPos);
    }
    public void restart(){
        this.isActive = false;
        hp = 100;
        hitArea.set(x, y, width / 3, height / 3 * 2);
    }
}
