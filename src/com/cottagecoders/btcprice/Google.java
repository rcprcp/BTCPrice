package com.cottagecoders.btcprice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Google implements DataProvider, Runnable {

    private static JTable table;

    public Google() {

    }

    public void startProcess(JTable tab) {
        table = tab;

        Thread btce = new Thread(new Google());
        btce.start();
    }

    public void run() {
        table.setValueAt("Google", BTCPrice.ROW_GOOGLE, BTCPrice.COL_NAME);
        table.setValueAt("Data", BTCPrice.ROW_GOOGLE, BTCPrice.COL_TYPE);

        String url = "http://www.google.com/finance?q=currency+bitcoin";

        while (true) {
            try {

                Document doc = Jsoup.connect(url)
                        .userAgent(BTCPrice.USER_AGENT)
                        .timeout(BTCPrice.JSOUP_TIMEOUT)
                        .get();

                Elements metaItems = doc.select("[itemprop]");
                for (Element e : metaItems) {
                    String str = e.toString();
                    if (str.contains("\"price\"")) {
                        int j = str.indexOf("content");
                        str = str.substring(j);
                        j = str.indexOf("\"");
                        int k = str.lastIndexOf("\"");
                        if(j == k) {
                            System.out.println("Google(): parse failed: " + str);
                        } else {
                            Double price = Double.parseDouble(str.substring(j+1, k));
                            System.out.println("Google(): price " + price);
                            table.setValueAt(price, BTCPrice.ROW_GOOGLE, BTCPrice.COL_TRADE);
                            Date dt = new Date();
                            DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                            table.setValueAt(fmt.format(dt), BTCPrice.ROW_GOOGLE, BTCPrice.COL_TIME);
                        }
                    }
                }

                /*
                 table.setValueAt(obj.getSell(), BTCPrice.ROW_GOOGLE, BTCPrice.COL_BID);
                 table.setValueAt(obj.getBuy(), BTCPrice.ROW_GOOGLE, BTCPrice.COL_ASK);

                 Date dt = new Date(obj.getUpdated() * 1000);
                 DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                 table.setValueAt(fmt.format(dt), BTCPrice.ROW_GOOGLE, BTCPrice.COL_TIME);
                 */
                Thread.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}