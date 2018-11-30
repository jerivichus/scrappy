package com.mygdx.game;

import java.util.HashMap;

/**
 * Created by jackwa on 2/14/18.
 */
public class ScoringTable {

    private HashMap<String,Integer> table;
    private HashMap<String, Integer> bonuses;

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


        // init bonuses
        bonuses = new HashMap<String, Integer>();
        bonuses.put("6 Letter Word", 0);
        bonuses.put("7 Letter Word", 0);
        bonuses.put("Singular S", 0);
        bonuses.put("All Words", 0);


    }

    public int getValue(String key) {
        return table.get(key);

    }

    public void setBonusValue(String key, int val) {
        bonuses.put(key, val);
    }

    public HashMap<String,Integer> getBonuses() {
        return bonuses;
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
        thisScore = handleBonuses(word, thisScore);

        return thisScore;

    }

    private int handleBonuses(String word, int score) {

        // add five point bonus for using 's' if the word doesn't end with s
        if (word.contains("S")) {
            if (word.charAt(word.length()-1) != ('S')) {
                score += 5;
                bonuses.put("Singular S", bonuses.get("Singular S") + 1);
            }
        }

        // add bonus for using every tile
        if (word.length()==Constants.INIT_NUM_TILES - 1) {
            score += 30;
            bonuses.put("6 Letter Word", bonuses.get("6 Letter Word") + 1);
        } else if (word.length()==Constants.INIT_NUM_TILES) {
            score += 40;
            bonuses.put("7 Letter Word", bonuses.get("7 Letter Word") + 1);
        }
        return score;

    }
}
