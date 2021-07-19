package main;

import sqlInjection.AbstractSqlInjectionPasswordGuesser;
import sqlInjection.NatasSqlInjectionPasswordGuesser;
import sqlInjection.blind.AbstractTotalBlindSqlInjectionPasswordGuesser;
import sqlInjection.blind.NatasTotalBlindSqlInjectionPasswordGuesser;
import sqlInjection.responseReader.GzipHttpURLConnectionResponseReader;
import sqlInjection.responseReader.HttpURLConnectionResponseReader;

public class Main {

//    xvKIqDjy4OPv7wCRgDlmj0pFsCsDjhdP
    public static void main(String[] args) throws Throwable {
        HttpURLConnectionResponseReader responseReader = new GzipHttpURLConnectionResponseReader();
        AbstractTotalBlindSqlInjectionPasswordGuesser abstractTotalBlindSqlInjectionPasswordGuesser
                = new NatasTotalBlindSqlInjectionPasswordGuesser(5, 32, responseReader, System.out);
        abstractTotalBlindSqlInjectionPasswordGuesser.doInjection();

        AbstractSqlInjectionPasswordGuesser abstractSqlInjectionPasswordGuesser =
                new NatasSqlInjectionPasswordGuesser(32, System.out);
        abstractSqlInjectionPasswordGuesser.doInjection();

    }

}
