package responseReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

public class GzipHttpURLConnectionResponseReader implements HttpURLConnectionResponseReader {

    @Override
    public String read(HttpURLConnection from) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder(1024);
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(from.getInputStream());
             InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            while (true) {
                int ch = bufferedReader.read();
                if (ch == -1) {
                    break;
                }
                char next = (char) ch;
                stringBuilder.append(next);
            }
        }
        return stringBuilder.toString();
    }

}
