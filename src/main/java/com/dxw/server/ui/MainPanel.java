package com.dxw.server.ui;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.communication.base.PlcConfig;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/2.
 */
public class MainPanel {
    public JPanel getRoot() {
        return root;
    }

    private JPanel root;
    private JTextArea txtMessage;
    private JRadioButton primaryRadioButton1;
    private JRadioButton secondlyRadioButton;

    NotificationManager notificationManager;

    public MainPanel(){
        init();
        primaryRadioButton1.addActionListener(e -> PlcConfig.ACTIVE = PlcConfig.PRIMARY);
        secondlyRadioButton.addActionListener(e -> PlcConfig.ACTIVE = PlcConfig.SECONDARY);
    }
    private void init() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();

        notificationManager = (NotificationManager) registry.getService(Services.NOTIFICATION_MANAGER);

        if (notificationManager != null) {
            notificationManager.addReceiver((String tag, Notification notification) -> {
                String message = String.format("[%S] %s %s\r\n", tag,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(notification.getWhen())),
                        notification.getContent().toString());
                txtMessage.append(message);
            });
        }
    }


}
