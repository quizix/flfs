package com.dxw.flfs.app;

import com.dxw.common.models.*;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.ms.NotificationManagerImpl;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.HibernateServiceImpl;
import com.dxw.flfs.jobs.*;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Administrator on 2016/4/7.
 */
public class AppInitializer {

    ServiceRegistry registry;
    DbInitializer dbInitializer;
    public AppInitializer(ServiceRegistry registry){
        this.registry = registry;
        dbInitializer = new DbInitializer(registry);
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
