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

    /**
     * 初始化系统服务
     *
     * @throws ServiceException
     */
    static void initServices() throws ServiceException {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        NotificationManager notificationManager = new NotificationManagerImpl();
        notificationManager.init();
        registry.register(notificationManager);

        HibernateService hibernateService = new HibernateServiceImpl();
        hibernateService.init();
        registry.register(hibernateService);

        //prepare the data.
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            dao.begin();

            Shed shed = new Shed();
            shed.setCreateTime(new Date());
            shed.setModifyTime(new Date());
            shed.setAddress("江西鄱阳");
            shed.setCode("12345678");
            shed.setName("猪舍1");
            dao.update(shed);

            Batch batch = new Batch();
            batch.setCode("1");
            batch.setInStockNumber(100);
            batch.setInStockDate(new Date());
            batch.setInStockDuration(10);
            batch.setCreateTime(new Date());
            batch.setModifyTime(new Date());

            dao.update(batch);

            Set<Batch> batches = new HashSet<>();
            batches.add(batch);

            Set<Sty> sties = new HashSet<>();
            for (int i = 0; i < 24; i++) {
                Sty sty = new Sty();
                sty.setCreateTime(new Date());
                sty.setModifyTime(new Date());
                sty.setCode(Integer.toString(i));
                sty.setName("Sty" + i);
                sty.setLastNumber(80 + i);
                sty.setCurrentNumber(100 + i);
                sty.setShed(shed);
                sties.add(sty);
                sty.setNo(i);

                dao.update(sty);
            }

            batch.setSties(sties);

            dao.update(batch);

            AppConfig config = new AppConfig();
            config.setCreateTime(new Date());
            config.setModifyTime(new Date());
            config.setBatchCode("1");
            config.setAppId(FlfsApp.getAppId());
            config.setHost("192.168.1.10");
            dao.update(config);

            dao.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
