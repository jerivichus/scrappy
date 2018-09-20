package com.mygdx.game;

import java.util.HashMap;

/**
 * Created by jackwa on 2/14/18.
 */
public class ScoringTable {

    private HashMap<String,Integer> table;

    public ScoringTable() {
        initScoringTable();
    }

    public void initScoringTable() {

        table = new HashMap<String,Integer>();

        // five pointers
        table.put("A", 5);
        table.put("E", 5);
        table.put("I", 5);
        table.put("L", 5);
        table.put("O", 5);
        table.put("R", 5);
        table.put("S", 5);
        table.put("T", 5);
        table.put("U", 5);


        // ten pointers
        table.put("B", 10);
        table.put("C", 10);
        table.put("D", 10);
        table.put("F", 10);
        table.put("G", 10);
        table.put("H", 10);
        table.put("M", 10);
        table.put("N", 10);
        table.put("P", 10);
        table.put("W", 10);
        table.put("Y", 10);


        // fifteen pointers
        table.put("J", 15);
        table.put("K", 15);
        table.put("V", 15);
        table.put("X", 15);


        // twenty pointers
        table.put("Q", 20);
        table.put("Z", 20);

    }

    public int getValue(String key) {
        return table.get(key);

    }

    public int scoreWord(String word) {

        int thisScore = 0;

        // first add positive score
        for (int i = 0; i < word.length(); i++) {
            String letter = Character.toString(word.charAt(i));
            int toAdd = table.get(letter);
            thisScore += toAdd;
        }

        // add bonus for using every tile
        if (word.length()==Constants.INIT_NUM_TILES - 1) {
            thisScore += 30;
            System.out.println("You got a thirty point bonus for playing a six letter word!");
        } else if (word.length()==Constants.INIT_NUM_TILES) {
            thisScore += 40;
            System.out.println("You got a forty point bonus for playing a seven letter word!");
        }

        return thisScore;

    }
}
