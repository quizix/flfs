/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.jobs;

import com.dxw.common.ms.NotificationFlags;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;

/**
 * 轮询发酵罐状态作业
 * 
 * @author pronics3
 */
public class PollFermentBarrelStatusJob extends AbstractJob{
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        PlcDelegate proxy = PlcDelegateFactory.getPlcProxy();

        notify("开始轮询发酵罐状态");
        boolean[] status = proxy.getFermentBarrelStatus();
        if( status != null){
            notifyData(NotificationFlags.FERMENT_BARREL_STATUS, status);
        }

        short[] data = proxy.getFermentBarrelAction();
        if( data != null){
            notifyData(NotificationFlags.FERMENT_BARREL_ACTION, data);
        }
    }
    
}
