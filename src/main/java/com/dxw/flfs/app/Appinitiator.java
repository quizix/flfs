package com.dxw.flfs.app;

import com.dxw.common.ms.NotificationManager;
import com.dxw.common.ms.NotificationManagerImpl;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

/**
 * Created by Administrator on 2016/4/7.
 */
public class AppInitiator {

    ServiceRegistry registry;
    DbInitiator dbInitializer;
    public AppInitiator(ServiceRegistry registry){
        this.registry = registry;
        dbInitializer = new DbInitiator(registry);
    }
    /**
     * 初始化系统服务
     *
     * @throws ServiceException
     */
    public void initServices() throws ServiceException {
        registerNotificationService();

        dbInitializer.registerService();

        //dbInitializer.prepareData();
    }

    private void registerNotificationService() throws ServiceException {
        NotificationManager notificationManager = new NotificationManagerImpl();
        notificationManager.init();
        registry.register(notificationManager);
    }

}
