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
 *
 * @author pronics3
 */
public class PollMixingBarrelStatusJob extends AbstractJob{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        notify("开始轮询搅拌桶状态");
        PlcProxy proxy = PlcProxyImpl.getInstance();
     
        short status = proxy.getMixingBarrelStatus();
    }
    
}
