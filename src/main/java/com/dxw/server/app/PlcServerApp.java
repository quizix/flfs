/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.server.app;

import com.dxw.common.ms.NotificationManagerImpl;
import com.dxw.server.ui.MainFrame;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;

/**
 *
 * @author pronics3
 */
public class PlcServerApp {

    PlcServerApp() throws ServiceException {
        init();
    }

    private void init() throws ServiceException {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        registry.register(new NotificationManagerImpl());

        try {
            new Simulator().start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void start() {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    public static void main(String args[]) {
        try {
            new PlcServerApp().start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
