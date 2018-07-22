package com.cottagecoders.btcprice;

import javax.swing.*;
import java.awt.*;

class BTCPrice extends JPanel {

  //declared here for Jsoup consistency.
  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64)AppleWebKit/535.2 (KHTML, like Gecko) "
      + "Chrome/15.0.874.120 Safari/535.2";
  public static final int JSOUP_TIMEOUT = 10000;

  public static final boolean DEBUG = false;

  // mapping for rows in the table:
  public static final int ROW_COINBASE = 0;
  public static final int ROW_BITCOINCHARTS = 1;
  public static final int ROW_GOOGLE = 2;
  public static final int ROW_BING = 3;
  public static final int ROW_QUADRIGA = 4;
  public static final int ROW_OKCOIN = 5;
  public static final int ROW_KRAKEN = 6;

  // column positions.
  public static final int COL_NAME = 0;
  public static final int COL_TYPE = 1;
  public static final int COL_TIME = 2;
  public static final int COL_TRADE = 3;

  private static JTable table = null;

  private BTCPrice() {

    super(new GridLayout(1, 0));

    MyTableModel model = new MyTableModel();

    table = new JTable(model);
    table.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 20));

    table.getTableHeader().setForeground(Color.darkGray);
    table.getTableHeader().setBackground(Color.lightGray);

    table.setFont(new Font("Times New Roman", Font.BOLD, 16));

    table.setPreferredScrollableViewportSize(new Dimension(500, 150));
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
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    BTCPrice newContentPane = new BTCPrice();
    newContentPane.setOpaque(true); //content panes must be opaque
    frame.setContentPane(newContentPane);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {

        createAndShowGUI();

        // start other threads...

        CoinBase coinbase = new CoinBase();
        coinbase.startProcess(table);

        //Bing's data comes from coinBase anyway.
        Bing bing = new Bing();
        bing.startProcess(table);

        BitcoinCharts btc = new BitcoinCharts();
        btc.startProcess(table);

        Google google = new Google();
        google.startProcess(table);

        // TODO: Quadriga API always returns 403.  :(
        // The documentation implies it's a public API, so
        // i think the API's probably broken. On the other hand,
        // maybe you need to authenticate to use the API?

        final boolean QUADRIGA_IS_FIXED = false;
        if (QUADRIGA_IS_FIXED) {
          QuadrigaAPI quadriga = new QuadrigaAPI();
          quadriga.startProcess(table);
        } else {
          // Anyway, strip the price off the main page of their
          // site.
          QuadrigaHTML quadriga = new QuadrigaHTML();
          quadriga.startProcess(table);
        }

        OKCoin okcoin = new OKCoin();
        okcoin.startProcess(table);

        Kraken kraken = new Kraken();
        kraken.startProcess(table);
      }
    });
  }
}
