package com.dxw.flfs.ui;

import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.app.Keeper;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.SiteConfig;
import com.dxw.flfs.scheduling.FlfsScheduler;
import com.dxw.flfs.scheduling.ScheduleResult;
import com.dxw.flfs.ui.dialogs.config.SiteConfigWizard;

import javax.swing.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainPanel {
    HibernateService hibernateService;

    public MainPanel(HibernateService hibernateService) {
        this.hibernateService = hibernateService;

        btnStart.addActionListener(e -> startSystem());
        btnStop.addActionListener(e -> stopSystem());
        btnClean.addActionListener(e -> cleanSystem());
    }

    private void startSystem() {
        try (UnitOfWork uow = new UnitOfWork(hibernateService.getSession())) {

            SiteConfigWizard dialog = new SiteConfigWizard(uow);
            dialog.setTitle("系统启动设置");
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            if (dialog.getDialogResult() == 1) {
                //启动

                this.btnStart.setEnabled(false);
                this.btnStop.setEnabled(true);

                PlcDelegate plcDelegate = PlcDelegateFactory.getPlcDelegate();

                //发送做料指令
                ServiceRegistry registry = ServiceRegistryImpl.getInstance();
                FlfsScheduler scheduler = (FlfsScheduler) registry.getService(Services.SCHEDULER_SERVICE);
                ScheduleResult result =
                        scheduler.schedule6AM(uow);
                plcDelegate.setProductionParam(result.getWater(), result.getDry(), result.getBacteria(),
                        result.getBarrels());

                //启动做料
                plcDelegate.start();

                //修改siteConfig状态
                String siteCode = FlfsApp.getContext().getSiteCode();
                SiteConfig siteConfig = uow.getSiteConfig(siteCode);

                siteConfig.setModifyTime(new Date());
                siteConfig.setStatus(1);
                uow.getSiteConfigRepository().save(siteConfig);
                uow.commit();

                new Keeper().startJobs();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void cleanSystem() {
        PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
        plcProxy.clean();
    }

    private void stopSystem() {
        this.btnStart.setEnabled(true);
        this.btnStop.setEnabled(false);

        try (UnitOfWork uow = new UnitOfWork(hibernateService.getSession())) {
            String siteCode = FlfsApp.getContext().getSiteCode();
            SiteConfig siteConfig = uow.getSiteConfig(siteCode);

            siteConfig.setModifyTime(new Date());
            siteConfig.setStatus(0);
            uow.getSiteConfigRepository().save(siteConfig);
            uow.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
        plcProxy.halt();
    }


    public void setActionEnable(String action, boolean enable) {
        if (action.equals("start"))
            btnStart.setEnabled(enable);
        else if (action.equals("stop"))
            btnStart.setEnabled(enable);
        else if (action.equals("clean"))
            btnStart.setEnabled(enable);

    }

    public JPanel getRoot() {
        return root;
    }

    private JPanel root;
    private JButton btnStart;
    private JButton btnStop;
    private JButton btnClean;
}
