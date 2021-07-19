package sqlInjection.utils;

import java.util.ArrayList;
import java.util.List;

public class SqlInjectionUtils {

    private SqlInjectionUtils(){

    }

    public static List<Character> getDigitsAndLetters() {
        List<Character> out = new ArrayList<>(70);
        for (int i = 48; i <= 57; i++) {
            out.add((char) i);
        }
        for (int i = 65; i <= 90; i++) {
            out.add((char) i);
        }
        for (int i = 97; i <= 122; i++) {
            out.add((char) i);
        }
        return out;
    }

}
