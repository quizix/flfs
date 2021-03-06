package com.dxw.flfs.ui;

import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.communication.PlcModel;
import com.dxw.flfs.communication.PlcModelField;

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
    private JLabel lblMixingBarrelStatus;
    private JLabel lblSystemStatus;
    private ImageIcon iconAlert;
    private AbstractTableModel dataModel;

    private String[] fermentBarrelColumnNames = {"发酵罐#", "状态"};

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
    PlcDelegate delegate;

    public DataPanel() {
        iconAlert = new ImageIcon(this.getClass().getResource("/images/alert-icon.png"));
        ServiceRegistry r = ServiceRegistryImpl.getInstance();
        notificationManager = (NotificationManager) r.getService(Services.NOTIFICATION_MANAGER);

        delegate = PlcDelegateFactory.getPlcDelegate();
        delegate.addModelChangedListener(event -> {
            long field = event.getField();
            PlcModel model = event.getModel();

            if (field == PlcModelField.SYSTEM_STATUS) {
                Short status = model.getSystemStatus();
                switch (status) {
                    case 1:
                        this.lblSystemStatus.setText("停机");
                        break;
                    case 2:
                        this.lblSystemStatus.setText("做料");
                        break;
                    case 3:
                        this.lblSystemStatus.setText("清洗");
                        break;
                    case 4:
                        this.lblSystemStatus.setText("紧停");
                        break;
                    case 5:
                        this.lblSystemStatus.setText("冷启动");
                        break;
                }

            } else if (field == PlcModelField.FERMENT_BARREL_STATUS) {
                boolean[] data = model.getFermentBarrelStatus();
                for (int i = 0; i < Math.min(data.length, fermentData.length); i++) {
                    fermentData[i][1] = (data[i]) ? "满" : "空";
                    dataModel.fireTableCellUpdated(i, 1);
                }
                dataModel.fireTableDataChanged();
            } else if (field == PlcModelField.MATERIAL_TOWER_ALARM) {
                Boolean lowAlarm = model.getMaterialTowerLowAlarm();
                Boolean emptyAlarm = model.getMaterialTowerEmptyAlarm();

                if (lowAlarm) {
                    lblMaterialTowerLow.setIcon(iconAlert);
                    lblMaterialTowerLow.setText("");
                } else {
                    lblMaterialTowerLow.setIcon(null);
                    lblMaterialTowerLow.setText("正常");
                }

                if (emptyAlarm) {
                    lblMaterialTowerEmpty.setIcon(iconAlert);
                    lblMaterialTowerEmpty.setText("");
                } else {
                    lblMaterialTowerEmpty.setIcon(null);
                    lblMaterialTowerEmpty.setText("正常");
                }
            } else if (field == PlcModelField.FERMENT_BARREL_IN_OUT) {
                short in = model.getFermentBarrelInNo();
                short out = model.getFermentBarrelOutNo();
                lblFermentBarrelIn.setText(Short.toString(in));
                lblFermentBarrelOut.setText(Short.toString(out));
            } else if (field == PlcModelField.MIXING_BARREL_STATUS) {
                Short status = model.getMixingBarrelStatus();
                lblMixingBarrelStatus.setText(status == 0 ? "空闲" : "运行");
            } else if (field == PlcModelField.PH_VALUE) {
                float ph = model.getPh();
                lblPh.setText(Float.toString(ph));
            }


        });
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
                return fermentBarrelColumnNames[col];
            }

        };

        table1 = new JTable(dataModel);
    }

}
