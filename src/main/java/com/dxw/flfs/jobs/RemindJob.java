package com.dxw.flfs.jobs;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationTags;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.SiteConfig;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by zhang on 2016-04-30.
 */
public class RemindJob extends AbstractJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        HibernateService hibernateService = (HibernateService)
                ServiceRegistryImpl.getInstance().getService(Services.HIBERNATE_SERVICE);
        try (UnitOfWork unitOfWork = new UnitOfWork(hibernateService.getSession())) {
            String siteCode = FlfsApp.getContext().getSiteCode();
            SiteConfig config = unitOfWork.getSiteConfig(siteCode);

            if( config != null){
                //stage==0表示还是处于入栏阶段
                if( config.getStage() ==0){
                    if(notificationManager != null){
                        Notification n = new Notification();
                        n.setContent("系统提示：请输入明天的入栏计划");
                        n.setWhen( System.currentTimeMillis());
                        notificationManager.notify(NotificationTags.Remind, n);
                    }
                }
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
