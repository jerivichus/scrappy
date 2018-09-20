package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jackwa on 11/29/17.
 * Created by jackwa on 11/29/17.
 */
public class GameScreen implements Screen, InputProcessor {

    // instance variables
    private Game game;

    // viewports
    private ExtendViewport viewport;
    private ScreenViewport textViewport;

    // background
    private TextureRegion backgroundTexture;

    // drawing tools and shapes
    private MyShapeRenderer renderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private Rectangle submitRectangle;
    private Rectangle tradeRectangle;
    private Rectangle shuffleRectangle;
    private int tradeStock;
    private Camera camera;
    private Camera textCamera;
    private InputMultiplexer inputMP;

    // game logic stuff
    private int playType;
    private Tileset stageSet;
    private int initNumTiles;
    private com.badlogic.gdx.graphics.Texture yayboi;
    private boolean yayboiDisplay;
    private Texture nodice;
    private boolean nodiceDisplay;
    private String theLastWord;
    private boolean tradeBegotten;
    private boolean cannotTrade;
    private boolean noNonWords;
    private int rounds;
    private int score;
    private ScoringTable scoringTable;
    private Preferences prefs;
    private int letterCount;
    private List<String> wordsPlayed;
    private boolean showFeedback;
    private int scoreToAdd;
    private CountDown timer;

    public GameScreen(Game game, int playType) {
        this.game = game;
        this.playType = playType;
        if (playType == 0) {
            initTimer();
        }
    }

    // test tiles
    @Override
    public void show() {

        // read word list
        MyWordChecker.readWordStringBuilder();

        // init game data stuff
        initNumTiles = Constants.INIT_NUM_TILES;

        // initialize viewport and rendering tools
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        camera = viewport.getCamera();
        textViewport = new ScreenViewport();
        textCamera = textViewport.getCamera();
        renderer = new MyShapeRenderer();
        renderer.setAutoShapeType(true);
        batch = new SpriteBatch();
        font = new FontHandler("fonts/Eczar-Medium.ttf",15).getBitmapFont();
        font.setColor(Constants.BUTTON_TEXT_COLOR);
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Texture bgTexture = new Texture(Gdx.files.internal("images/purty_wood.png"));
        bgTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        backgroundTexture = new TextureRegion(bgTexture,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        submitRectangle = new Rectangle(Constants.SUBMIT_XPOS, Constants.SUBMIT_YPOS, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        tradeRectangle = new Rectangle(Constants.TRADE_XPOS, Constants.TRADE_YPOS, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        shuffleRectangle = new Rectangle(Constants.SHUFFLE_XPOS, Constants.SHUFFLE_YPOS, Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        tradeBegotten = false;
        cannotTrade = false;
        showFeedback = false;
        tradeStock = 4;
        noNonWords = true;
        letterCount = Constants.LETTER_COUNT;;

        // set up letter distribution
        LetterBag.initLetterMap();
        scoringTable = new ScoringTable();

        stageSet = new Tileset(initNumTiles, renderer, Constants.STAGE_FILL_COLOR, Constants.WORLD_SIZE / 3, viewport, this);
        inputMP = new InputMultiplexer();

        // add input processor for each tile
        addTileSetInputProcessors();

        // add this GameScreen class to the multiplexer so we can registere submit button clicks
        inputMP.addProcessor(this);

        Gdx.input.setInputProcessor(inputMP);

        yayboi = new Texture(Gdx.files.internal("images/yayboi.png"));
        yayboiDisplay = false;
        nodice = new Texture(Gdx.files.internal("images/nodice.png"));
        nodiceDisplay = false;

        theLastWord = "";
        rounds = 0;

        prefs = Gdx.app.getPreferences("My Preferences");

        wordsPlayed = new ArrayList<String>();



    }

    @Override
    public void render(float delta) {


        // block trades on last round
        if (letterCount == 6) {
            tradeBegotten = true;
        }

        if (stageSet.update(delta)) {
            cannotTrade = false;
        }

        // apply the general viewport
        viewport.apply(true);

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

        if (!tradeBegotten) {
            renderer.roundedRect(tradeRectangle.x, tradeRectangle.y, tradeRectangle.width, tradeRectangle.height, Constants.BUTTON_RADIUS, Constants.BUTTON_FILL_COLOR);
            renderer.setColor(Constants.DARK_BROWN);
            // the commented out code below made a straight edged rect that could have a border around it
            //renderer.rectLine(tradeRectangle.x, tradeRectangle.y, tradeRectangle.x + tradeRectangle.width, tradeRectangle.y, 1.5f);
            //renderer.rectLine(tradeRectangle.x, tradeRectangle.y + tradeRectangle.height, tradeRectangle.x + tradeRectangle.width, tradeRectangle.y + tradeRectangle.height, 1.5f);
            //renderer.rectLine(tradeRectangle.x + .7f, tradeRectangle.y, tradeRectangle.x + .7f, tradeRectangle.y + tradeRectangle.height, 1.5f);
            //renderer.rectLine(tradeRectangle.x + tradeRectangle.width - .75f, tradeRectangle.y, tradeRectangle.x + tradeRectangle.width - .75f, tradeRectangle.y + tradeRectangle.height, 1.5f);
            renderer.rectLine(tradeRectangle.x + (tradeRectangle.width / 5), tradeRectangle.y, tradeRectangle.x + (tradeRectangle.width / 5), (Constants.WORLD_SIZE / 3) + Constants.TILE_HEIGHT, 1.5f);
        } else {
            renderer.setColor(Color.LIGHT_GRAY);
            renderer.roundedRect(tradeRectangle.x, tradeRectangle.y, tradeRectangle.width, tradeRectangle.height, Constants.BUTTON_RADIUS, Color.LIGHT_GRAY);

        }


        // reset color
        renderer.setColor(Constants.BUTTON_FILL_COLOR);
        
        renderer.setColor(Constants.BUTTON_FILL_COLOR);
        renderer.roundedRect(submitRectangle.x, submitRectangle.y, submitRectangle.width, submitRectangle.height, Constants.BUTTON_RADIUS, Constants.BUTTON_FILL_COLOR);

        renderer.roundedRect(shuffleRectangle.x, shuffleRectangle.y, shuffleRectangle.width, shuffleRectangle.height, Constants.BUTTON_RADIUS, Constants.BUTTON_FILL_COLOR);

        // render other stuff
        stageSet.render(renderer);

        renderer.end();

        // apply the text viewport
        textViewport.apply();
        batch.setProjectionMatrix(textCamera.combined);
        batch.begin();

        // display status of last played word
        if (yayboiDisplay) {
            batch.draw(yayboi, Constants.XICON, Constants.YICON);
            font.getData().setScale(Constants.MESSAGE_FONT_SIZE);
            font.setColor(Color.BLACK);
            font.draw(batch, "Your last play was: " + theLastWord + ". Yep, that's a word!", Constants.XMESSAGE, Constants.YMESSAGE);
        } else if (nodiceDisplay) {
            font.setColor(Color.BLACK);
            font.getData().setScale(Constants.SCOREBOARD_FONT_SIZE);
            batch.draw(nodice, Constants.XICON, Constants.YICON);
            font.draw(batch, "Your last play was: " + theLastWord + ". Sorry, that isn't a word.", Constants.XMESSAGE, Constants.YMESSAGE);
        }

        // draw the score stuff
        Vector3 scoreVector = camera.project(new Vector3(Constants.SCOREBOARD_XPOS, Constants.SCOREBOARD_TOP_YPOS, 0));
        font.getData().setScale(Constants.SCOREBOARD_FONT_SIZE);
        font.setColor(Color.BLACK);
        String topScoreAsString;
        if (playType==0) {
            topScoreAsString = Integer.toString(prefs.getInteger("topTimedScore", 0)) + " (timed)";
        } else {
            topScoreAsString = Integer.toString(prefs.getInteger("topScore", 0)) + " (untimed)";
        }
        font.draw(batch, "Top Score: " + topScoreAsString, scoreVector.x, scoreVector.y);
        String currScoreAsString = Integer.toString(score);
        font.draw(batch, "Your Score: " + currScoreAsString, scoreVector.x, scoreVector.y - 25);
        Vector3 countVector = camera.project(new Vector3(Constants.LETTER_COUNT_XPOS, Constants.LETTER_COUNT_YPOS, 0));
        font.draw(batch, "Total tiles remaining: " + Integer.toString(letterCount), countVector.x, countVector.y);

        // handle timed game display and logic
        if (playType == 0 && rounds <= Constants.NUM_OF_ROUNDS) {
            // display
            int timeLeft = timer.getInterval();
            if (timeLeft <= 5) {
                font.setColor(Color.RED);
            }
            font.draw(batch, "Time left: " + timeLeft, scoreVector.x, scoreVector.y - 50);
            font.setColor(Color.BLACK);

            // logic
            if (timeLeft <= 0) {

                // reboot
                String theWord = getCurrWord();
                submitWord(theWord);


            }
        }

        // reset font color for button text
        font.setColor(Constants.BUTTON_TEXT_COLOR);

        // draw the submit button text
        font.getData().setScale(Constants.SUBMIT_FONT_SIZE);
        Vector3 rectInScreenCords = camera.project(new Vector3(submitRectangle.x + Constants.SUBMIT_TEXT_OFFSET, submitRectangle.y + Constants.SUBMIT_TEXT_OFFSET, 0));
        font.draw(batch, "PLAY WORD", rectInScreenCords.x, rectInScreenCords.y);
        Vector3 vowelRectInScreenCoords = camera.project(new Vector3(tradeRectangle.x + Constants.VOWEL_TEXT_OFFSET, tradeRectangle.y + Constants.SUBMIT_TEXT_OFFSET, 0));
        font.draw(batch, "TRADE THIS TILE (" + tradeStock + ")", vowelRectInScreenCoords.x, vowelRectInScreenCoords.y);
        Vector3 shuffleRectInScreenCoords = camera.project(new Vector3(shuffleRectangle.x + Constants.SUBMIT_TEXT_OFFSET, shuffleRectangle.y + Constants.SUBMIT_TEXT_OFFSET, 0));
        font.draw(batch, "SHUFFLE", shuffleRectInScreenCoords.x, shuffleRectInScreenCoords.y);


        font.setColor(Constants.BUTTON_TEXT_COLOR);
        font.getData().setScale(Constants.SUBMIT_FONT_SIZE);

        // display the last score if applicable
        if (showFeedback) {
            Vector3 feedbackVector = camera.project(new Vector3(Constants.SCORE_FEEDBACK_XPOS, Constants.SCORE_FEEDBACK_YPOS, 0));
            if (scoreToAdd >= 0) {
                font.setColor(Constants.POSITIVE_COLOR);
                font.draw(batch, "Last play: +" + scoreToAdd, feedbackVector.x, feedbackVector.y);
            }
            // reset font color
            font.setColor(Constants.BUTTON_TEXT_COLOR);
        }

        // draw the letterbag in two rows
        font.setColor(Color.BLACK);
        Vector3 whatsLeftVector = camera.project(new Vector3(Constants.WHATS_LEFT_XPOS, Constants.WHATS_LEFT_YPOS, 0));
        font.draw(batch, "Letters left in bag :", whatsLeftVector.x, whatsLeftVector.y);
        font.getData().setScale(Constants.LETTERBAG_FONT_SIZE);
        Vector3 letterBagRowOne = camera.project(new Vector3(Constants.LETTERBAG_ROW1_XPOS, Constants.LETTERBAG_ROW1_YPOS, 0));
        Vector3 letterBagRowTwo = camera.project(new Vector3(Constants.LETTERBAG_ROW1_XPOS, Constants.LETTERBAG_ROW1_YPOS - 5f, 0));
        Vector3 letterBagRowThree = camera.project(new Vector3(Constants.LETTERBAG_ROW1_XPOS, Constants.LETTERBAG_ROW1_YPOS - 10f, 0));
        Vector3 letterBagRowFour = camera.project(new Vector3(Constants.LETTERBAG_ROW1_XPOS, Constants.LETTERBAG_ROW1_YPOS - 15f, 0));
        Vector3 letterBagRowFive = camera.project(new Vector3(Constants.LETTERBAG_ROW1_XPOS, Constants.LETTERBAG_ROW1_YPOS - 20f, 0));

        // row one
        int iterator = 0;
        for (Map.Entry<Character,Integer> entry : LetterBag.lmap.entrySet()) {

            if (iterator < 6) {
                font.draw(batch, entry.getKey() + " : " + entry.getValue() + "  ", letterBagRowOne.x + ((iterator % 6) * Constants.LETTERBAG_OFFSET), letterBagRowOne.y);
            } else if (iterator >= 6 && iterator < 12) {
                font.draw(batch, entry.getKey() + " : " + entry.getValue() + "  ", letterBagRowTwo.x + ((iterator % 6) * Constants.LETTERBAG_OFFSET), letterBagRowTwo.y);
            } else if (iterator >= 12 &&  iterator < 18) {
                font.draw(batch, entry.getKey() + " : " + entry.getValue() + "  ", letterBagRowThree.x + ((iterator % 6) * Constants.LETTERBAG_OFFSET), letterBagRowThree.y);
            } else if (iterator >= 18 && iterator < 24 ){
                font.draw(batch, entry.getKey() + " : " + entry.getValue() + "  ", letterBagRowFour.x + ((iterator % 6) * Constants.LETTERBAG_OFFSET), letterBagRowFour.y);
            } else {
                font.draw(batch, entry.getKey() + " : " + entry.getValue() + "  ", letterBagRowFive.x + ((iterator % 6) * Constants.LETTERBAG_OFFSET), letterBagRowFive.y);
            }
            iterator++;
        }
        font.setColor(Constants.BUTTON_TEXT_COLOR);
        font.getData().setScale(Constants.SUBMIT_FONT_SIZE);

        // display the cannot trade letter message if applicable
        if (cannotTrade) {
            Vector3 cannotTradeVector = camera.project(new Vector3(Constants.TRADE_MESSAGE_XPOS, Constants.TRADE_MESSAGE_YPOS,0));
            font.setColor((Constants.NEGATIVE_COLOR));
            String letterTriedToTrade = Character.toString(stageSet.getTiles().get(0).getLetter());
            if (LetterBag.isVowel(letterTriedToTrade)) {
                font.draw(batch, "Cannot trade: no consonants left", cannotTradeVector.x, cannotTradeVector.y);
            } else {
                font.draw(batch, "Cannot trade: no vowels left", cannotTradeVector.x, cannotTradeVector.y);
            }
            // reset font color
            font.setColor(Constants.BUTTON_TEXT_COLOR);
        }


        // draw the letters and point values on the tiles
        drawLetters();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        textViewport.update(width, height, true);
        backgroundTexture.setRegionWidth(width);
        backgroundTexture.setRegionHeight(height);
        font.getData().setScale(Math.min(width, height) / Constants.FONT_SIZE_REF);

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        renderer.dispose();
        batch.dispose();
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
        if (submitRectangle.contains(touchPoint)) {
            // build word
            String theWord = getCurrWord();
            submitWord(theWord);
            cannotTrade = false;
        } else if (tradeRectangle.contains(touchPoint) && !tradeBegotten) {
            if (stageSet.replaceLetter()) {
                cannotTrade = false;
                tradeStock--;
                if (tradeStock == 0) {
                    tradeBegotten = true;
                }
            } else {
                // print message that trade can't happen
                cannotTrade = true;

            }
        }  else if (shuffleRectangle.contains(touchPoint)) {
            stageSet.shuffle();

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

    public void drawLetters() {
        font.getData().setScale(Constants.TILE_FONT_SIZE);
        ArrayList<Tile> tiles = stageSet.getTiles();
        for (int i = 0; i < tiles.size(); i++) {
            Tile thisTile = tiles.get(i);
            Vector3 tileRectInScreenCords = camera.project(new Vector3(thisTile.getTileRectangle().x + Constants.TILE_TEXT_XOFFSET, thisTile.getTileRectangle().y + Constants.TILE_TEXT_YOFFSET, 0));
            Vector3 circleInScreenCoords = camera.project(new Vector3(thisTile.getKillCircle().x + Constants.KILL_XOFFSET, thisTile.getKillCircle().y + Constants.KILL_YOFFSET, 0));
            font.getData().setScale(Constants.TILE_FONT_SIZE);
            font.draw(batch, Character.toString(thisTile.getLetter()), tileRectInScreenCords.x, tileRectInScreenCords.y);
            font.getData().setScale(Constants.KILL_FONT_SIZE);
            font.draw(batch, "x", circleInScreenCoords.x, circleInScreenCoords.y);

            // draw point value
            font.getData().setScale(Constants.TILE_POINT_FONT_SIZE);
            int value = scoringTable.getValue(Character.toString(thisTile.getLetter()));
            String valueAsString = Integer.toString(value);
            font.draw(batch, "+" + valueAsString, tileRectInScreenCords.x, tileRectInScreenCords.y + Constants.POINT_VAL_YOFFSET);

        }

    }

    /*
    * this method gets the current word
     */
    private String getCurrWord() {
        String theWord = "";
        for (int i = 0; i < stageSet.getTiles().size(); i++) {
            theWord = theWord + stageSet.getTile(i).getLetter();
        }
        return theWord;
    }

    /*
    * submit word button callback method
     */
    private void submitWord(String word) {
        rounds++;
        letterCount = letterCount - Constants.INIT_NUM_TILES;
        tradeStock = 4;
        System.out.println("The word is: " + word);
        theLastWord = word;
        boolean wellisIt = MyWordChecker.isItAWord(word);
        if (wellisIt) {
            System.out.println("Yay! That's a word!");
            yayboiDisplay = true;
            nodiceDisplay = false;
            scoreToAdd = scoringTable.scoreWord(word);
            showFeedback = true;
            score += scoreToAdd;
            wordsPlayed.add(word);

        } else {
            System.out.println("Sorry, that's not a word in my dictionary");
            showFeedback = true;
            nodiceDisplay = true;
            yayboiDisplay = false;
            noNonWords = false;
        }

        if (rounds < Constants.NUM_OF_ROUNDS) {
            stageSet = new Tileset(initNumTiles, renderer, Constants.STAGE_FILL_COLOR, Constants.WORLD_SIZE / 3, viewport, GameScreen.this);
            addTileSetInputProcessors();
            tradeBegotten = false;

            // reset timer
            if (playType == 0) {
                timer.cancel();
                timer = new CountDown(Constants.STANDARD_TIME_ALLOTTED);
                timer.countDown();

            }
        } else {

            // it's the end of the game

            // add 30 point bonus to score and register 'All Words' bonus in bonuses hashmap
            if (noNonWords) {
                score += 30;
                scoringTable.setBonusValue("All Words", 1);
            }

            if (playType==0) {
                int topScore = prefs.getInteger("topTimedScore", 0);
                if (score > topScore) {
                    prefs.putInteger("topTimedScore", score);
                    prefs.flush();
                }
            } else {
                int topScore = prefs.getInteger("topScore", 0);
                if (score > topScore) {
                    prefs.putInteger("topScore", score);
                    prefs.flush();
                }
            }

            // cancel timer just in case
            if (timer != null) {
                timer.cancel();
            }

            // set to endscreen
            HashMap bonuses = scoringTable.getBonuses();
            game.setScreen(new EndScreen(wordsPlayed, score, playType, bonuses, game));
        }



        // for now just print the score to the console
        System.out.println("The score is " + score);

    }


    /*
    *  add tileset input processors to input multiplexer
     */
    private void addTileSetInputProcessors() {
        for (int i = 0; i < stageSet.getTiles().size(); i++) {
            inputMP.addProcessor(stageSet.getTile(i));
        }
    }

    /*
    * initTimer if playing timed game
     */
    private void initTimer() {

        timer = new CountDown(Constants.STANDARD_TIME_ALLOTTED);
        timer.countDown();
    }

}
