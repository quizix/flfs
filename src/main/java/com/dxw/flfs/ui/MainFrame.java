package com.dxw.flfs.ui;

import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.ui.dialogs.BatchDialog;
import com.dxw.flfs.ui.dialogs.ShedDialog;
import com.dxw.flfs.ui.dialogs.StockDialog;
import com.dxw.flfs.ui.wizards.SelectShedDialog;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainFrame extends JFrame {

    HibernateService hibernateService;
    public MainFrame(HibernateService hibernateService){
        this.hibernateService = hibernateService;
        initComponents();
    }

    private void initComponents() {

        this.setTitle("发酵式液态饲料饲喂系统——[稻香湾科技]");
        this.setMinimumSize(new Dimension(800,600));
        this.setContentPane(new MainPanel().getRoot());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

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

        JMenuItem miStock = new JMenuItem();
        miStock.setText("库存管理");
        menuManage.add(miStock);

        miExit.addActionListener(e->{
            System.exit(0);
        });


        miShed.addActionListener(e->{
            ShedDialog dialog = new ShedDialog(hibernateService);
            dialog.setTitle("猪舍管理");
            dialog.setSize(800,600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);

        });

        miBatch.addActionListener(e->{
            BatchDialog dialog = new BatchDialog(hibernateService);
            dialog.setTitle("批次管理");
            dialog.setSize(800,600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        miStock.addActionListener(e->{
            StockDialog dialog = new StockDialog();
            dialog.setTitle("库存管理");
            dialog.setSize(800,600);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

    }
}
