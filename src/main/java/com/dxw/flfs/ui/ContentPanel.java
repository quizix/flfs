package com.dxw.flfs.ui;

import javax.swing.*;

/**
 * Created by Administrator on 2016/4/2.
 */
public class ContentPanel {
    private JPanel root;
    private JTabbedPane tabbedPane1;
    private RemindPanel remindPanel;
    private NotificationPanel notificationPanel;

    private void createUIComponents() {

        tabbedPane1 = new JTabbedPane();
        remindPanel = new RemindPanel(tabbedPane1);
        notificationPanel = new NotificationPanel(tabbedPane1);


    }
}
