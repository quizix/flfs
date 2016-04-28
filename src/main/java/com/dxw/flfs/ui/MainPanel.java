package com.dxw.flfs.ui;

import com.dxw.flfs.app.Keeper;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.ui.dialogs.config.SiteConfigDialog;

import javax.swing.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainPanel {
    HibernateService hibernateService;

    public MainPanel(HibernateService hibernateService) {
        this.hibernateService = hibernateService;

        btnStart.addActionListener(e -> {
            startSystem();
        });
        btnStop.addActionListener(e -> {
            stopSystem();

        });
        btnClean.addActionListener(e -> {
            cleanSystem();

        });
    }

    private void startSystem() {
        try(UnitOfWork uow = new UnitOfWork(hibernateService.getSession())){

            SiteConfigDialog dialog = new SiteConfigDialog(uow);
            dialog.setTitle("猪舍管理");
            dialog.setSize(800,600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

            if( dialog.getDialogResult() == 1){
                //启动

                this.btnStart.setEnabled(false);
                this.btnClean.setEnabled(true);
                this.btnStop.setEnabled(true);

                PlcDelegate plcProxy = PlcDelegateFactory.getPlcDelegate();
                //发送做料指令


                //启动做料
                plcProxy.start();

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
