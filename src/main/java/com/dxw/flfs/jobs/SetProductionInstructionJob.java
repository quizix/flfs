/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;

/**
 * 发送当日做料任务和时间校准作业
 * 每天早6:00和晚6:00
 * @author pronics3
 */
public class SetProductionInstructionJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        notify("开始下达做料信息");
        PlcDelegate proxy = PlcDelegateFactory.getPlcProxy();
        proxy.setProductionParam(0, 0, 0, new short[]{1,2,3});
    }
    
}
