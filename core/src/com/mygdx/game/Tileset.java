package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jackwa on 12/7/17.
 */
public class Tileset {


    // fields
    private int numTiles;
    private ArrayList<Tile> tiles;
    private Color fillColor;
    private float yPos;
    private ExtendViewport viewport;
    private GameScreen gameScreen;
    private float setWidth;

    // constructor
    public Tileset(int numTiles, MyShapeRenderer renderer, Color fillColor, float yPos, ExtendViewport viewport, GameScreen gameScreen) {
        this.numTiles = numTiles;
        tiles = new ArrayList<Tile>();
        this.fillColor = fillColor;
        this.yPos = yPos;;
        this.viewport = viewport;
        this.gameScreen = gameScreen;
        initTileset();
    }

    private void initTileset() {
        setWidth = (Constants.TILE_OFFSET * numTiles) - 14;
        if (numTiles == 6) {
            setWidth = setWidth - (numTiles * 3.2f);
        }
        else if (numTiles == 7) {
            setWidth = setWidth - (numTiles * 2.5f);
        }
        for (int i = 0; i < numTiles; i++) {
            tiles.add(new Tile(Constants.WORLD_SIZE - (setWidth)  + (i * Constants.TILE_OFFSET), yPos, fillColor, viewport, gameScreen, this));
        }

    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public Tile getTile(int index) {
        return tiles.get(index);
    }

    public float getSetWidth() {
        return setWidth;
    }

    // methods
    public void render(MyShapeRenderer renderer) {
        for (Tile t : tiles) {
            t.render(renderer);
        }

    }

    public boolean update(float delta) {
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).update(delta)) {
                return true;
            }

        }
        return false;
    }

    public int getSize() {
        return tiles.size();
    }

    public void shuffle() {

        Collections.shuffle(tiles);
       updatePositions();


    }

    public void updatePositions() {
        for (int i = 0; i < tiles.size(); i++) {
            tiles.get(i).getTileRectangle().x = Constants.WORLD_SIZE - (setWidth)  + (i * Constants.TILE_OFFSET);
            tiles.get(i).getKillCircle().x = Constants.WORLD_SIZE - (setWidth)  + (i * Constants.TILE_OFFSET) + Constants.TILE_WIDTH;
        }

    }

    public void switchTiles(int i, int j) {
        Collections.swap(tiles, i, j);
        float tempRect = tiles.get(i).getTileRectangle().x;
        float tempCirc = tiles.get(i).getKillCircle().x;
        tiles.get(i).getTileRectangle().x = tiles.get(j).getTileRectangle().x;
        tiles.get(i).getKillCircle().x = tiles.get(j).getKillCircle().x;
        tiles.get(j).getTileRectangle().x = tempRect;
        tiles.get(j).getKillCircle().x = tempCirc;
    }

    public boolean replaceLetter() {
        char cIn = tiles.get(0).getLetter();
        char cOut = LetterBag.tradeLetter(cIn);
        tiles.get(0).setLetter(cOut);
        // if cIn was returned back because no trade was possible, return false
        if (cIn == cOut) {
            return false;
        } else {
            // return true if the char could really be traded in LetterBag.tradeLetter method
            return true;
        }
    }
}
