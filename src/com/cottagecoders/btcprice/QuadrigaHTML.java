package com.cottagecoders.btcprice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class QuadrigaHTML implements DataProvider, Runnable {

    private static JTable table;

    QuadrigaHTML() {

    }

    public void startProcess(JTable tab) {
        table = tab;

        Thread quadriga = new Thread(new QuadrigaHTML());
        quadriga.start();
    }

    public void run() {
        table.setValueAt("QuadrigaHTML", BTCPrice.ROW_QUADRIGA, BTCPrice.COL_NAME);
        table.setValueAt("Data", BTCPrice.ROW_QUADRIGA, BTCPrice.COL_TYPE);

        String url = "https://www.quadrigacx.com";

        while (true) {
            try {

                InputStream ins;

                Document doc = null;
                try {
                    doc = Jsoup.connect(url)
                            .userAgent(BTCPrice.USER_AGENT)
                            .timeout(BTCPrice.JSOUP_TIMEOUT)
                            .get();
                } catch (Exception e) {
                    System.out.println("Jsoup exception: " + e.getMessage());
                    break;
                }

                //parse here...
                Elements items = doc.select("ul li strong");
                double price = 0;
                if(items.size() > 1) {
                    price = Double.parseDouble(items.get(0) .toString().replaceAll("[^0-9.]", ""));
                }
                table.setValueAt(price, BTCPrice.ROW_QUADRIGA, BTCPrice.COL_TRADE);

                Date dt = new Date(System.currentTimeMillis());
                DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                table.setValueAt(fmt.format(dt), BTCPrice.ROW_QUADRIGA, BTCPrice.COL_TIME);


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(60000);
            } catch (Exception e) {
            }
        }
    }
}