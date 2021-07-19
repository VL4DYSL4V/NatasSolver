package main;

import sessionHijacking.IntegerCookieValueGuesser;
import sessionHijacking.NatasIntegerCookieValueGuesser;

public class Main {

    public static void main(String[] args) throws Throwable {
        IntegerCookieValueGuesser integerCookieValueGuesser =
                new NatasIntegerCookieValueGuesser(0, 640);
        integerCookieValueGuesser.doSessionHijacking(System.out::println);
    }

}
