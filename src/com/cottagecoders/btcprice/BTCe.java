package com.cottagecoders.btcprice;

import com.google.gson.Gson;

import javax.swing.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

class BTCe implements DataProvider, Runnable {

    static int b = 1;
    static int a = 100;
    static JTable table;
    static MyTableModel model;

    public BTCe() {

    }

    public void startProcess(JTable table, MyTableModel model) {
        this.table = table;
        this.model = model;

        Thread btce = new Thread(new BTCe());
        btce.start();
    }

    public void run() {
        table.setValueAt("BTCe", BTCPrice.ROW_BTCE, BTCPrice.COL_NAME);
        table.setValueAt("Data", BTCPrice.ROW_BTCE, BTCPrice.COL_TYPE);

        String url = "https://btc-e.com/api/3/ticker/btc_usd";

        while (true) {
            try {

                URL uu = new URL(url);
                Scanner scanner = new Scanner(uu.openStream());
                String response = scanner.useDelimiter("\\Z").next();
                scanner.close();

                int j = response.indexOf("{", 1);
//                System.out.println("BTCe(): ix " + j);
                response = response.substring(j, response.length() - 1);
                System.out.println("BTCe(): response after hack " + response);

                BTCeObject obj = new Gson().fromJson(response, BTCeObject.class);

                table.setValueAt(obj.getLast(), BTCPrice.ROW_BTCE, BTCPrice.COL_TRADE);
                table.setValueAt(obj.getSell(), BTCPrice.ROW_BTCE, BTCPrice.COL_BID);
                table.setValueAt(obj.getBuy(), BTCPrice.ROW_BTCE, BTCPrice.COL_ASK);

                Date dt = new Date(obj.getUpdated() * 1000);
                DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                table.setValueAt(fmt.format(dt), BTCPrice.ROW_BTCE, BTCPrice.COL_TIME);

                //      table.setValueAt(, BTCPrice.ROW_BTCE, BTCPrice.COL_BID);
                //     table.setValueAt(b, BTCPrice.ROW_BTCE, BTCPrice.COL_BID);

                Thread.sleep(60000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        private class BTCeObject {
            Double high;
            Double low;
            Double avg;
            Double vol;
            Double vol_cur;
            Double last;
            Double buy;
            Double sell;
            Long updated;

            private BTCeObject(Double high,
                               Double low,
                               Double avg,
                               Double vol,
                               Double vol_cur,
                               Double last,
                               Double buy,
                               Double sell,
                               Long updated
            ) {
                this.high = high;
                this.low = low;
                this.avg = avg;
                this.vol = vol;
                this.vol_cur = vol_cur;
                this.last = last;
                this.buy = buy;
                this.sell = sell;
                this.updated = updated;

            }

            public Double getHigh() {
                return high;
            }

            public Double getLow() {
                return low;
            }

            public Double getAvg() {
                return avg;
            }

            public Double getVol() {
                return vol;
            }

            public Double getVol_cur() {
                return vol_cur;
            }

            public Double getLast() {
                return last;
            }

            public Double getBuy() {
                return buy;
            }

            public Double getSell() {
                return sell;
            }

            public Long getUpdated() {
                return updated;
            }
        }
    }