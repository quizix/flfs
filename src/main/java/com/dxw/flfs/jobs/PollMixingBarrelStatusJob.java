/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import com.dxw.common.ms.NotificationFlags;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyFactory;

/**
 *
 * @author pronics3
 */
public class PollMixingBarrelStatusJob extends AbstractJob{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        notify("开始轮询搅拌桶状态");
        PlcProxy proxy = PlcProxyFactory.getPlcProxy();
        Short status = proxy.getMixingBarrelStatus();

        //if( status != null)
          //  notifyData(NotificationFlags.MIXING_BARREL_STATUS, status);
    }
    
}
