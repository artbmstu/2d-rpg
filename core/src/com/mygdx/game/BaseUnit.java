package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class BaseUnit {
    protected GameScreen gameScreen;
    protected Map map;
    protected TextureRegion[] regions;
    protected Vector2 position;
    protected Vector2 tempPosition;
    protected Vector2 velocity;
    protected float animationTime;
    protected boolean right;
    protected int maxHp;
    protected int hp;
    protected Circle hitArea;
    protected int width;
    protected int height;
    protected float firePressTimer;
    protected float timeBetweenFire;
    protected float speed;

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public BaseUnit(GameScreen gameScreen, Map map, TextureRegion original, int maxHp, float speed, float timeBetweenFire, float radius, float x, float y, int width, int height) {
        this.gameScreen = gameScreen;
        this.map = map;
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.tempPosition = new Vector2(0, 0);
        this.width = width;
        this.height = height;
        this.regions = new TextureRegion(original).split(width, height)[0];
        this.right = true;
        this.maxHp = maxHp;
        this.speed = speed;
        this.hp = this.maxHp;
        this.hitArea = new Circle(position, radius);
        this.timeBetweenFire = timeBetweenFire;
    }

    public void update(float dt) {
        velocity.add(0, -600.0f * dt);
        tempPosition.set(position);
        velocity.x *= 0.6f;
        float len = velocity.len() * dt;
        float dx = velocity.x * dt / len;
        float dy = velocity.y * dt / len;
        for (int i = 0; i < len; i++) {
            tempPosition.y += dy;
            if (checkCollision(tempPosition)) {
                tempPosition.y -= dy;
                velocity.y = 0.0f;
            }
            tempPosition.x += dx;
            if (checkCollision(tempPosition)) {
                tempPosition.x -= dx;
                velocity.x = 0.0f;
            }
        }
        if (Math.abs(velocity.x) > 1.0f) {
            if (Math.abs(velocity.y) < 1.0f) {
                animationTime += (Math.abs(velocity.x) / 1800.0f);
            }
        } else {
            if (getCurrentFrame() > 0) {
                animationTime += dt * 50.0f;
            }
        }
        position.set(tempPosition);
        hitArea.setPosition(position.x + width / 2, position.y + height / 2);
    }

    public void moveLeft(){
        velocity.x = -speed;
        right = false;
    }
    public void moveRight(){
        velocity.x = speed;
        right = true;
    }
    public void fire(float dt, boolean isPlayer){
        firePressTimer += dt;
        if (firePressTimer > timeBetweenFire) {
            float bulletVelX = 600.0f;
            if (!right) bulletVelX *= -1;
            firePressTimer -= timeBetweenFire;
            if (right) gameScreen.getBulletEmitter().setup(isPlayer, position.x + width / 2 + 35, position.y + height / 2, bulletVelX, 0, gameScreen.getShoot());
            else gameScreen.getBulletEmitter().setup(isPlayer, position.x + width / 2 - 35, position.y + height / 2, bulletVelX, 0, gameScreen.getShoot());
        }
    }
    public void instantFire(float dt, boolean isPlayer){
        float bulletVelX = 600.0f;
        if (!right) bulletVelX *= -1;
        gameScreen.getBulletEmitter().setup(isPlayer, position.x + width / 2, position.y + height / 2, bulletVelX, 0, gameScreen.getShoot());
    }
    public void jump(){
        tempPosition.set(position).add(0, 1);
        if (Math.abs(velocity.y) < 1.0f) {
            velocity.y = 400.0f;
        }
    }
    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    public boolean checkCollision(Vector2 pos) {
        for (int i = 0; i <= 5; i++) {
            if (!map.checkSpaceIsEmpty(pos.x + 25 + i * 10, pos.y)){
                return true;
            }
            if (!map.checkSpaceIsEmpty(pos.x + 25 + i * 10, pos.y + 90)){
                return true;
            }
            if (!map.checkSpaceIsEmpty(pos.x + 25, pos.y + i * 18 )){
                return true;
            }
            if (!map.checkSpaceIsEmpty(pos.x + 75, pos.y + i * 18)){
                return true;
            }
        }
        return false;
    }

    public void render(SpriteBatch batch) {
        int frameIndex = getCurrentFrame();
        if (!right && !regions[frameIndex].isFlipX()) {
            regions[frameIndex].flip(true, false);
        }
        if (right && regions[frameIndex].isFlipX()) {
            regions[frameIndex].flip(true, false);
        }
        batch.draw(regions[frameIndex], position.x, position.y, width/2, height/2, width, height, 1, 1, 0);
    }

    public int getCurrentFrame() {
        return (int) animationTime % regions.length;
    }
}
