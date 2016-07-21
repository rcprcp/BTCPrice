package com.cottagecoders.btcprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class CoinBase implements DataProvider, Runnable {

    static JTable table;
    static MyTableModel model;

    public CoinBase() {

    }

    public void startProcess(JTable table, MyTableModel model) {
        this.table = table;
        this.model = model;

        Thread coinbase = new Thread(new CoinBase());
        coinbase.start();
    }

    public void run() {
        table.setValueAt("CoinBase", BTCPrice.ROW_COINBASE, BTCPrice.COL_NAME);
        table.setValueAt("Spot", BTCPrice.ROW_COINBASE, BTCPrice.COL_TYPE);

        String url = "https://api.coinbase.com/v2/prices/spot";
        MyHTTPRequest http = new MyHTTPRequest();
        while (true) {
            try {

                String response = http.doRequest(url);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Reply reply = gson.fromJson(response, Reply.class);
                table.setValueAt(Double.parseDouble(reply.getData().getAmount()), BTCPrice.ROW_COINBASE, BTCPrice.COL_TRADE);

                Date dt = new Date(System.currentTimeMillis());
                DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                table.setValueAt(fmt.format(dt), BTCPrice.ROW_COINBASE, BTCPrice.COL_TIME);

                Thread.sleep(60000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Reply
    {
        private Data data;

        private Warnings[] warnings;

        public Data getData ()
        {
            return data;
        }

        public void setData (Data data)
        {
            this.data = data;
        }

        public Warnings[] getWarnings ()
        {
            return warnings;
        }

        public void setWarnings (Warnings[] warnings)
        {
            this.warnings = warnings;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [data = "+data+", warnings = "+warnings+"]";
        }
    }


    public class Data {
        private String amount;

        private String currency;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        @Override
        public String toString() {
            return "ClassPojo [amount = " + amount + ", currency = " + currency + "]";
        }
    }

    public class Warnings {
        private String message;

        private String id;

        private String url;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "ClassPojo [message = " + message + ", id = " + id + ", url = " + url + "]";
        }
    }
}
