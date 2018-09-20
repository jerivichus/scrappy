package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
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

/**
 * Created by jackwa on 11/29/17.
 */
public class MainMenuScreen extends InputAdapter  implements Screen {

    private Game game;

    // viewports
    private ExtendViewport viewport;
    private ScreenViewport textViewport;

    // drawing tools and shapes
    private MyShapeRenderer renderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private BitmapFont headerFont;

    private Camera camera;
    private Camera textCamera;

    private Rectangle playRectangle;
    private Rectangle untimedRectangle;
    private Rectangle helpRectangle;

    private TextureRegion backgroundTexture;

    // init
    public MainMenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {

        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        camera = viewport.getCamera();
        renderer = new MyShapeRenderer();
        renderer.setAutoShapeType(true);
        batch = new SpriteBatch();
        headerFont = new FontHandler("fonts/FFF_Tusj.ttf", 60).getBitmapFont();
        font = new FontHandler("fonts/Eczar-Medium.ttf", 15).getBitmapFont();
        font.setColor(Constants.BUTTON_TEXT_COLOR);
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Texture bgTexture = new Texture(Gdx.files.internal("images/purty_wood.png"));
        bgTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        backgroundTexture = new TextureRegion(bgTexture,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        playRectangle = new Rectangle(Constants.WORLD_SIZE / 2.5f, Constants.WORLD_SIZE / 2f, Constants.MAIN_MENU_BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        untimedRectangle = new Rectangle(Constants.WORLD_SIZE / 2.5f, Constants.WORLD_SIZE / 3.2f, Constants.MAIN_MENU_BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        helpRectangle = new Rectangle(Constants.WORLD_SIZE / 2.5f, Constants.WORLD_SIZE / 7.7f, Constants.MAIN_MENU_BUTTON_WIDTH, Constants.BUTTON_HEIGHT);

        // text it up
        textViewport = new ScreenViewport();
        textCamera = textViewport.getCamera();

        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render(float delta) {

        // apply the general viewport
        viewport.apply(true);

        // set projection matrix to combined
        renderer.setProjectionMatrix(camera.combined);

        // clear the screen
        Gdx.gl.glClearColor( 1, 1, 1, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        // draw background
        batch.begin();
        batch.draw(backgroundTexture, 0, 0);
        batch.end();


        // draw buttons
        renderer.begin(ShapeType.Filled);
        renderer.setColor(Constants.BUTTON_FILL_COLOR);

        renderer.roundedRect(playRectangle.x, playRectangle.y, playRectangle.width, playRectangle.height, Constants.BUTTON_RADIUS, Constants.BUTTON_FILL_COLOR);
        renderer.roundedRect(untimedRectangle.x, untimedRectangle.y, untimedRectangle.width, untimedRectangle.height, Constants.BUTTON_RADIUS, Constants.BUTTON_FILL_COLOR);
        renderer.roundedRect(helpRectangle.x, helpRectangle.y, helpRectangle.width, helpRectangle.height, Constants.BUTTON_RADIUS, Constants.BUTTON_FILL_COLOR);

        renderer.end();

        // apply the text viewport
        textViewport.apply();
        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();

        font.getData().setScale(Constants.SUBMIT_FONT_SIZE);

        Vector3 playRectInScreenCoords = camera.project(new Vector3(playRectangle.x + Constants.SUBMIT_TEXT_OFFSET, playRectangle.y + Constants.SUBMIT_TEXT_OFFSET, 0));
        font.draw(batch, "New Timed Game", playRectInScreenCoords.x, playRectInScreenCoords.y);
        Vector3 unTimedRectInScreenCoords = camera.project(new Vector3(untimedRectangle.x + Constants.SUBMIT_TEXT_OFFSET, untimedRectangle.y + Constants.SUBMIT_TEXT_OFFSET, 0));
        font.draw(batch, "New Untimed Game", unTimedRectInScreenCoords.x, unTimedRectInScreenCoords.y);
        Vector3 helpRectInScreenCoords = camera.project(new Vector3(helpRectangle.x + Constants.SUBMIT_TEXT_OFFSET, helpRectangle.y + Constants.SUBMIT_TEXT_OFFSET, 0));
        font.draw(batch, "How To Play", helpRectInScreenCoords.x, helpRectInScreenCoords.y);

        // draw title

        Vector3 titlePosInScreenCoords = camera.project(new Vector3(Constants.TITLE_POS_X, Constants.TITLE_POS_Y, 0));
        font.getData().setScale(3f);
        font.setColor(Color.BLACK);
        headerFont.setColor(Color.BLACK);
        headerFont.draw(batch, "Scrappy Word", titlePosInScreenCoords.x, titlePosInScreenCoords.y);
        font.getData().setScale(Constants.SUBMIT_FONT_SIZE);
        font.setColor(Constants.BUTTON_TEXT_COLOR);



        // end batch
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

        // play Rectange listener
        if (playRectangle.contains(touchPoint)) {
            game.setScreen(new GameScreen(game, 0));
        }

        // untimed rectangle listener
        if (untimedRectangle.contains(touchPoint)) {
            // untimed game is '1'
            game.setScreen(new GameScreen(game, 1));
        }

        // how to rectangle listener
        if (helpRectangle.contains(touchPoint)) {
            game.setScreen(new HelpScreen(game));
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
