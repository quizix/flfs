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
 * 轮询料塔状态
 * @author pronics3
 */
public class PollMaterialTowerStatusJob extends AbstractJob{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        notify("开始轮询料塔状态");
        PlcDelegate proxy = PlcDelegateFactory.getPlcProxy();

        boolean[] status = proxy.getMaterialTowerStatus();

        if( status != null){
            notifyData(NotificationFlags.MATERIAL_TOWER_STATUS, status);

        }

    }
    
}
