package com.cottagecoders.btcprice;

import javax.swing.*;
import java.awt.*;

class BTCPrice extends JPanel {

    public static final boolean DEBUG = false;
    
    // mapping for rows in the table: 
    public static int ROW_COINBASE = 0;
    public static int ROW_BITCOINCHARTS = 1;
    public static int ROW_BTCE = 2;
    public static int ROW_GOOGLE = 3;
    public static int ROW_QUADRIGA = 4;
    public static int ROW_OKCOIN= 5;
    public static int ROW_BING = 6;


    // column positions.
    public static final int COL_NAME = 0;
    public static final int COL_TYPE = 1;
    public static final int COL_TIME = 2;
    public static final int COL_TRADE = 3;
    public static final int COL_BIDSIZE = 4;
    public static final int COL_BID = 5;
    public static final int COL_SPREAD = 6;
    public static final int COL_ASK = 7;
    public static final int COL_ASKSIZE = 8;

    static JTable table = null;
    static MyTableModel model = null;

    public BTCPrice() {
        super(new GridLayout(1, 0));

        model = new MyTableModel();
        table = new JTable(model);
        table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 20));

        //remarkably ugly.
        //table.getTableHeader().setForeground(Color.white);
        //table.getTableHeader().setBackground(Color.black);

        table.setFont(new Font("Times New Roman", Font.BOLD, 16));

        table.setPreferredScrollableViewportSize(new Dimension(900, 175));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("BitCoin Prices");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        BTCPrice newContentPane = new BTCPrice();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                createAndShowGUI();

                // start other threads...

                CoinBase coinbase = new CoinBase();
                coinbase.startProcess(table, model);

                //Bing's data comes from coinBase anyway.
                Bing bing = new Bing();
                bing.startProcess(table, model);

                BitcoinCharts btc = new BitcoinCharts();
                btc.startProcess(table, model);

                BTCe btce = new BTCe();
                btce.startProcess(table, model);

                Google google = new Google();
                google.startProcess(table, model);

                // TODO: Quadriga API always returns 403.  :(
                // The documentation implies it's a public API, so
                // i think the API's probably broken. On the other hand,
                // maybe you need to authenticate to use the API?
                final boolean IS_QUADRIGA_FIXED = false;

                if(IS_QUADRIGA_FIXED) {
                    QuadrigaAPI quadriga = new QuadrigaAPI();
                    quadriga.startProcess(table, model);
                } else {
                    // Anyway, strip the price off the main page of their
                    // site.
                    QuadrigaHTML quadriga = new QuadrigaHTML();
                    quadriga.startProcess(table, model);
                }

                OKCoin okcoin = new OKCoin();
                okcoin.startProcess(table, model);
            }
        });
     }
}
