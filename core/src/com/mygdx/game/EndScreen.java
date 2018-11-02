package com.mygdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jackwa on 1/5/18.
 */
public class EndScreen implements Screen, InputProcessor {

    private ExtendViewport viewport;
    private Camera camera;
    private ScreenViewport textViewport;
    private Camera textCamera;
    private BitmapFont font;
    private BitmapFont headerFont;
    private SpriteBatch batch;
    private int score;
    private int playType;
    private HashMap bonuses;
    private Preferences prefs;

    private TextureRegion backgroundTexture;

    // button to go back to main menu
    private MyShapeRenderer renderer;
    private Rectangle mainMenuRectangle;
    private Game game;

    private List<String> wordsPlayed;

    public EndScreen(List<String> words, int score, int playType, HashMap bonuses, Game game) {
        this.wordsPlayed = words;
        this.score = score;
        this.playType = playType;
        this.bonuses = bonuses;
        this.game = game;

    }

    @Override
    public void show() {

        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        camera = viewport.getCamera();
        textViewport = new ScreenViewport();
        textCamera = textViewport.getCamera();
        ApplicationType appType = Gdx.app.getType();
        if (appType == ApplicationType.Android || appType == ApplicationType.iOS) {
            font = new FontHandler("fonts/Eczar-Medium.ttf", 25).getBitmapFont();
            headerFont = new FontHandler("fonts/FFF_Tusj.ttf", 80).getBitmapFont();
        } else { font = new FontHandler("fonts/Eczar-Medium.ttf", 15).getBitmapFont();
            headerFont = new FontHandler("fonts/FFF_Tusj.ttf", 40).getBitmapFont();
        } //if its a desktop
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.getData().setScale(3f);
        font.setColor(Color.BLACK);
        batch = new SpriteBatch();
        Texture bgTexture = new Texture(Gdx.files.internal("images/purty_wood.png"));
        bgTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        backgroundTexture = new TextureRegion(bgTexture,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainMenuRectangle = new Rectangle(Constants.CRED_BUTTON_X, Constants.CRED_BUTTON_Y, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        renderer = new MyShapeRenderer();

        Gdx.input.setInputProcessor(this);

        prefs = Gdx.app.getPreferences("My Preferences");

    }

    @Override
    public void render(float delta) {



        // clear screen
        Gdx.gl.glClearColor( 1, 1, 1, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        // draw background
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        batch.end();

        textViewport.apply();
        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();
        Vector3 creditsInScreenCoords = camera.project(new Vector3(Constants.X_CREDITS, Constants.Y_CREDITS, 0));
        headerFont.setColor(Color.BLACK);
        headerFont.draw(batch, "GAME COMPLETE" + "\n", creditsInScreenCoords.x, creditsInScreenCoords.y);
        font.getData().setScale(1.2f);

        // get top score and compare to current store to see if congratulations are in order

        font.draw(batch, "Your final score was " + score, creditsInScreenCoords.x, creditsInScreenCoords.y - 40f);
        int topScore = 0;
        // if timed game
        if (playType == 0) {
            topScore = prefs.getInteger("topTimedScore", 0);
        } else {
            topScore = prefs.getInteger("topScore", 0);
        }
        if (score >= topScore) {
                font.draw(batch, "Congratulations, you set a new high score!", creditsInScreenCoords.x, creditsInScreenCoords.y - 60f);
        } else {
                font.draw(batch, "For comparison, the current top score is " + topScore, creditsInScreenCoords.x, creditsInScreenCoords.y - 60f);
        }

        String bonusesScored = "";
        Iterator it = bonuses.entrySet().iterator();
        int bonusCount = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if ((Integer)pair.getValue() > 0) {
                bonusCount++;
                bonusesScored += pair.getKey() + " (" + pair.getValue() + ")";
                if (it.hasNext()) {
                    bonusesScored += " ";
                }
                if (bonusCount > 2) {
                    bonusesScored += "\n";
                }

        }
    }
        font.draw(batch, "Bonuses achieved : " + bonusesScored, creditsInScreenCoords.x, creditsInScreenCoords.y - 80f);

        String words = "";
        words = words + "\n";
        for (int i = 0; i < wordsPlayed.size(); i++)  {
            words = words + " " + wordsPlayed.get(i);
            // new line every five words
            if (i > 0 && i % 5 == 0) {
                words = words + "\n";
            }
        }
        font.draw(batch, "You played the following valid words: ", creditsInScreenCoords.x, creditsInScreenCoords.y - 150f);

        // draw words
        font.draw(batch, words, creditsInScreenCoords.x, creditsInScreenCoords.y - 160f);





        batch.end();

        // apply the general viewport
        viewport.apply(true);

        // set projection matrix to combined
        renderer.setProjectionMatrix(camera.combined);

        // draw main menu button
        renderer.begin(ShapeType.Filled);
        renderer.setColor(Constants.BUTTON_FILL_COLOR);
        renderer.roundedRect(mainMenuRectangle.x, mainMenuRectangle.y, mainMenuRectangle.width, mainMenuRectangle.height, Constants.BUTTON_RADIUS, Constants.BUTTON_FILL_COLOR);

        renderer.end();

        // button text
        textViewport.apply();
        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();

        // draw text on button to go back to main menu
        Vector3 returnButtonInScreenCoords = camera.project(new Vector3(mainMenuRectangle.x, mainMenuRectangle.y, 0));
        font.setColor(Constants.BUTTON_TEXT_COLOR);
        font.getData().setScale(Constants.MESSAGE_FONT_SIZE);
        font.draw(batch, "MAIN MENU", returnButtonInScreenCoords.x + 25f, returnButtonInScreenCoords.y + 40f);

        font.setColor(Color.BLACK);
        font.getData().setScale(1.2f);

        batch.end();



    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        textViewport.update(width, height, true);
        font.getData().setScale(Math.min(width, height) / Constants.FONT_SIZE_REF);
        backgroundTexture.setRegionWidth(width);
        backgroundTexture.setRegionHeight(height);

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

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 touchPoint = viewport.unproject(new Vector2(screenX, screenY));
        if (mainMenuRectangle.contains(touchPoint)) {
            // new game
            game.setScreen(new MainMenuScreen(game));
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
