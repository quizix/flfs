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
 * 每分钟执行一次
 * @author pronics3
 */
public class PollFermentBarrelStatusJob extends AbstractJob{
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();

        notify("开始轮询发酵罐状态");
        //发酵罐的空和满状态
        boolean[] status = delegate.getFermentBarrelStatus();
        if( status != null){
            notifyData(NotificationFlags.FERMENT_BARREL_STATUS, status);
        }

        //发酵罐中的出罐和入罐
        short[] data = delegate.getFermentBarrelAction();
        if( data != null){
            notifyData(NotificationFlags.FERMENT_BARREL_ACTION, data);
        }
    }
    
}
