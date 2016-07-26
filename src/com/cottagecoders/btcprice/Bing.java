package com.cottagecoders.btcprice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Bing implements DataProvider, Runnable {

    private static final String URL = "http://www.bing.com/search?q=bitcoin+price";

    private static JTable table;

    Bing() {

    }

    public void startProcess(JTable tab) {
        table = tab;

        Thread bing = new Thread(new Bing());
        bing.start();
    }


    public void run() {
        table.setValueAt("Bing", BTCPrice.ROW_BING, BTCPrice.COL_NAME);
        table.setValueAt("Data", BTCPrice.ROW_BING, BTCPrice.COL_TYPE);


        while (true) {
            try {

                Document doc = Jsoup.connect(URL)
                        .userAgent(BTCPrice.USER_AGENT)
                        .timeout(BTCPrice.JSOUP_TIMEOUT)
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
