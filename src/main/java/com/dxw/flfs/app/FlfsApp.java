/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.app;

import com.dxw.common.ms.NotificationManager;
import com.dxw.common.ms.NotificationManagerImpl;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.HibernateServiceImpl;
import com.dxw.flfs.jobs.*;
import com.dxw.flfs.ui.MainFrame;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Fermented Liquiding Feeding System
 * 发酵液态料饲喂系统
 *
 * @author pronics3
 */
public class FlfsApp {

    private NotificationManager notificationManager;

    private FlfsApp()
            throws ServiceException, SchedulerException {

    }

    private void init() throws ServiceException, SchedulerException {
        initServices();

    }

    /**
     * 初始化系统服务
     *
     * @throws ServiceException
     */
    private void initServices() throws ServiceException {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        notificationManager = new NotificationManagerImpl();
        notificationManager.init();
        registry.register(notificationManager);

        HibernateService hibernateService = new HibernateServiceImpl();
        hibernateService.init();
        registry.register(hibernateService);

    }


    /**
     * 初始化系统Job
     *
     * @throws SchedulerException
     */
    private void initJobs() throws SchedulerException {
        SchedulerFactory f = new StdSchedulerFactory();
        Scheduler s = f.getScheduler();

        scheduleFermentBarrelPollJob(s);
        scheduleMaterialTowerPollJob(s);
        scheduleMixingBarrelPollJob(s);
        scheduleFermentStatusPollJob(s);
        scheduleProductionInstructionJob(s);

        s.start();
    }

    private void scheduleFermentBarrelPollJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(PollFermentBarrelStatusJob.class)
                .withIdentity("pollFermentBarrwlStatusJob", "flfsGroup")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("pollFermentBarrwlStatusTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(1)
                                .repeatForever())
                .build();
        s.scheduleJob(job, trigger);
    }

    private void scheduleMaterialTowerPollJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(PollMaterialTowerStatusJob.class)
                .withIdentity("materialTowerStatusPollJob", "flfsGroup")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("materialTowerStatusPollTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(10)
                                .repeatForever())
                .build();
        s.scheduleJob(job, trigger);
    }

    private void scheduleMixingBarrelPollJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(PollMixingBarrelStatusJob.class)
                .withIdentity("mixingBarrelStatusPollJob", "flfsGroup")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("mixingBarrelStatusPollTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(1)
                                .repeatForever())
                .build();
        s.scheduleJob(job, trigger);
    }

    private void scheduleFermentStatusPollJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(PollFermentStatusJob.class)
                .withIdentity("fermentStatusPollJob", "flfsGroup")
                .build();

        //每半个小时读取一次数据
        Trigger trigger = newTrigger()
                .withIdentity("fermentStatusPollTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(30)
                                .repeatForever())
                .build();
        s.scheduleJob(job, trigger);
    }



    private void scheduleProductionInstructionJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(SetProductionInstructionJob.class)
                .withIdentity("productionInstructionJob", "flfsGroup")
                .build();
        //使用cronSchedule， 0 18 5/12 * * ?，表示6点开始，每12个小时执行一次
        Trigger trigger = newTrigger()
                .withIdentity("productionInstructionTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        cronSchedule("0 20 5/12 * * ?"))
                .build();
        s.scheduleJob(job, trigger);
    }

    private void start() throws SchedulerException {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        //<editor-fold defaultstate="collapsed" desc=" Set ui font ">
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

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
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
            JFrame frame = new MainFrame();
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    try {
                        initJobs();
                    } catch (SchedulerException e1) {
                        e1.printStackTrace();
                    }
                }
            });
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
            app.init();
            app.start();
        } catch (ServiceException | SchedulerException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
