package com.dxw.flfs.app;

import com.dxw.common.ms.NotificationManager;
import com.dxw.common.ms.NotificationManagerImpl;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.HibernateServiceImpl;
import com.dxw.flfs.scheduling.AdaptiveScheduler;
import com.dxw.flfs.scheduling.AdvancedDistributor;
import com.dxw.flfs.scheduling.FlfsScheduler;

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

        registerHibernateService();

        registerScheduler();

        //dbInitializer.prepareData();
    }

    private void registerScheduler() throws ServiceException {
        HibernateService hibernateService = (HibernateService)
                registry.getService(Services.SCHEDULER_SERVICE);
        FlfsScheduler scheduler = new AdaptiveScheduler(hibernateService,
                new AdvancedDistributor()
        );
        registry.register(scheduler);
    }

    private void registerNotificationService() throws ServiceException {
        NotificationManager notificationManager = new NotificationManagerImpl();
        notificationManager.init();
        registry.register(notificationManager);
    }

    private void registerHibernateService() throws ServiceException {
        HibernateService hibernateService = new HibernateServiceImpl();
        hibernateService.init();
        registry.register(hibernateService);
    }

}
