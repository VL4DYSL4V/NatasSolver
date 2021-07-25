package main;

import cipherCracking.CaesarCracker;

public class Main {

    public static void main(String[] args) throws Throwable {
        for(int i = 0; i < 26; i++){
            System.out.println(CaesarCracker.decodeIgnoringWhitespace("OMQEMDUEQMEK", i));
        }
    }

}
