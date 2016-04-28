/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.app;

import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.ui.MainFrame;
import org.quartz.SchedulerException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fermented Liquid Feeding System
 * 发酵液态料饲喂系统
 *
 * @author pronics3
 */
public class FlfsApp {
    private static AppContext context = new AppContext();

    private FlfsApp()
            throws ServiceException, SchedulerException {
        loadAppId();

        //initialize the services.
        new AppInitiator(ServiceRegistryImpl.getInstance())
                .initServices();
    }

    public static AppContext getContext() {
        return context;
    }


    private void loadAppId() {
        Properties prop = new Properties();
        InputStream in = this.getClass().getResourceAsStream("/flfs.config");
        try {
            prop.load(in);
            String appId = prop.getProperty("appId");
            context.setAppId(appId);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "无法获取程序appId！", "消息提示", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }
    /*private void loadAppConfig() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            AppConfig appConfig = dao.findAppConfig(context.getAppId());
            //context.setBatchCode(appConfig.getBatchCode());
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, "无法获取appConfig！", "消息提示", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }*/

    private void start() throws SchedulerException {

        /*if( needReset()) {
            SelectShedDialog dialog = new SelectShedDialog();
            dialog.pack();

            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            if (!dialog.getDialogResult())
                System.exit(0);
        }*/

        //loadAppConfig();

        //System.setProperty("awt.useSystemAAFontSettings", "on");
        //WSystem.setProperty("swing.aatext", "true");

        //<editor-fold defaultstate="collapsed" desc="#Set ui font ">
        Font font = new Font("微软雅黑", Font.PLAIN,12);
        UIManager.put("Button.font",font);
        UIManager.put("ToggleButton.font",font);
        UIManager.put("RadioButton.font",font);
        UIManager.put("CheckBox.font",font);
        UIManager.put("ColorChooser.font",font);
        UIManager.put("ToggleButton.font",font);
        UIManager.put("ComboBox.font",font);
        UIManager.put("ComboBoxItem.font",font);
        UIManager.put("InternalFrame.titleFont",font);
        UIManager.put("Label.font",font);
        UIManager.put("List.font",font);
        UIManager.put("MenuBar.font",font);
        UIManager.put("Menu.font",font);
        UIManager.put("MenuItem.font",font);
        UIManager.put("RadioButtonMenuItem.font",font);
        UIManager.put("CheckBoxMenuItem.font",font);
        UIManager.put("PopupMenu.font",font);
        UIManager.put("OptionPane.font",font);
        UIManager.put("Panel.font",font);
        UIManager.put("ProgressBar.font",font);
        UIManager.put("ScrollPane.font",font);
        UIManager.put("Viewport",font);
        UIManager.put("TabbedPane.font",font);
        UIManager.put("TableHeader.font",font);
        UIManager.put("TextField.font",font);
        UIManager.put("PasswordFiled.font",font);
        UIManager.put("TextArea.font",font);
        UIManager.put("TextPane.font",font);
        UIManager.put("EditorPane.font",font);
        UIManager.put("TitledBorder.font",font);
        UIManager.put("ToolBar.font",font);
        UIManager.put("ToolTip.font",font);
        UIManager.put("Tree.font",font);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="#Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            Logger.getLogger(FlfsApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            ServiceRegistry registry = ServiceRegistryImpl.getInstance();
            HibernateService hibernateService = (HibernateService)registry.getService(Services.HIBERNATE_SERVICE);
            JFrame frame = new MainFrame(hibernateService);

            /*frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    try {
                        new Keeper()
                                .startJobs();

                    } catch (SchedulerException ex) {
                        ex.printStackTrace();
                    }
                }
            });*/
        });
    }

    private void destory() {

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            FlfsApp app = new FlfsApp();
            app.start();
        } catch (ServiceException | SchedulerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "消息提示", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }


}
