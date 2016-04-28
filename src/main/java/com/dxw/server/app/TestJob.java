/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.server.app;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.Services;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author pronics3
 */
public class TestJob implements Job{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
            //System.out.println(TimeUtil.getCurrentDateTime());
            
            ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        
        NotificationManager notificationManger = (NotificationManager)registry.getService(Services.NOTIFICATION_MANAGER);
        
        if(notificationManger != null){
            Notification notification = new Notification();
            notification.setWhen(System.currentTimeMillis());
            notificationManger.notify("job", notification);
        }
    }
    
}
