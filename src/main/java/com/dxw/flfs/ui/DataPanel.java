package com.dxw.flfs.ui;

import com.dxw.common.ms.NotificationFlags;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * Created by Administrator on 2016/4/4.
 */
public class DataPanel {
    private JTable table1;
    private JPanel root;
    private JLabel lblFermentBarrelIn;
    private JLabel lblFermentBarrelOut;
    private JLabel lblPh;
    private JLabel lblMaterialTowerLow;
    private JLabel lblMaterialTowerEmpty;
    private ImageIcon iconAlert;
    private AbstractTableModel dataModel;

    private String[] fermentBarrelcolumnNames = {"发酵罐#", "状态"};

    private Object[][] fermentData = {
            {"1", "空"},
            {"2", "空"},
            {"3", "空"},
            {"4", "空"},
            {"5", "空"},
            {"6", "空"},
            {"7", "空"},
    };

    NotificationManager notificationManager;

    public DataPanel() {
        iconAlert = new ImageIcon(this.getClass().getResource("/images/alert-icon.png"));
        ServiceRegistry r = ServiceRegistryImpl.getInstance();

        notificationManager = (NotificationManager) r.lookupService(Services.NOTIFICATION_MANAGER);
        if (notificationManager != null) {
            notificationManager.addReceiver((tag, notification) -> {
                if (tag.equals("DATA")) {

                    switch (notification.getFlag()) {
                        case NotificationFlags.FERMENT_BARREL_STATUS: {
                            boolean[] data = (boolean[])notification.getContent();
                            for(int i=0;i<data.length;i++){
                                fermentData[i][1] = (data[i])?"满":"空";
                                dataModel.fireTableCellUpdated(i,1);
                            }
                            dataModel.fireTableDataChanged();
                        }
                        break;
                        case NotificationFlags.MATERIAL_TOWER_STATUS:{
                            boolean[] data = (boolean[])notification.getContent();

                            if( data[0]){
                                lblMaterialTowerLow.setIcon( iconAlert);
                                lblMaterialTowerLow.setText("");
                            }
                            else{
                                lblMaterialTowerLow.setIcon( null);
                                lblMaterialTowerLow.setText("正常");
                            }
                        }
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void createUIComponents() {
        dataModel = new AbstractTableModel() {
            public int getColumnCount() {
                return 2;
            }

            public int getRowCount() {
                return fermentData.length;
            }

            public Object getValueAt(int row, int col) {
                return fermentData[row][col];
            }

            public String getColumnName(int col) {
                return fermentBarrelcolumnNames[col];
            }

        };

        table1 = new JTable(dataModel);
    }
}
