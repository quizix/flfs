package com.dxw.flfs.ui;

import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.communication.PlcModelField;
import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyFactory;
import com.dxw.flfs.communication.PlcProxyModel;

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
    PlcProxy proxy;

    public DataPanel() {
        iconAlert = new ImageIcon(this.getClass().getResource("/images/alert-icon.png"));
        ServiceRegistry r = ServiceRegistryImpl.getInstance();
        notificationManager = (NotificationManager) r.lookupService(Services.NOTIFICATION_MANAGER);

        proxy = PlcProxyFactory.getPlcProxy();
        proxy.addModelChangedListener( event-> {
            long field =event.getField();
            PlcProxyModel model = event.getModel();

            if( field == PlcModelField.FERMENT_BARREL_STATUS){
                boolean[] data = model.getFermentBarrelStatus();
                for (int i = 0; i < Math.min(data.length, fermentData.length); i++) {
                    fermentData[i][1] = (data[i]) ? "满" : "空";
                    dataModel.fireTableCellUpdated(i, 1);
                }
                dataModel.fireTableDataChanged();
            }
            else if( field == PlcModelField.MATERIAL_TOWER_ALARM){
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
            }
            else if( field == PlcModelField.FERMENT_BARREL_IN_OUT){
                short in = model.getFermentBarrelInNo();
                short out = model.getFermentBarrelOutNo();
                lblFermentBarrelIn.setText(Short.toString(in));
                lblFermentBarrelOut.setText(Short.toString(out));
            }
            else if( field == PlcModelField.MIXING_BARREL_STATUS){
                Short status = model.getMixingBarrelStatus();
                lblMixingBarrelStatus.setText(status==0 ? "空闲":"运行");
            }
            else if( field == PlcModelField.PH_VALUE){
                float ph = model.getPh();
                lblPh.setText(Float.toString(ph));
            }

        } );
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
