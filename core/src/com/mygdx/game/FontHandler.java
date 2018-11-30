package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by jackwa on 9/14/18.
 */
public class FontHandler {

    private String text;
    private BitmapFont bitmapFont;



    public FontHandler(String file, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        bitmapFont = generator.generateFont(parameter);
        bitmapFont.getData().setLineHeight(20);
        generator.dispose();
    }

    public BitmapFont getBitmapFont() {
        return bitmapFont;
    }
}
