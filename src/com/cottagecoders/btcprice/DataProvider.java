package com.cottagecoders.btcprice;

import javax.swing.*;

interface  DataProvider {

    void startProcess(JTable table, MyTableModel model);
}
