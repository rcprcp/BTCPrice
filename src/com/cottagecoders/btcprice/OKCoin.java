package com.cottagecoders.btcprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class OKCoin implements DataProvider, Runnable {

    private static JTable table;

    public OKCoin() {

    }

    public void startProcess(JTable tab) {
        table = tab;

        Thread okcoin = new Thread(new OKCoin());
        okcoin.start();
    }

    public void run() {
        table.setValueAt("OKCoin", BTCPrice.ROW_OKCOIN, BTCPrice.COL_NAME);
        table.setValueAt("Data", BTCPrice.ROW_OKCOIN, BTCPrice.COL_TYPE);

        String url = "https://www.okcoin.com/api/v1/trades.do?symbol=btc_usd";
        MyHTTPRequest http = new MyHTTPRequest();

        while (true) {
            try {

                String response = http.doRequest(url);

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
        final Long date;
        final Long date_ms ;
        final Double price;
        final Double amount;
        final String tid;
        final String type;

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