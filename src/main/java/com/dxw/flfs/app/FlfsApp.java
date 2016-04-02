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
import com.dxw.flfs.jobs.PollFermentBarrelStatusJob;
import com.dxw.flfs.jobs.PollMaterialTowerStatusJob;
import com.dxw.flfs.jobs.PollMixingBarrelStatusJob;
import com.dxw.flfs.jobs.SetProductionInstructionJob;
import com.dxw.flfs.ui.MainFrame;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Fermented Liquiding Feeding System
 * 发酵液态料饲喂系统
 * @author pronics3
 */
public class FlfsApp {
    
    private NotificationManager notificationManager;
    
    private FlfsApp()
            throws ServiceException, SchedulerException{
        
    }
    
    private void init() throws ServiceException, SchedulerException{
        initServices();
        
    }

    /**
     * 初始化系统服务
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
     * @throws SchedulerException 
     */
    private void initJobs() throws SchedulerException {
        SchedulerFactory f = new StdSchedulerFactory();
        Scheduler s = f.getScheduler();
        
        scheduleFermentBarrelPollJob(s);
        scheduleMaterialTowerPollJob(s);
        scheduleMixingBarrelPollJob(s);
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
                                .withIntervalInMinutes(10)
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
    
    private void start() throws SchedulerException{
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
        } catch (ClassNotFoundException | InstantiationException 
                | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FlfsApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrame();

            try {
                initJobs();
            } catch (SchedulerException ex) {
                Logger.getLogger(FlfsApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    private void destory(){
        
    }
   /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try{
            FlfsApp app = new FlfsApp();
            app.init();
            app.start();
        }
        catch(ServiceException | SchedulerException ex){
            System.out.println(ex.getMessage());
        }
    }
}
