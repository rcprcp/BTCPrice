package com.cottagecoders.btcprice;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bob on 7/21/2016.
 */
class MyHTTPRequest {

    MyHTTPRequest() {
    }


    String doRequest(String url) throws Exception {

        String response = "";
        BufferedReader input = null;
        try {
            URL myUrl = new URL(url);
            InputStream ins;
            if (url.toLowerCase().contains("https")) {
                HttpsURLConnection con = (HttpsURLConnection) myUrl.openConnection();
                ins = con.getInputStream();
            } else {
                HttpURLConnection con = (HttpURLConnection) myUrl.openConnection();
                ins = con.getInputStream();
            }

            InputStreamReader isr = new InputStreamReader(ins);
            input = new BufferedReader(isr);

            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                response += inputLine;
            }
        } catch(Exception e) {
            throw e;
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                throw e;
            }
        }

        return response;
    }
}