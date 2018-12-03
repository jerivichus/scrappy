package com.mygdx.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by jackwa on 1/3/18.
 * Modified by wyoung on 12/02/18.
 */
public class LetterBag {

    private static HashMap<Character, Integer> letterMap;

    /**
     * Fill the letter bag.
     *
     * @return the filled letter bag.
     */
    public static HashMap initLetterMap() {
        letterMap = new HashMap();

        letterMap.put('A', 10);
        letterMap.put('B', 3);
        letterMap.put('C', 2);
        letterMap.put('D', 4);
        letterMap.put('E', 10);
        letterMap.put('F', 2);
        letterMap.put('G', 3);
        letterMap.put('H', 3);
        letterMap.put('I', 8);
        letterMap.put('J', 1);
        letterMap.put('K', 2);
        letterMap.put('L', 5);
        letterMap.put('M', 3);
        letterMap.put('N', 5);
        letterMap.put('O', 9);
        letterMap.put('P', 2);
        letterMap.put('Q', 1);
        letterMap.put('R', 7);
        letterMap.put('S', 5);
        letterMap.put('T', 7);
        letterMap.put('U', 5);
        letterMap.put('V', 2);
        letterMap.put('W', 2);
        letterMap.put('X', 1);
        letterMap.put('Y', 2);
        letterMap.put('Z', 1);

        return letterMap;
    }

    /**
     * Get a random letter.
     *
     * @return a random letter.
     */
    public static char getRandomChar() {
        List<char> letters = new ArrayList<char>;
        for (char key :letterMap.keySet()) {
            for (int i = 0; i < letterMap.get(key); i++) {
                letters.add(key);
            }
        }
        char returnLetter = ' ';

        if (letters.size() > 0) {
            Random r = new Random();
            returnLetter = letters.get(r.nextInt(letters.size()));
            updateStock(returnLetter);
        }
        return returnLetter;
    }

    /**
     * Get a random vowel or consonant for trading.
     *
     * @param getVowel True if vowel, False if consonant.
     * @return New vowel, new consonant, or a blank character if none are left in the bag.
     */
    private static char getRandomChar(boolean getVowel) {
        List<Character> keysAsArr = new ArrayList<Character>(letterMap.keySet());
        // If there are no keys left, return ' '
        if(keysAsArr.isEmpty()) {
            return ' ';
        }

        // Else, create a list of consonants and vowels.
        // Add the number of each consonant or vowel to the appropriate list.
        Random r = new Random();
        List<Character> consonants = new ArrayList<Character>();
        List<Character> vowels = new ArrayList<Character>();
        for (int i = 0; i < keysAsArr.size(); i++) {
            Character c = keysAsArr.get(i);
            if (isVowel(Character.toString(c))) {
                for(int j = 0; j < letterMap.get(c); j++) {
                    vowels.add(c);
                }
            } else {
                for(int j = 0; j < letterMap.get(c); j++) {
                    consonants.add(c);
                }
            }
        }

        // Attempt to get a replacement vowel or consonant, and update the stock.
        // Return ' ' if one is not available.
        char returnLetter = ' ';
        if (getVowel) {
            if (vowels.size() > 0) {
                returnLetter = vowels.get(r.nextInt(vowels.size()));
                updateStock(returnLetter);
            }
        } else {
            if (consonants.size() > 0) {
                returnLetter = consonants.get(r.nextInt(consonants.size()));
                updateStock(returnLetter);
            }
        }

        return returnLetter;
    }


    /**
     * Trade a letter. Vowels return vowels, consonants return consonants.
     *
     * @param c the letter to trade
     * @return the replacement letter
     */
    public static char tradeLetter(char c) {
        Character replacement = getRandomChar(isVowel(c));
        // if replacement returned a real char, put c back in bag and return replacement...if replacement still blank, then return c
        if (replacement != ' ') {
            if (letterMap.containsKey(c)) {
                int cInCurrStock = (Integer) letterMap.get(c);
                letterMap.put(c, cInCurrStock + 1);
            } else {
                letterMap.put(c, 1);
            }
            return replacement;
        } else {
            return c;
        }
    }

    /**
     * Check if a char letter is a vowel.
     *
     * @param c the character to check.
     * @return true if the character is a vowel
     */
    public static boolean isVowel(char c) {
        return isVowel(Character.toString(c));
    }

    /**
     * Check if a String letter is a vowel.
     *
     * @param s The String to check.
     * @return true if the letter is a vowel.
     */
    public static boolean isVowel(String s) {
        if (("AEIOU".indexOf() >= 0)) {
            return true;
        }
        return false;
    }

    /**
     * Update the stock of a letter in the LetterBag.
     *
     * @param key the letter to update.
     */
    public static void updateStock(char key) {
        int stock = (Integer) letterMap.get(key);
        stock--;
        if (stock == 0) {
            letterMap.remove(key);
        } else {
            letterMap.put(key, stock);
        }
    }
}
