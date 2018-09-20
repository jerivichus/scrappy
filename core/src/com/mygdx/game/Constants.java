package com.mygdx.game;


import com.badlogic.gdx.graphics.Color;

/**
 * Created by jackwa on 11/29/17.
 */
public class Constants {

    // in general
    public static final float WORLD_SIZE = 100f;

    // main menu constants
    public static final float MAIN_MENU_BUTTON_WIDTH = 53f;
    public static final float TITLE_POS_X = WORLD_SIZE / 3.5f;
    public static final float TITLE_POS_Y = WORLD_SIZE / 1.2f;


    // settings constants

    // game screen constant
    public static final Color DARK_BROWN = new Color(toRGB(26,13,0));
    public static final Color BUTTON_FILL_COLOR = DARK_BROWN;
    public static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    public static final float BUTTON_RADIUS =  3f;
    public static final float TILE_RADIUS = 2f;
    public static final float BUTTON_WIDTH = 48f;
    public static final float BUTTON_HEIGHT = 15f;

    // score hud
    public static final float SCOREBOARD_XPOS = 5f;
    public static final float SCOREBOARD_TOP_YPOS = WORLD_SIZE / 1.05f;
    public static final float LETTER_COUNT_XPOS = WORLD_SIZE - 25f;
    public static final float LETTER_COUNT_YPOS = WORLD_SIZE / 1.05f;
    public static final int LETTER_COUNT = 105;
    public static final float SCORE_FEEDBACK_XPOS = 5f;
    public static final float SCORE_FEEDBACK_YPOS = WORLD_SIZE / 1.18f;
    public static final Color POSITIVE_COLOR = new Color(toRGB(0,77,0));
    public static final Color NEGATIVE_COLOR = Color.RED;
    public static final float SCOREBOARD_FONT_SIZE = 1.2f;

    // letterbag constants
    public static final float WHATS_LEFT_XPOS = WORLD_SIZE - 25f;
    public static final float WHATS_LEFT_YPOS = WORLD_SIZE / 1.1f;
    public static final float LETTERBAG_ROW1_XPOS = WORLD_SIZE - 40f;
    public static final float LETTERBAG_ROW1_YPOS = WORLD_SIZE / 1.2f;
    public static final float LETTERBAG_FONT_SIZE = 1f;
    public static final float LETTERBAG_OFFSET = 45f;

    // submit button
    public static final float SUBMIT_XPOS = 25f;
    public static final float SUBMIT_YPOS = WORLD_SIZE / 7f;

    // shuffle button
    public static final float SHUFFLE_XPOS = (WORLD_SIZE / 2) + BUTTON_WIDTH / 2;
    public static final float SHUFFLE_YPOS = WORLD_SIZE / 7f;

    // trade button and trade message
    public static final float TRADE_XPOS = 1f;
    public static final float TRADE_YPOS = WORLD_SIZE / 1.7f;
    public static final float TRADE_MESSAGE_XPOS = 1f;
    public static final float TRADE_MESSAGE_YPOS = TRADE_YPOS + Constants.BUTTON_HEIGHT + 5f;


    // font stuff
    public static final float SUBMIT_FONT_SIZE = 1.3f;
    public static final float SUBMIT_TEXT_OFFSET = 8f;
    public static final float VOWEL_TEXT_OFFSET = 5;
    public static final float FONT_SIZE_REF = 480f;


    // tile and tileset dims and style
    public static final float TILE_WIDTH = 14f;
    public static final float TILE_HEIGHT = 14f;
    public static final float TILE_OFFSET = 18f;
    public static final Color STAGE_FILL_COLOR = DARK_BROWN;
    public static final Color TILE_OUTLINE_COLOR = DARK_BROWN;

    public static final float KILL_RADIUS = 4f;
    public static final int KILL_SEGMENTS = 20;
    public static final Color KILL_BG_COLOR = Color.BLACK;
    public static final float KILL_FONT_SIZE = 1.5f;
    public static final float KILL_XOFFSET = -.5f;
    public static final float KILL_YOFFSET = 1.2f;

    // tile font
    public static final float TILE_FONT_SIZE = 2f;
    public static final float TILE_POINT_FONT_SIZE = .9f;
    public static final float POINT_VAL_YOFFSET = 22f;
    public static final float TILE_TEXT_XOFFSET = 3f;
    public static final float TILE_TEXT_YOFFSET = 8f;

    // game data stuff
    public static final int INIT_NUM_TILES = 7;
    public static final int NUM_OF_ROUNDS = 15;

    // lower textviewport stuff
    public static final float XICON = 20f;
    public static final float YICON = 20f;
    public static final float XMESSAGE = 90f;
    public static final float YMESSAGE = 40f;
    public static final float MESSAGE_FONT_SIZE = 1.5f;

    // end screen
    public static final float X_CREDITS = WORLD_SIZE / 3f;
    public static final float Y_CREDITS = WORLD_SIZE / 1.2f;
    public static final float CRED_BUTTON_X = X_CREDITS;
    public static final float CRED_BUTTON_Y = 5f;

    // timer constants
    public static final int STANDARD_TIME_ALLOTTED = 45;


    private static Color toRGB(int r, int g, int b) {
        float RED = r / 255.0f;
        float GREEN = g / 255.0f;
        float BLUE = b / 255.0f;
        return new Color(RED, GREEN, BLUE, 1);
    }



}
