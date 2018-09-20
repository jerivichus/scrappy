package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

/**
 * Created by jackwa on 4/19/18.
 */
public class HelpScreen implements Screen, InputProcessor {

    private ExtendViewport viewport;
    private Camera camera;
    private ScreenViewport textViewport;
    private Camera textCamera;
    private BitmapFont font;
    private SpriteBatch batch;

    // button to go back to main menu
    private MyShapeRenderer renderer;
    private Rectangle mainMenuRectangle;
    private TextureRegion backgroundTexture;
    private Game game;

    public HelpScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {

        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        camera = viewport.getCamera();
        textViewport = new ScreenViewport();
        textCamera = textViewport.getCamera();
        font = new FontHandler("fonts/Eczar-Medium.ttf",15).getBitmapFont();
        batch = new SpriteBatch();

        renderer = new MyShapeRenderer();

        Texture bgTexture = new Texture(Gdx.files.internal("images/purty_wood.png"));
        bgTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        backgroundTexture = new TextureRegion(bgTexture,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        mainMenuRectangle = new Rectangle(10, 5,  Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);

        Gdx.input.setInputProcessor(this);



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


        // apply text viewport and write help text
        textViewport.apply();
        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();

        // do here
        font.setColor(Color.BLACK);
        Vector3 helpTextInScreenCoords = camera.project(new Vector3(10, Constants.WORLD_SIZE - 5, 0));
        font.draw(batch, "--You will receive 17 sets of 6 letters each. For each set, try to make the highest-scored word possible.\n", helpTextInScreenCoords.x, helpTextInScreenCoords.y, 500, 200, true);
        font.draw(batch, "--Make words by arranging the letter tiles (swiping right or left). Discard unnecessary tiles by touching the 'X' button in the upper right corner of each tile. Touch the submit button to submit your word.\n", helpTextInScreenCoords.x, helpTextInScreenCoords.y - 50, 500, 200, true);
        font.draw(batch, "--Invalid words are 30 points off your score, and valid words are scored according to the value of each letter used (displayed on the letter's tile).\n", helpTextInScreenCoords.x, helpTextInScreenCoords.y - 115, 500, 200, true);
        font.draw(batch, "--Also, 5 points are removed for each letter tile discarded, so there is a benefit to using as many tiles in each set as possible.\n", helpTextInScreenCoords.x, helpTextInScreenCoords.y-165, 500, 200, true);
        font.draw(batch, "--Letter tiles are swiped left or right one at a time. You can also use the shuffle button to shuffle the tiles randomly.\n", helpTextInScreenCoords.x, helpTextInScreenCoords.y - 215, 500, 200, true);
        font.draw(batch, "--There is a 30 point bonus for using all 6 letter tiles in a set.\n", helpTextInScreenCoords.x, helpTextInScreenCoords.y - 265, 500, 200, true);
        font.draw(batch, "--For each set, you have the option to replace two vowels and two consonants. Vowels replace consonants; consonants replace vowels. The first tile from the left is replaced.", helpTextInScreenCoords.x, helpTextInScreenCoords.y - 295, 500, 200, true);
        batch.end();

        // draw button
        viewport.apply(true);
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeType.Filled);

        // do here
        renderer.roundedRect(mainMenuRectangle.x, mainMenuRectangle.y, mainMenuRectangle.width, mainMenuRectangle.height, Constants.BUTTON_RADIUS, Constants.BUTTON_FILL_COLOR);

        renderer.end();

        // draw text on button
        textViewport.apply();
        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();

        // do here
        // draw text on button to go back to main menu
        Vector3 returnButtonInScreenCoords = camera.project(new Vector3(mainMenuRectangle.x, mainMenuRectangle.y, 0));
        font.setColor(Constants.BUTTON_TEXT_COLOR);
        font.getData().setScale(Constants.MESSAGE_FONT_SIZE);
        font.draw(batch, "MAIN MENU", returnButtonInScreenCoords.x + 25f, returnButtonInScreenCoords.y + 40f);

        font.setColor(Color.BLACK);
        font.getData().setScale(1);

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
