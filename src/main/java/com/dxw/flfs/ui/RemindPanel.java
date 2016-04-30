package com.dxw.flfs.ui;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.ms.NotificationTags;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.ui.dialogs.config.PlanConfigDialog;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

/**
 * Created by zhang on 2016-04-30.
 */
public class RemindPanel {
    private JPanel panel1;
    private JTable tableRemind;

    DefaultTableModel planDataModel;

    String[] columns = {"时间", "描述"};
    private NotificationManager notificationManger;

    public RemindPanel() {

        ServiceRegistry registry = ServiceRegistryImpl.getInstance();

        notificationManger = (NotificationManager) registry.getService(Services.NOTIFICATION_MANAGER);

        if (notificationManger != null) {
            notificationManger.addReceiver((String tag, Notification notification) -> {
                onNotify(tag, notification);
            });
        }
    }

    private void onNotify(String tag, Notification notification) {
        if (tag.equals(NotificationTags.Remind)) {
            planDataModel.addRow(new Object[]{
                    new Date(notification.getWhen()), notification.getContent()
            });
            planDataModel.fireTableRowsInserted(planDataModel.getRowCount() - 1,
                    planDataModel.getRowCount() - 1);

        }
    }

    private void createUIComponents() {
        planDataModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tableRemind = new JTable(planDataModel);

        planDataModel.addTableModelListener(e -> {
            int type = e.getType();
            if (type == TableModelEvent.INSERT || type == TableModelEvent.DELETE) {
            }
        });

        tableRemind.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(e.getClickCount()==2 && !e.isConsumed()){
                    HibernateService hibernateService = (HibernateService)
                            ServiceRegistryImpl.getInstance().getService(Services.HIBERNATE_SERVICE);
                    try (UnitOfWork unitOfWork = new UnitOfWork(hibernateService.getSession())) {
                        PlanConfigDialog dialog = new PlanConfigDialog(unitOfWork);
                        dialog.setTitle("入栏计划");
                        dialog.setSize(800, 600);
                        dialog.setLocationRelativeTo(null);
                        dialog.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

    }


}
