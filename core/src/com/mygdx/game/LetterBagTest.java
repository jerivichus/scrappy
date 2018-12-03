package com.mygdx.game;

/**
 * Created by wyoung on 11/30/18.
 */
public class LetterBagTest {

    public static void main(String args[]) {
        System.out.println("Starting to test the LetterBag");
        LetterBag.initLetterMap();
        try {
            while (true) {
                char c = LetterBag.getRandomChar();
                if(c==' ') {
                    break;
                }
                System.out.println("  Randomly got: " + c + "\n");
            }
        } catch (Exception e) {
            System.out.println("Exception encountered: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("     Now testing trading functionality!");

        LetterBag.initLetterMap();
        System.out.println("   Trading vowels...");
        tradeLetter('a');
        tradeLetter('e');
        tradeLetter('i');
        tradeLetter('o');
        tradeLetter('u');
        System.out.println("   Trading consonants...");
        tradeLetter('c');
        tradeLetter('d');
        tradeLetter('h');
        tradeLetter('m');
        tradeLetter('z');
        tradeLetter('q');
        tradeLetter('r');
        tradeLetter('t');


    }

    public static void tradeLetter(char c) {
        char trade = LetterBag.tradeLetter(c);
        System.out.println("Traded in " + c + " and got " + trade);

    }

}
