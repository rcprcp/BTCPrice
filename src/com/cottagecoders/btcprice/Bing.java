package com.cottagecoders.btcprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.spec.ECField;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class Bing implements DataProvider, Runnable {

    static int b = 1;
    static int a = 100;

    private static final String URL = "http://www.bing.com/search?q=bitcoin+price";

    static JTable table;
    static MyTableModel model;

    public Bing() {

    }

    public void startProcess(JTable table, MyTableModel model) {
        this.table = table;
        this.model = model;

        Thread bing = new Thread(new Bing());
        bing.start();
    }


    public void run() {
        table.setValueAt("Bing", BTCPrice.ROW_BING, BTCPrice.COL_NAME);
        table.setValueAt("Data", BTCPrice.ROW_BING, BTCPrice.COL_TYPE);


        while (true) {
            try {

                Document doc = Jsoup.connect(URL)
                        .userAgent("Mozilla")
                        .timeout(10000)
                        .get();

                Element elem = doc.getElementById("cc_tdv");
                double d = Double.parseDouble(elem.attr("value"));
                System.out.println("Bing(): price " + d);
                table.setValueAt(d, BTCPrice.ROW_BING, BTCPrice.COL_TRADE);

                Date dt = new Date();
                DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                table.setValueAt(fmt.format(dt), BTCPrice.ROW_BING, BTCPrice.COL_TIME);

                Thread.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
