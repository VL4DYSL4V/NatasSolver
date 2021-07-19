package responseReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

public class NatasHttpUrlConnectionResponseReader implements HttpURLConnectionResponseReader{

    private final long skipBytesAmount;

    public NatasHttpUrlConnectionResponseReader(long skipBytesAmount) {
        this.skipBytesAmount = skipBytesAmount;
    }

    @Override
    public String read(HttpURLConnection from) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder(1024);
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(from.getInputStream());
             InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            bufferedReader.skip(skipBytesAmount);
            while (true) {
                int ch = bufferedReader.read();
                if (ch == -1) {
                    break;
                }
                char next = (char) ch;
                if(next == '<'){
                    break;
                }
                stringBuilder.append(next);
            }
        }
        return stringBuilder.toString();
    }
}
