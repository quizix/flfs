package com.dxw.flfs.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainFrame extends JFrame {


    public MainFrame(){
        initComponents();
    }

    private void initComponents() {

        this.setTitle("发酵式液态饲料饲喂系统——[稻香湾科技]");
        this.setMinimumSize(new Dimension(600,400));
        this.setContentPane(new MainPanel().getRoot());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JMenuBar bar = new javax.swing.JMenuBar();
        this.setJMenuBar(bar);

        JMenu menu = new JMenu();
        menu.setText("系统");
        bar.add(menu);

    }
}
