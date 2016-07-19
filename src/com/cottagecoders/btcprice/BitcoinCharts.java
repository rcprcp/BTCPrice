package com.cottagecoders.btcprice;

import com.google.gson.Gson;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class BitcoinCharts implements DataProvider, Runnable {

    static int b = 1;
    static int a = 100;
    static JTable table;
    static MyTableModel model;

    public BitcoinCharts() {

    }

    public void startProcess(JTable table, MyTableModel model) {
        this.table = table;
        this.model = model;

        Thread bitcoinCharts = new Thread(new BitcoinCharts());
        bitcoinCharts.start();
    }

    public void run() {

        table.setValueAt("BitcoinCharts", BTCPrice.ROW_BITCOINCHARTS, BTCPrice.COL_NAME);
        table.setValueAt("Data", BTCPrice.ROW_BITCOINCHARTS, BTCPrice.COL_TYPE);

        String URI = "api.bitcoincharts.com";
        int port = 27007;
        BufferedReader br = null;
        while(true) {
            try {
                Socket ClientSocket = new Socket(URI, port);
                ClientSocket.setSoTimeout(20000);

                br = new BufferedReader(new InputStreamReader(
                        ClientSocket.getInputStream()));

                while (true) {
                    String jsonText = br.readLine();
                    if (jsonText != null) ;

                    // first check if it's in USD.
                    if (jsonText.contains("USD")) {
                        BitcoinChartsObject bcObj = new Gson().fromJson(jsonText, BitcoinChartsObject.class);

                        Date dt = new Date(bcObj.getTimestamp() * 1000);
                        DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                        table.setValueAt(fmt.format(dt), BTCPrice.ROW_BITCOINCHARTS, BTCPrice.COL_TIME);
                        table.setValueAt(bcObj.getPrice(), BTCPrice.ROW_BITCOINCHARTS, BTCPrice.COL_TRADE);

                        String symbol = bcObj.getSymbol().replace("USD", "").toUpperCase();
                        table.setValueAt(symbol, BTCPrice.ROW_BITCOINCHARTS, BTCPrice.COL_TYPE);
                        if (BTCPrice.DEBUG)
                            System.out.println(fmt.format(dt) + ": " + bcObj.getSymbol() + " " + bcObj.getPrice());
                    } else {
                        // print CNY stuff?
                    }
                }
            } catch (Exception e) {
                System.out.println("BitcoinCharts(): socket connection failed.  retry in one minute.");
                try {
                    br.close();
                } catch (Exception ee) {
                }
            }

            try {
                Thread.sleep(60000);    // wait a minute...
            } catch(Exception e) {
            }
        }
    }

    private class BitcoinChartsObject {
        Double volume;
        Long timestamp;
        Double price;
        String symbol;
        Long id;

        private BitcoinChartsObject(Double volume, Long timestamp, Double price, String symbol, Long id) {
            this.volume = volume;
            this.timestamp = timestamp;
            this.price = price;
            this.symbol = symbol;
            this.id = id;
        }

        public Double getVolume() {
            return volume;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public Double getPrice() {
            return price;
        }

        public String getSymbol() {
            return symbol;
        }

        public Long getId() {
            return id;
        }
    }
}