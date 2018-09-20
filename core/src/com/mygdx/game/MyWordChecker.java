package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * Created by jackwa on 12/28/17.
 */
public class MyWordChecker {
    /*
    * word checker class using the Enable2K list at http://switchb.org/kpreid/2009/enable2k/WORD.LST
    * acknowledgments to mzechner at badlogicgames.com/forum for file reading code
     */

    // ObjectMap to hold contents of word file in memory
    public static ObjectMap<String, String> words = new ObjectMap<String, String>(120000);

    /*
    *method that checks the words objectmap to see if the word exists
     */
    public static boolean isItAWord(String word) {
        // make lowercase to check against word file
        word = word.toLowerCase();
        return words.containsKey(word);
    }

    /*
    * method to read contents of word file into objectmap
     */
    public static void readWordStringBuilder() {
        InputStream in = new BufferedInputStream(Gdx.files.internal("data/words.txt").read());
        try {
            byte[] buffer = new byte[1024*10];
            int readBytes = 0;
            StringBuilder builder = new StringBuilder();
            while((readBytes =in.read(buffer)) != -1) {
                for(int i = 0; i < readBytes; i++) {
                    char c = (char)buffer[i];
                    if(c == '\n') {
                        String wordStr = builder.toString();
                        words.put(wordStr, wordStr);
                        builder.setLength(0);
                    } else {
                        builder.append(c);
                    }
                }
            }
        } catch(Exception e) {
        } finally {
            try { in.close(); } catch(Exception e) { };
        }
    }


}
