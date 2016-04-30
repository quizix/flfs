package com.dxw.flfs.ui;

import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.app.Keeper;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.SiteConfig;
import com.dxw.flfs.ui.dialogs.BatchDialog;
import com.dxw.flfs.ui.dialogs.ShedDialog;
import com.dxw.flfs.ui.dialogs.config.PlanConfigDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainFrame extends JFrame {

    HibernateService hibernateService;

    public MainFrame(HibernateService hibernateService) {
        this.hibernateService = hibernateService;
        initComponents();
    }

    MainPanel mainPanel;

    private void initComponents() {
        this.setTitle("发酵式液态饲料饲喂系统——[稻香湾科技]");
        this.setMinimumSize(new Dimension(800, 600));
        mainPanel = new MainPanel(hibernateService);
        this.setContentPane(mainPanel.getRoot());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                MainFrame.this.onInit();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                MainFrame.this.onDestroy();
            }
        });

        JMenuBar bar = new JMenuBar();
        this.setJMenuBar(bar);

        JMenu menuSystem = new JMenu();
        menuSystem.setText("系统");
        bar.add(menuSystem);

        JMenuItem miExit = new JMenuItem();
        miExit.setText("退出");
        menuSystem.add(miExit);

        JMenu menuManage = new JMenu();
        menuManage.setText("管理");
        bar.add(menuManage);

        JMenuItem miShed = new JMenuItem();
        miShed.setText("猪舍管理");
        menuManage.add(miShed);

        JMenuItem miBatch = new JMenuItem();
        miBatch.setText("批次管理");
        menuManage.add(miBatch);

        JMenuItem miPlan = new JMenuItem();
        miPlan.setText("进猪计划");
        menuManage.add(miPlan);

        miExit.addActionListener(e -> {
            System.exit(0);
        });


        miShed.addActionListener(e -> {
            ShedDialog dialog = new ShedDialog(hibernateService);
            dialog.setTitle("猪舍管理");
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

        });

        miBatch.addActionListener(e -> {
            BatchDialog dialog = new BatchDialog(hibernateService);
            dialog.setTitle("批次管理");
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        miPlan.addActionListener(e -> {

            try (UnitOfWork uow = new UnitOfWork(hibernateService.getSession())) {
                PlanConfigDialog dialog = new PlanConfigDialog(uow);

                dialog.setTitle("进猪计划");
                dialog.setSize(800, 600);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void onInit() {
        String siteCode = FlfsApp.getContext().getSiteCode();

        try (UnitOfWork uow = new UnitOfWork(hibernateService.getSession())) {
            SiteConfig siteConfig = uow.getSiteConfig(siteCode);

            if (siteConfig == null) {
                JOptionPane.showMessageDialog(null, "无法获取应用程序配置信息！", "消息提示", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            } else {
                if (siteConfig.getStatus() == 0) {
                    //stopped
                    mainPanel.setActionEnable("start", true);
                    mainPanel.setActionEnable("stop", false);
                    mainPanel.setActionEnable("clean", true);
                } else {
                    //started
                    mainPanel.setActionEnable("start", false);
                    mainPanel.setActionEnable("stop", true);
                    mainPanel.setActionEnable("clean", true);

                    //status为started，则自动启动后台定时服务
                    new Keeper().startJobs();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDestroy() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        try {
            registry.dispose();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
