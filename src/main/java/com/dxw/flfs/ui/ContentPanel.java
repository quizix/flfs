/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.ui;

/**
 *
 * @author pronics3
 */
public class ContentPanel extends javax.swing.JPanel {

    /**
     * Creates new form ContentPanel
     */
    public ContentPanel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JSplitPane jSplitPane1 = new javax.swing.JSplitPane();
        com.dxw.flfs.ui.SvgPanel svgPanel1 = new com.dxw.flfs.ui.SvgPanel();
        com.dxw.flfs.ui.NotificationPanel notificationPanel1 = new com.dxw.flfs.ui.NotificationPanel();

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(400);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(0.7);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(400, 134));
        jSplitPane1.setTopComponent(svgPanel1);
        jSplitPane1.setRightComponent(notificationPanel1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
