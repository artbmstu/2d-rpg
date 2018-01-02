package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class Bullet implements Poolable {
    private boolean isPlayersBullet;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    public boolean isPlayersBullet() {
        return isPlayersBullet;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    public Bullet() {
        this.position = new Vector2(0.0f, 0.0f);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.active = false;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate(boolean isPlayersBullet, float x, float y, float vx, float vy) {
        this.isPlayersBullet = isPlayersBullet;
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x > 1280 || position.x < 0 || position.y < 0 || position.y > 720) {
            deactivate();
        }
    }
}
