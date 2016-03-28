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
 * 轮询料塔状态
 * @author pronics3
 */
public class PollMaterialTowerStatusJob extends AbstractJob{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        notify("开始轮询料塔状态");
        PlcProxy proxy = PlcProxyImpl.getInstance();
        
        proxy.getMaterialTowerStatus();
    }
    
}
