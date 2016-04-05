/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import org.quartz.Job;

/**
 *
 * @author pronics3
 */
public abstract class AbstractJob implements Job {

    NotificationManager notificationManager;

    public AbstractJob() {
        this.notificationManager = (NotificationManager) ServiceRegistryImpl.getInstance()
                .lookupService(Services.NOTIFICATION_MANAGER);;
    }
    
    protected void notify(String message){
        if(notificationManager != null){
            Notification n = new Notification();
            n.setContent(message);
            n.setWhen( System.currentTimeMillis());
            notificationManager.notify("JOB", n);
        }
    }

    protected void notifyData(int flag, Object data){
        if(notificationManager != null){
            Notification n = new Notification();
            n.setFlag(flag);
            n.setContent(data);
            n.setWhen( System.currentTimeMillis());
            notificationManager.notify("DATA", n);
        }
    }

}
