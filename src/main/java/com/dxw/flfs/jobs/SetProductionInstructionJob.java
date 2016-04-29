/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.scheduling.FlfsScheduler;
import com.dxw.flfs.scheduling.ScheduleResult;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 发送当日做料任务和时间校准作业
 * 每天早6:00和晚6:00
 *
 * @author pronics3
 */
public class SetProductionInstructionJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        HibernateService hibernateService = (HibernateService) ServiceRegistryImpl.getInstance()
                .getService(Services.HIBERNATE_SERVICE);

        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();
        try (UnitOfWork uow = new UnitOfWork(hibernateService.getSession())) {
            ServiceRegistry registry = ServiceRegistryImpl.getInstance();

            FlfsScheduler scheduler = (FlfsScheduler) registry.getService(Services.SCHEDULER_SERVICE);
            boolean amOrPm = TimeUtil.isAmOrPm();
            ScheduleResult result = amOrPm ? scheduler.schedule6AM(uow) : scheduler.schedule6PM(uow);
            notify("开始下达做料信息");
            delegate.setProductionParam(result.getWater(), result.getDry(), result.getBacteria(),
                    result.getBarrels());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
