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
            config.setHost("192.168.1.10");
            dao.update(config);

            dao.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /**
     * 初始化系统Job
     * @throws SchedulerException
     */
    static void initJobs() throws SchedulerException {
        SchedulerFactory f = new StdSchedulerFactory();
        Scheduler s = f.getScheduler();

        scheduleFermentBarrelPollJob(s);
        scheduleMaterialTowerPollJob(s);
        scheduleMixingBarrelPollJob(s);
        scheduleFermentStatusPollJob(s);
        scheduleProductionInstructionJob(s);

        s.start();
    }
    private static void scheduleFermentBarrelPollJob(Scheduler s) throws SchedulerException {
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

    private static void scheduleMaterialTowerPollJob(Scheduler s) throws SchedulerException {
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

    private static void scheduleMixingBarrelPollJob(Scheduler s) throws SchedulerException {
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

    private static void scheduleFermentStatusPollJob(Scheduler s) throws SchedulerException {
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



    private static void scheduleProductionInstructionJob(Scheduler s) throws SchedulerException {
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
}
