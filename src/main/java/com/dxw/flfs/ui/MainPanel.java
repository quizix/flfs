package com.dxw.flfs.ui;

import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyFactory;

import javax.swing.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainPanel {
    public MainPanel() {
        btnStart.addActionListener(e -> {
            PlcProxy plcProxy = PlcProxyFactory.getPrimaryPlcProxy();
            plcProxy.start();
        });
        btnStop.addActionListener(e -> {
            PlcProxy plcProxy = PlcProxyFactory.getPrimaryPlcProxy();
            plcProxy.halt();
        });
        btnClean.addActionListener(e -> {
            PlcProxy plcProxy = PlcProxyFactory.getPrimaryPlcProxy();
            plcProxy.clean();
        });
    }

    public JPanel getRoot() {
        return root;
    }

    private JPanel root;
    private JButton btnStart;
    private JButton btnStop;
    private JButton btnClean;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainPanel");
        frame.setContentPane(new MainPanel().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
