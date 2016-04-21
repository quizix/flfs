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
 *
 * @author pronics3
 */
public class PollMixingBarrelStatusJob extends AbstractJob{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        notify("开始轮询搅拌桶状态");
        PlcDelegate proxy = PlcDelegateFactory.getPlcDelegate();
        Short status = proxy.getMixingBarrelStatus();

        //if( status != null)
          //  notifyData(NotificationFlags.MIXING_BARREL_STATUS, status);
    }
    
}
