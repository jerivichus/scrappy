package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

/**
 * Created by jackwa on 12/7/17.
 * a tile represents an individual game piece
 * a tile is associated with a single letter
 * tiles are collected in tilesets to play a word
 */
public class Tile implements InputProcessor {

    // instance vars
    private ExtendViewport viewport;
    private Rectangle tileRectangle;
    private Circle killCircle;
    private char letter;
    private Color fillColor;
    private boolean flicking;
    private boolean killed;
    private Vector2 touchCoords;
    private String direction;
    private GameScreen gameScreen;
    private Tileset ts;



    // constructor
    public Tile(float posX, float posY, Color fillColor, ExtendViewport viewport, GameScreen gameScreen, Tileset ts) {
        tileRectangle = new Rectangle(posX, posY, Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
        killCircle = new Circle(tileRectangle.x + Constants.TILE_WIDTH, tileRectangle.y + Constants.TILE_HEIGHT, Constants.KILL_RADIUS);
        letter = genLetter();
        this.fillColor = fillColor;
        this.viewport = viewport;
        this.gameScreen = gameScreen;
        this.ts = ts;
        touchCoords = new Vector2();
        direction = "";
        killed = false;
        flicking = false;
    }

    public Tile(float posX, float posY, Color fillColor, char c, ExtendViewport viewport, GameScreen gameScreen) {
        tileRectangle = new Rectangle(posX, posY, Constants.TILE_WIDTH, Constants.TILE_HEIGHT);
        killCircle = new Circle(tileRectangle.x + Constants.TILE_WIDTH, tileRectangle.y + Constants.TILE_HEIGHT, Constants.KILL_RADIUS);
        letter = c;
        this.fillColor = fillColor;
        this.viewport = viewport;
        this.gameScreen = gameScreen;
        touchCoords = new Vector2();
        direction = "";;
        killed = false;
        flicking = false;
    }

    // public methods
    public void render(MyShapeRenderer renderer) {
        // render stuff
        renderer.setColor(fillColor);
        renderer.roundedRect(tileRectangle.x, tileRectangle.y, tileRectangle.width, tileRectangle.height, Constants.TILE_RADIUS, Constants.TILE_OUTLINE_COLOR);
        renderer.setColor(Constants.KILL_BG_COLOR);
        renderer.circle(killCircle.x, killCircle.y, killCircle.radius, Constants.KILL_SEGMENTS);

    }

    public boolean update(float delta) {

        if (killed) {
            int index = ts.getTiles().indexOf(this);

            ts.getTiles().remove(this);

            ts.updatePositions();
        }


        // if a tile needs to be moved
        if (shiftTile(ts)) {
            return true;
        }
        return false;

    }

    public boolean isFlicking() {
        return flicking;
    }

    public String getDirection() {
        return direction;
    }

    public char getLetter() {
        return letter;
    }



    public void setLetter(char c) {
        letter = c;
    }

    public Rectangle getTileRectangle() {
        return tileRectangle;
    }

    public Circle getKillCircle() {
        return killCircle;
    }

    // private methods

    // temporary method to return a random letter
    private char genLetter() {
        if (letter != ' ') {
             char c = LetterBag.getRandomChar();
            return c;
        } else {
            return ' ';
        }
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
        touchCoords = viewport.unproject(new Vector2(screenX, screenY));
        if (killCircle.contains(touchCoords)) {
            killed = true;

        }
        if (tileRectangle.contains(touchCoords)) {
            flicking = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        flicking = false;
        direction = "";
        return false;


    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (!flicking) { return false; }

        Vector2 newTouch = viewport.unproject(new Vector2(screenX, screenY));
        // delta will now hold the difference between the last and the current touch positions
        // delta.x > 0 means the touch moved to the right, delta.x < 0 means a move to the left
        Vector2 delta = newTouch.cpy().sub(touchCoords);
        System.out.println(delta);
        if (delta.x > 0) {
            // moving right
            direction = "right";
        } else if (delta.x < 0) {
            // moving left
            direction = "left";
        }

        touchCoords = newTouch;
        return false;
    }


    public boolean shiftTile(Tileset ts) {

        // update stuff
        if (flicking) {
            // switch with next door
            int setLength = ts.getTiles().size();
            for (int i = 0; i < setLength; i++) {
                if (this == ts.getTile(i)) {
                    if (direction.equals("right")) {
                        if (i < setLength - 1) {
                            ts.switchTiles(i, i + 1);

                        } else if (i == setLength - 1) {
                            ts.switchTiles(i, 0);
                        }
                        flicking = false;
                        direction = "";
                        break;

                    }
                    // tswipe left
                    else if (direction.equals("left")) {
                        if (i > 0) {
                            ts.switchTiles(i, i-1);
                        } else {
                            ts.switchTiles(i, ts.getTiles().size()-1);
                        }
                        flicking = false;
                        direction = "";
                        break;
                    }
                }
            }
        return true;
        }
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
