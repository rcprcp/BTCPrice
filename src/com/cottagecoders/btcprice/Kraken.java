package com.cottagecoders.btcprice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

class Kraken implements DataProvider, Runnable {

    static JTable table;
    static MyTableModel model;

    public void startProcess(JTable table, MyTableModel model) {
        this.table = table;
        this.model = model;

        Thread kraken = new Thread(new Kraken());
        kraken.start();
    }

    public void run() {

        table.setValueAt("Kraken", BTCPrice.ROW_KRAKEN, BTCPrice.COL_NAME);
        table.setValueAt("Spot", BTCPrice.ROW_KRAKEN, BTCPrice.COL_TYPE);

        String url = "https://api.kraken.com/0/public/Trades?pair=XBTUSD";
        MyHTTPRequest http = new MyHTTPRequest();

        while (true) {
            try {

                String response = http.doRequest(url);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                KrakenObj reply = gson.fromJson(response, KrakenObj.class);

                String [][] data = reply.getResult().getXXBTZUSD();
                int len  = reply.getResult().getXXBTZUSD().length;

//                System.out.println(" price " + data[len-1][0] + " size " + data[len-1][1] + "  epoch " + data[len-1][2]);

                table.setValueAt(Double.parseDouble(data[len-1][0]), BTCPrice.ROW_KRAKEN, BTCPrice.COL_TRADE);
                double dd = Double.parseDouble(data[len-1][2]);
                dd *= 1000;  //  scale millis into the integer part.

                Date dt = new Date((long)dd);
                DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                table.setValueAt(fmt.format(dt), BTCPrice.ROW_KRAKEN, BTCPrice.COL_TIME);

                Thread.sleep(60000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public class KrakenObj
    {
        private Result result;

        private String[] error;

        public Result getResult ()
        {
            return result;
        }

        public void setResult (Result result)
        {
            this.result = result;
        }

        public String[] getError ()
        {
            return error;
        }

        public void setError (String[] error)
        {
            this.error = error;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [result = "+result+", error = "+error+"]";
        }
    }
    public class Result
    {
        private String last;

        private String[][] XXBTZUSD;

        public String getLast ()
        {
            return last;
        }

        public void setLast (String last)
        {
            this.last = last;
        }

        public String[][] getXXBTZUSD ()
        {
            return XXBTZUSD;
        }

        public void setXXBTZUSD (String[][] XXBTZUSD)
        {
            this.XXBTZUSD = XXBTZUSD;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [last = "+last+", XXBTZUSD = "+XXBTZUSD+"]";
        }
    }

}
