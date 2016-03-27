/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyImpl;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 发送当日做料任务和时间校准作业
 * 每天早6:00和晚6:00
 * @author pronics3
 */
public class SetProductionInstructionJob extends AbstractJob {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        
        notify("开始下达做料信息");
        PlcProxy proxy = PlcProxyImpl.getInstance();
        proxy.setProductionParam(0, 0, 0, new float[]{1,2,3});
        //proxy.setTimeCalibration();
    }
    
}
