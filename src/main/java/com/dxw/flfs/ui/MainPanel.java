package com.dxw.flfs.ui;

import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;

import javax.swing.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainPanel {
    public MainPanel() {
        btnStart.addActionListener(e -> {
            PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
            plcProxy.start();
        });
        btnStop.addActionListener(e -> {
            PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
            plcProxy.halt();
        });
        btnClean.addActionListener(e -> {
            PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
            plcProxy.clean();
        });
    }

    public void setActionEnable(String action, boolean enable){
        if(action.equals("start"))
            btnStart.setEnabled(enable);
        else if(action.equals("stop"))
            btnStart.setEnabled(enable);
        else if(action.equals("clean"))
            btnStart.setEnabled(enable);

    }

    public JPanel getRoot() {
        return root;
    }

    private JPanel root;
    private JButton btnStart;
    private JButton btnStop;
    private JButton btnClean;

}
