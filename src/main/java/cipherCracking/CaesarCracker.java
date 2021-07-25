package cipherCracking;

public class CaesarCracker {

    public static String decodeIgnoringWhitespace(String input, int step) {
        if (step < 0 || step >= 26) {
            throw new IllegalArgumentException("Step is not within bounds");
        }
        int lowerLowerCaseBound = 65;
        int upperLowerCaseBound = 91;
        int lowerUpperCaseBound = 97;
        int upperUpperCaseBound = 123;
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            @SuppressWarnings("all") //it says it is redundant, but code is much more readable
            int code = c;
            int neuCode = code + step;
            if (code >= lowerLowerCaseBound && code < upperLowerCaseBound) {
                neuCode = upperLowerCaseBound <= neuCode ? neuCode - 26 : neuCode;
                sb.append((char) neuCode);
            } else if (code >= lowerUpperCaseBound && code < upperUpperCaseBound) {
                neuCode = upperUpperCaseBound <= neuCode ? neuCode - 26 : neuCode;
                sb.append((char) neuCode);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
