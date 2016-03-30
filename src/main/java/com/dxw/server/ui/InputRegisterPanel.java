/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.server.ui;

import com.dxw.flfs.communication.PlcConfig;
import com.dxw.flfs.communication.PlcFactory;
import com.dxw.flfs.communication.Plc;
import com.dxw.flfs.communication.PlcException;
import com.dxw.flfs.communication.RegisterType;

/**
 *
 * @author Administrator
 */
public class InputRegisterPanel extends javax.swing.JPanel {

    /**
     * Creates new form HoldingRegisterPanel
     */
    public InputRegisterPanel() {
        initComponents();
        
        this.buttonGroup1.add(this.radioReadFloat);
        this.buttonGroup1.add(this.radioReadInt);
        this.buttonGroup1.add(this.radioReadShort);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtReadOffset = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        radioReadShort = new javax.swing.JRadioButton();
        radioReadInt = new javax.swing.JRadioButton();
        radioReadFloat = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        txtReadResult = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jLabel2.setText("Offset");
        jLabel2.setToolTipText("");

        jLabel3.setText("Type");

        radioReadShort.setSelected(true);
        radioReadShort.setText("short");

        radioReadInt.setText("int");

        radioReadFloat.setText("float");

        jButton1.setText("Read");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtReadOffset, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radioReadFloat)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioReadInt)
                            .addComponent(radioReadShort))
                        .addGap(28, 28, 28)
                        .addComponent(jButton1)
                        .addGap(26, 26, 26)
                        .addComponent(txtReadResult, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(163, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtReadOffset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(radioReadShort))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioReadInt))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(txtReadResult, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radioReadFloat)
                .addContainerGap(132, Short.MAX_VALUE))
        );

        add(jPanel6, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Plc plc = PlcFactory.getPlc(PlcConfig.PRIMARY);

        int offset = Integer.parseInt(this.txtReadOffset.getText());

        try{
        if (this.radioReadShort.isSelected()) {
            
            short s = plc.getRegisterShort(offset, RegisterType.InputRegister);
            this.txtReadResult.setText(String.format("%d", s));

        } else if (this.radioReadInt.isSelected()) {
            int s = plc.getRegisterInt(offset, RegisterType.InputRegister);
            this.txtReadResult.setText(String.format("%d", s));
        }
        else if (this.radioReadFloat.isSelected()) {
            float v = plc.getRegisterFloat(offset, RegisterType.InputRegister);
            this.txtReadResult.setText(String.format("%f", v));
        }
        }
        catch(PlcException ex){
            this.txtReadResult.setText(ex.getMessage());
        }

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel6;
    javax.swing.JRadioButton radioReadFloat;
    javax.swing.JRadioButton radioReadInt;
    javax.swing.JRadioButton radioReadShort;
    javax.swing.JTextField txtReadOffset;
    javax.swing.JTextField txtReadResult;
    // End of variables declaration//GEN-END:variables
}
