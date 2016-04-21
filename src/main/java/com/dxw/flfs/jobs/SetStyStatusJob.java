/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import com.dxw.common.models.Batch;
import com.dxw.common.models.Sty;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;
import org.hibernate.mapping.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Set;

/**
 * 发送栏位空/满信息
 * 每一分钟执行一次
 * @author pronics3
 */
public class SetStyStatusJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        notify("开始发送栏位空/满信息");
        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();

        short[] status = getStyStatus();

        if(status != null){
            delegate.setStyStatus(status);
        }

    }

    private short[] getStyStatus(){
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        HibernateService hibernateService = (HibernateService)registry.lookupService(Services.HIBERNATE_SERVICE);

        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {

            Batch batch = dao.findBatchByCode(FlfsApp.getContext().getBatchCode());

            Set<Sty> sties = batch.getSties();
            int size = sties.size();
            short[] status = new short[size];
            int i=0;
            for(Sty sty: sties){
                status[i++] = (short)((sty.getCurrentNumber()==0)?0:1);
            }
            return status;

        } catch (Exception e) {
            notify("获取栏位信息失败！");
        }
        return null;
    }
    
}
