package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {
    private TextureAtlas atlas;
    private SpriteBatch batch;
    private Map map;
    private Hero hero;
    private BitmapFont font;
    private Trash[] trashes;
    private PowerUpsEmitter powerUpsEmitter;
    private BulletEmitter bulletEmitter;
    private int counter;
    private Sound soundTakeMoney;
    private ShapeRenderer shapeRenderer;
    private Monster monster;
    private final static boolean DEBUG_MODE = true;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public Hero getHero() {
        return hero;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    @Override
    public void show() {
        atlas = new TextureAtlas(Gdx.files.internal("mainPack.pack"));
        map = new Map(atlas.findRegion("star16"), atlas.findRegion("ground"));
        map.generateMap();
        hero = new Hero(this, map, atlas.findRegion("runner"), 300, 500);
        monster = new Monster(this, map, atlas.findRegion("runner"), 700, 500);
        trashes = new Trash[30];
        TextureRegion asteroidTexture = atlas.findRegion("asteroid64");
        for (int i = 0; i < trashes.length; i++) {
            trashes[i] = new Trash(asteroidTexture);

            trashes[i].prepare();
        }
//        soundTakeMoney = Gdx.audio.newSound(Gdx.files.internal("takeMoney.wav"));
        powerUpsEmitter = new PowerUpsEmitter(atlas.findRegion("money"));
        bulletEmitter = new BulletEmitter(atlas.findRegion("bullet48"), 100);
        if (DEBUG_MODE) {
            shapeRenderer = new ShapeRenderer();
            shapeRenderer.setAutoShapeType(true);
        }
        generateFonts();
    }

    public void generateFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("zorque.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.size = 24;
        parameters.color = Color.WHITE;
        parameters.borderWidth = 1;
        parameters.borderColor = Color.BLACK;
        parameters.shadowOffsetX = 3;
        parameters.shadowOffsetY = 3;
        parameters.shadowColor = Color.BLACK;
        font = generator.generateFont(parameters);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        map.render(batch);
        hero.render(batch);
        monster.render(batch);
        for (int i = 0; i < trashes.length; i++) {
            trashes[i].render(batch);
        }
        powerUpsEmitter.render(batch);
        bulletEmitter.render(batch);
        hero.renderGUI(batch, font);
        monster.renderGUI(batch, font);
        batch.end();
        if (DEBUG_MODE) {
            shapeRenderer.begin();
            shapeRenderer.circle(hero.getHitArea().x, hero.getHitArea().y, hero.getHitArea().radius);
            shapeRenderer.end();
        }
    }

    public void update(float dt) {
        counter++;
        if (counter % 50 == 0) {
            powerUpsEmitter.tryToCreatePowerUp(MathUtils.random(0, 1280), MathUtils.random(200, 250), 1.0f);
        }
        map.update(dt);
        hero.update(dt);
        monster.update(dt);
        bulletEmitter.update(dt);
        powerUpsEmitter.update(dt);
        for (int i = 0; i < trashes.length; i++) {
            trashes[i].update(dt);
            if (hero.getHitArea().overlaps(trashes[i].getHitArea())) {
                trashes[i].prepare();
                hero.takeDamage(5);
            }
        }
        for (int i = 0; i < powerUpsEmitter.getPowerUps().length; i++) {
            PowerUp p = powerUpsEmitter.getPowerUps()[i];
            if (p.isActive() && hero.getHitArea().contains(p.getPosition())) {
                p.use(hero);
                p.deactivate();
//                soundTakeMoney.play();
            }
        }
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            if (!map.checkSpaceIsEmpty(bulletEmitter.getActiveList().get(i).getPosition().x, bulletEmitter.getActiveList().get(i).getPosition().y)) {
                bulletEmitter.getActiveList().get(i).deactivate();
            }
        }
        bulletEmitter.checkPool();
        for (int i = 0; i < bulletEmitter.getActiveList().size(); i++) {
            if (monster.hitArea.contains(bulletEmitter.getActiveList().get(i).getPosition())){
                monster.takeDamage(5);
                bulletEmitter.getActiveList().get(i).deactivate();
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (DEBUG_MODE){
            shapeRenderer.dispose();
        }
    }
}
