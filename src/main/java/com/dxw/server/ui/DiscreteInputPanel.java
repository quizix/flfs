package com.dxw.server.ui;

import com.dxw.flfs.communication.base.Plc;
import com.dxw.flfs.communication.base.PlcConfig;
import com.dxw.flfs.communication.base.PlcException;
import com.dxw.flfs.communication.base.PlcFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

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
            Plc plc = PlcFactory.getPlc(PlcConfig.ACTIVE);
            int offset = Integer.parseInt(this.txtReadOffset.getText());

            boolean result;
            try {
                result = plc.getDiscreteInput(offset);
                this.txtReadResult.setText(Boolean.toString(result));
            } catch (PlcException ex) {
                this.txtReadResult.setText(ex.getMessage());
            }
        });
    }

}
