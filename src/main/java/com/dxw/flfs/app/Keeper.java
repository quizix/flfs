package com.dxw.flfs.app;

import com.dxw.flfs.jobs.PollSystemStatus;
import com.dxw.flfs.jobs.SetProductionInstructionJob;
import com.dxw.flfs.jobs.SetStyStatusJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 执行定时任务
 * Created by zhang on 2016-04-21.
 */
public class Keeper {

    public Keeper() {

    }

    /**
     * 初始化系统Job
     * @throws SchedulerException
     */
    public void startJobs() throws SchedulerException {

        Scheduler s = new StdSchedulerFactory().getScheduler();

        scheduleSystemStatusPollJob(s);
        scheduleProductionInstructionJob(s);
        scheduleSetStyStatusJob(s);

        s.start();
    }
    private void scheduleSystemStatusPollJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(PollSystemStatus.class)
                .withIdentity("pollFermentBarrelStatusJob", "flfsGroup")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("pollFermentBarrelStatusTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(1)
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

    private void scheduleSetStyStatusJob(Scheduler s) throws SchedulerException {
        JobDetail job = newJob(SetStyStatusJob.class)
                .withIdentity("setStyStatusJob", "flfsGroup")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("setStyStatusJobPollTrigger", "flfsGroup")
                .startNow()
                .withSchedule(
                        simpleSchedule()
                                .withIntervalInMinutes(1)
                                .repeatForever())
                .build();
        s.scheduleJob(job, trigger);
    }
}
