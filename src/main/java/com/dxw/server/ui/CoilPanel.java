package com.dxw.server.ui;

import com.dxw.flfs.communication.base.Plc;
import com.dxw.flfs.communication.base.PlcConfig;
import com.dxw.flfs.communication.base.PlcException;
import com.dxw.flfs.communication.base.PlcFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class CoilPanel {
    public CoilPanel() {
        btnRead.addActionListener(e -> {
            Plc plc = PlcFactory.getPlc(PlcConfig.ACTIVE);
            int offset = Integer.parseInt(this.txtReadOffset.getText());

            boolean result;
            try {
                result = plc.getCoil(offset);
                this.txtReadResult.setText(Boolean.toString(result));
            } catch (PlcException ex) {
                this.txtReadResult.setText(ex.getMessage());
            }

        });
        btnWrite.addActionListener(e -> {
            Plc plc = PlcFactory.getPlc(PlcConfig.ACTIVE);
            int offset = Integer.parseInt(this.txtWriteOffset.getText());

            boolean result = (this.radioTrue.isSelected());
            try {
                plc.setCoil(offset, result);
            } catch (PlcException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    public JPanel getRoot() {
        return root;
    }

    private JPanel root;
    private JTabbedPane tabbedPane1;
    private JTextField txtReadOffset;
    private JTextField txtReadResult;
    private JButton btnRead;
    private JTextField txtWriteOffset;
    private JButton btnWrite;
    private JRadioButton radioTrue;
    private JRadioButton radioFalse;

}
