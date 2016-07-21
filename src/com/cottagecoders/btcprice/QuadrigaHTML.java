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

    static JTable table;
    static MyTableModel model;

    QuadrigaHTML() {

    }

    public void startProcess(JTable table, MyTableModel model) {
        this.table = table;
        this.model = model;

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