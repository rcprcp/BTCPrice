package com.cottagecoders.btcprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

class OKCoin implements DataProvider, Runnable {

    static JTable table;
    static MyTableModel model;

    public OKCoin() {

    }

    public void startProcess(JTable table, MyTableModel model) {
        this.table = table;
        this.model = model;

        Thread okcoin = new Thread(new OKCoin());
        okcoin.start();
    }

    public void run() {
        table.setValueAt("OKCoin", BTCPrice.ROW_OKCOIN, BTCPrice.COL_NAME);
        table.setValueAt("Data", BTCPrice.ROW_OKCOIN, BTCPrice.COL_TYPE);

        String url = "https://www.okcoin.com/api/v1/trades.do?symbol=btc_usd";

        while (true) {
            try {

                InputStream ins;

                URL myUrl = new URL(url);
                if(url.toLowerCase().contains("https")) {
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
                in.close();

                Gson gson= new GsonBuilder().setPrettyPrinting().create();
                OKCoinObject obj[] = gson.fromJson(response, OKCoinObject[].class);
                if(obj != null) {
//                    System.out.println("OkCoin(): array length " + obj.length + " " + gson.toJson(obj[obj.length-1]));
                    table.setValueAt(obj[obj.length-1].getPrice(), BTCPrice.ROW_OKCOIN, BTCPrice.COL_TRADE);

                    Date dt = new Date(obj[obj.length-1].getdate_ms());
                    DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                    table.setValueAt(fmt.format(dt), BTCPrice.ROW_OKCOIN, BTCPrice.COL_TIME);
                }
                Thread.sleep(60000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class OKCoinObject {
        Long date;
        Long date_ms ;
        Double price;
        Double amount;
        String tid;
        String type;

        private OKCoinObject(
                Long date,
                Long date_ms,
                Double price,
                Double amount,
                String tid,
                String type
        ) {
            this.date = date;
            this.date_ms = date_ms;
            this.price = price;
            this.amount = amount ;
            this.tid = tid ;
            this.type = type;

        }

        public Long getDate() {
            return date;
        }

        public Long getdate_ms() {
            return date_ms;
        }

        public Double getPrice() {
            return price;
        }

        public Double getAmount() {
            return amount;
        }

        public String getTid() {
            return tid;
        }

        public String getType() {
            return type;
        }
    }
}