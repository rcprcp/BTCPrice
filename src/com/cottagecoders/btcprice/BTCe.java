package com.cottagecoders.btcprice;

import com.google.gson.Gson;

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

class BTCe implements DataProvider, Runnable {

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

                URL myUrl = new URL(url);
                InputStream ins;
                if (url.toLowerCase().contains("https")) {
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

                BTCeObj obj = new Gson().fromJson(response, BTCeObj.class);

                table.setValueAt(obj.getBtc_usd().getLast(), BTCPrice.ROW_BTCE, BTCPrice.COL_TRADE);
                table.setValueAt(obj.getBtc_usd().getSell(), BTCPrice.ROW_BTCE, BTCPrice.COL_BID);
                table.setValueAt(obj.getBtc_usd().getBuy(), BTCPrice.ROW_BTCE, BTCPrice.COL_ASK);

                Date dt = new Date(obj.getBtc_usd().getUpdated() * 1000);
                DateFormat fmt = new SimpleDateFormat("HH:mm:ss");
                table.setValueAt(fmt.format(dt), BTCPrice.ROW_BTCE, BTCPrice.COL_TIME);

                Thread.sleep(60000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class BTCeObj
    {
        private Btc_usd btc_usd;

        public Btc_usd getBtc_usd ()
        {
            return btc_usd;
        }

        public void setBtc_usd (Btc_usd btc_usd)
        {
            this.btc_usd = btc_usd;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [btc_usd = "+btc_usd+"]";
        }
    }

    public class Btc_usd
    {
        private Double vol;

        private Double last;

        private Long updated;

        private Double sell;

        private Double buy;

        private Double high;

        private Double avg;

        private Double low;

        private Double vol_cur;

        public Double getVol ()
        {
            return vol;
        }

        public void setVol (Double vol)
        {
            this.vol = vol;
        }

        public Double getLast ()
        {
            return last;
        }

        public void setLast (Double last)
        {
            this.last = last;
        }

        public Long getUpdated ()
        {
            return updated;
        }

        public void setUpdated (Long updated)
        {
            this.updated = updated;
        }

        public Double getSell ()
        {
            return sell;
        }

        public void setSell (Double sell)
        {
            this.sell = sell;
        }

        public Double getBuy ()
        {
            return buy;
        }

        public void setBuy (Double buy)
        {
            this.buy = buy;
        }

        public Double getHigh ()
        {
            return high;
        }

        public void setHigh (Double high)
        {
            this.high = high;
        }

        public Double getAvg ()
        {
            return avg;
        }

        public void setAvg (Double avg)
        {
            this.avg = avg;
        }

        public Double getLow ()
        {
            return low;
        }

        public void setLow (Double low)
        {
            this.low = low;
        }

        public Double getVol_cur ()
        {
            return vol_cur;
        }

        public void setVol_cur (Double vol_cur)
        {
            this.vol_cur = vol_cur;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [vol = "+vol+", last = "+last+", updated = "+updated+", sell = "+sell+", buy = "+buy+", high = "+high+", avg = "+avg+", low = "+low+", vol_cur = "+vol_cur+"]";
        }
    }

}