package com.mygdx.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by jackwa on 1/3/18.
 */
public class LetterBag {

    public static HashMap<Character, Integer> lmap;

    public static HashMap initLetterMap() {

        lmap = new HashMap();

        lmap.put('A', 10);
        lmap.put('B', 3);
        lmap.put('C', 2);
        lmap.put('D', 4);
        lmap.put('E', 10);
        lmap.put('F', 2);
        lmap.put('G', 3);
        lmap.put('H', 3);
        lmap.put('I', 8);
        lmap.put('J', 1);
        lmap.put('K', 2);
        lmap.put('L', 5);
        lmap.put('M', 3);
        lmap.put('N', 5);
        lmap.put('O', 9);
        lmap.put('P', 2);
        lmap.put('Q', 1);
        lmap.put('R', 7);
        lmap.put('S', 5);
        lmap.put('T', 7);
        lmap.put('U', 5);
        lmap.put('V', 2);
        lmap.put('W', 2);
        lmap.put('X', 1);
        lmap.put('Y', 2);
        lmap.put('Z', 1);

        return lmap;

    }

    // return a random letter
    public static char getRandomChar() {
        List<Character> keysAsArr = new ArrayList<Character>(lmap.keySet());
        if (keysAsArr.size() > 0) {
            Random r = new Random();
            char key = keysAsArr.get(r.nextInt(keysAsArr.size()));
            updateStock(key);
            return key;
        } else {
            return ' ';
        }
    }

    public static char getRandomChar(int type) {

        List<Character> keysAsArr = new ArrayList<Character>(lmap.keySet());
        // 0 is vowel, 1 is consonant
        if (keysAsArr.size() > 0) {
            Random r = new Random();
            if (type == 0) {

                // init and populate vowelKeys
                List<Character> vowelKeys = new ArrayList<Character>();
                for (int i = 0; i < keysAsArr.size(); i++) {
                    Character thisC = keysAsArr.get(i);
                    if (isVowel(Character.toString(thisC))) {
                        vowelKeys.add(thisC);
                    }
                }

                // if there are vowels left
                if (vowelKeys.size() > 0) {
                    char key = vowelKeys.get(r.nextInt(vowelKeys.size()));
                    updateStock(key);
                    return key;
                }

            } else {

                // type == 1, so get a char instead
                List<Character> charKeys = new ArrayList<Character>();
                for (int i = 0; i < keysAsArr.size(); i++) {
                    Character thisC = keysAsArr.get(i);
                    if (!isVowel(Character.toString(thisC))) {
                        charKeys.add(thisC);
                    }

                    // if there are chars left
                    if (charKeys.size() > 0) {
                        char key = charKeys.get(r.nextInt(charKeys.size()));
                        updateStock(key);
                        return key;
                    }
                }
            }
        }
        // if no letter could be found, return blank
        return ' ';
    }



    // trade for a random vowel
    public static char tradeLetter(char cIn) {

        String letter = Character.toString(cIn);
        Character cOut = ' ';
        // if cIn is vowel, get a consonant
        if (isVowel(letter)) {

            cOut = getRandomChar(1);
        }
        // otherwise, get a vowel
        else {

            cOut = getRandomChar(0);
        }

        // if cOut returned a real char, put cIn back in bag and return cOut...if cOut still blank, then return cIn
        if (cOut != ' ') {
            if (lmap.containsKey(cIn)) {
                int cInCurrStock = (Integer) lmap.get(cIn);
                lmap.put(cIn, cInCurrStock + 1);
            } else {
                lmap.put(cIn, 1);
            }
            return cOut;
        } else {
            return cIn;
        }
    }

    public static boolean isVowel(String letter) {
        if (("AEIOU".indexOf(letter) >= 0)) {
            return true;
        }
        return false;
    }

    public static void updateStock(char key) {

        int stock = (Integer) lmap.get(key);
        stock--;
        if (stock == 0) {
            lmap.remove(key);
        } else {
            lmap.put(key, stock);
        }

    }


}
