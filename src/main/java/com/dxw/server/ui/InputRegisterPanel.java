package com.dxw.server.ui;

import com.dxw.flfs.communication.*;

import javax.swing.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class InputRegisterPanel {
    private JTextField txtReadOffset;
    private JTextField txtReadResult;
    private JButton btnRead;
    private JRadioButton radioShort;
    private JRadioButton radioInt;
    private JRadioButton radioFloat;
    private JPanel root;

    public InputRegisterPanel() {
        btnRead.addActionListener(e -> {
            Plc plc = PlcFactory.getPlc(PlcConfig.PRIMARY);

            int offset = Integer.parseInt(this.txtReadOffset.getText());

            try{
                if (this.radioShort.isSelected()) {

                    short s = plc.getRegisterShort(offset, RegisterType.InputRegister);
                    this.txtReadResult.setText(String.format("%d", s));

                } else if (this.radioInt.isSelected()) {
                    int s = plc.getRegisterInt(offset, RegisterType.InputRegister);
                    this.txtReadResult.setText(String.format("%d", s));
                }
                else if (this.radioFloat.isSelected()) {
                    float v = plc.getRegisterFloat(offset, RegisterType.InputRegister);
                    this.txtReadResult.setText(String.format("%f", v));
                }
            }
            catch(PlcException ex){
                this.txtReadResult.setText(ex.getMessage());
            }
        });
    }
}
