package main;

import sessionHijacking.IntegerCookieValueGuesser;
import sessionHijacking.NatasHexCookieValueGuesser;
import sessionHijacking.NatasIntegerCookieValueGuesser;

public class Main {

    public static void main(String[] args) throws Throwable {
        IntegerCookieValueGuesser integerCookieValueGuesser =
                new NatasHexCookieValueGuesser(0, 640);
        integerCookieValueGuesser.doSessionHijacking();
    }

}
