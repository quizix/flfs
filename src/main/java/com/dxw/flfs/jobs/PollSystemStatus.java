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

import static groovy.xml.Entity.not;

/**
 * 轮询系统状态
 * 每分钟执行一次
 *
 * @author pronics3
 */
public class PollSystemStatus extends AbstractJob {

    private static int phCountDown = 0;
    private static int materialTowerCountDown = 0;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();

        notify("开始轮询系统状态");
        //系统状态
        {
            Short status = delegate.getSystemStatus();
            notifyData(NotificationFlags.SYSTEM_STATUS, status);
        }

        //查询料塔状态:10分钟执行一次
        if(materialTowerCountDown-- ==0)
        {
            boolean[] status = delegate.getMaterialTowerStatus();

            if (status != null) {
                notifyData(NotificationFlags.MATERIAL_TOWER_STATUS, status);

            }
            materialTowerCountDown = 10;
        }
        //搅拌桶状态
        {
            Short status = delegate.getMixingBarrelStatus();
            notifyData(NotificationFlags.SYSTEM_STATUS, status);
        }

        //发酵罐的空和满状态
        {
            boolean[] status = delegate.getFermentBarrelStatus();
            if (status != null) {
                notifyData(NotificationFlags.FERMENT_BARREL_STATUS, status);
            }
        }

        //发酵罐中的出罐和入罐
        {
            short[] data = delegate.getFermentBarrelAction();
            if (data != null) {
                notifyData(NotificationFlags.FERMENT_BARREL_ACTION, data);
            }
        }


        //发酵罐PH值:60分钟执行一次
        if (phCountDown-- == 0) {
            Float value = delegate.getPhValue();
            if (value != null)
                notifyData(NotificationFlags.FERMENT_BARREL_PH_VALUE, value);
            phCountDown = 60;

        }


    }

}
