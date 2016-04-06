package com.dxw.flfs.jobs;

import com.dxw.common.ms.NotificationFlags;
import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Administrator on 2016/4/6.
 * 轮询发酵状态
 */
public class PollFermentStatusJob extends AbstractJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        PlcProxy proxy = PlcProxyFactory.getPrimaryPlcProxy();

        float value = proxy.getPhValue();

        notifyData(NotificationFlags.FERMENT_BARREL_PH_VALUE, value);

    }
}

