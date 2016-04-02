package com.dxw.server.ui;

import com.dxw.flfs.communication.Plc;
import com.dxw.flfs.communication.PlcConfig;
import com.dxw.flfs.communication.PlcException;
import com.dxw.flfs.communication.PlcFactory;

import javax.swing.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class DiscreteInputPanel {
    public JPanel getRoot() {
        return root;
    }

    private JPanel root;
    private JTextField txtReadOffset;
    private JTextField txtReadResult;
    private JButton btnRead;

    public DiscreteInputPanel() {
        btnRead.addActionListener(e -> {
            Plc plc = PlcFactory.getPlc(PlcConfig.PRIMARY);
            int offset = Integer.parseInt(this.txtReadOffset.getText());

            boolean result;
            try {
                result = plc.getDiscreteInput(offset);
                this.txtReadResult.setText( Boolean.toString(result));
            } catch (PlcException ex) {
                this.txtReadResult.setText( ex.getMessage());
            }
        });
    }
}
