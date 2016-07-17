package com.cottagecoders.btcprice;

import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class QuadrigaAPI implements DataProvider, Runnable {

    static JTable table;
    static MyTableModel model;

    QuadrigaAPI() {

    }

    public void startProcess(JTable table, MyTableModel model) {
        this.table = table;
        this.model = model;

        Thread quadriga = new Thread(new QuadrigaAPI());
        quadriga.start();
    }

    public void run() {
        table.setValueAt("QuadrigaAPI", BTCPrice.ROW_QUADRIGA, BTCPrice.COL_NAME);
        table.setValueAt("Data", BTCPrice.ROW_QUADRIGA, BTCPrice.COL_TYPE);

        String url = "https://api.quadrigacx.com/v2/ticker?book=btc_usd";

        while (true) {
            try {

                InputStream ins;

                URL myUrl = new URL(url);
                if (url.toLowerCase().contains("https")) {
                    HttpsURLConnection con = (HttpsURLConnection) myUrl.openConnection();
                    ins = con.getInputStream();
                } else {
                    HttpURLConnection con = (HttpURLConnection) myUrl.openConnection();
                    ins = con.getInputStream();
                }
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader in = new BufferedReader(isr);

                String inputLine = "";
                String response = "";
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                System.out.println(response);
                in.close();

                QuadrigaObject obj = new Gson().fromJson(response, QuadrigaObject.class);

                table.setValueAt(obj.getBid(), BTCPrice.ROW_BTCE, BTCPrice.COL_BID);
                table.setValueAt(obj.getAsk(), BTCPrice.ROW_BTCE, BTCPrice.COL_ASK);

                Date dt = new Date(obj.getTimestamp() * 1000);
                DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                table.setValueAt(fmt.format(dt), BTCPrice.ROW_BTCE, BTCPrice.COL_TIME);

                //      table.setValueAt(, BTCPrice.ROW_BTCE, BTCPrice.COL_BID);
                //     table.setValueAt(b, BTCPrice.ROW_BTCE, BTCPrice.COL_BID);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(60000);
            } catch (Exception e) {
            }
        }
    }

    private class QuadrigaObject {
        Double high;
        Double low;
        Double vwap;
        Double volume;
        Double last;
        Double bid;
        Double ask;
        Long timestamp;

        private QuadrigaObject(Double high,
                               Double low,
                               Double vwap,
                               Double volume,
                               Double last,
                               Double bid,
                               Double ask,
                               Long timestamp
        ) {
            this.high = high;
            this.low = low;
            this.vwap = vwap;
            this.volume = volume;
            this.last = last;
            this.bid = bid;
            this.ask = ask;
            this.timestamp = timestamp;

        }

        public Double getHigh() {
            return high;
        }

        public Double getLow() {
            return low;
        }

        public Double getVwap() {
            return vwap;
        }

        public Double getVolume() {
            return volume;
        }

        public Double getLast() {
            return last;
        }

        public Double getBid() {
            return bid;
        }

        public Double getAsk() {
            return ask;
        }

        public Long getTimestamp() {
            return timestamp;
        }
    }
}