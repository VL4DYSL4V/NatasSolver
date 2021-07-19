package responseReader;

import java.net.HttpURLConnection;

public interface HttpURLConnectionResponseReader {

    String read(HttpURLConnection from) throws Throwable;

}
