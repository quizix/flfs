package com.dxw.server.ui;

import com.dxw.flfs.communication.*;

import javax.swing.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class HoldingRegisterPanel {
    private JTextField txtReadOffset;
    private JTextField txtReadResult;
    private JButton btnRead;
    private JTextField txtWriteOffset;
    private JButton btnWrite;
    private JRadioButton radioShort;
    private JRadioButton radioFloat;
    private JRadioButton radioInt;
    private JRadioButton radioWriteShort;
    private JRadioButton radioWriteInt;
    private JRadioButton radioWriteFloat;
    private JTextField txtValue;
    private JPanel root;

    public HoldingRegisterPanel() {
        btnRead.addActionListener(e -> {
            Plc plc = PlcFactory.getPlc(PlcConfig.PRIMARY);
            int offset = Integer.parseInt(this.txtReadOffset.getText());
            try {
                if (this.radioShort.isSelected()) {

                    short s = plc.getRegisterShort(offset, RegisterType.HoldingRegister);
                    this.txtReadResult.setText(String.format("%d", s));

                } else if (this.radioInt.isSelected()) {
                    int x = plc.getRegisterInt(offset, RegisterType.HoldingRegister);
                    this.txtReadResult.setText(String.format("%d", x));
                } else if (this.radioFloat.isSelected()) {
                    float v = plc.getRegisterFloat(offset, RegisterType.HoldingRegister);
                    this.txtReadResult.setText(String.format("%f", v));
                }
            } catch (PlcException ex) {
                this.txtReadResult.setText(ex.getMessage());
            }
        });

        btnWrite.addActionListener(e -> {
            Plc plc = PlcFactory.getPlc(PlcConfig.PRIMARY);

            int offset = Integer.parseInt(this.txtWriteOffset.getText());
            try {
                if (this.radioWriteShort.isSelected()) {
                    short value = Short.parseShort(this.txtValue.getText());
                    plc.setRegister(offset, value);
                } else if (this.radioWriteInt.isSelected()) {
                    int value = Integer.parseInt(this.txtValue.getText());
                    plc.setRegister(offset, value);
                } else if (this.radioWriteFloat.isSelected()) {
                    float value = Float.parseFloat(this.txtValue.getText());
                    plc.setRegister(offset, value);
                }
            } catch (PlcException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }
}
