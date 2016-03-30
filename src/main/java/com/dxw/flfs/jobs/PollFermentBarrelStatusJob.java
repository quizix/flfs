/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyFactory;

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
        PlcProxy proxy = PlcProxyFactory.getPrimaryPlcProxy();
        
        boolean[] status = proxy.getFermentBarrelStatus();
    }
    
}
