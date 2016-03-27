/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import com.dxw.common.ms.NotificationManager;
import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 轮询发酵罐状态作业
 * 
 * @author pronics3
 */
public class PollFermentBarrelStatusJob extends AbstractJob{
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        notify("开始轮询发酵罐状态");
        //
        PlcProxy proxy = PlcProxyImpl.getInstance();
        
        short[] status = proxy.getFermentBarrelStatus();
    }
    
}
