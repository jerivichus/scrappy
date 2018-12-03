package com.mygdx.game;

/**
 * Created by jackwa on 11/30/18.
 */
public class LetterBagTest {

    public static void main(String args[]) {
        LetterBag.initLetterMap();
        try {
            while (true) {
                char c = LetterBag.getRandomChar();
                if(c==' ') {
                    break;
                }
                //LetterBag.updateStock(c);
                System.out.println(c);
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.out.println("Exception encountered: " + e.getMessage());
            //e.printStackTrace();
        }
    }
}
