package com.dxw.flfs.ui.dialogs;

import com.dxw.common.models.Batch;
import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

public class AddStyAssociationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tableShed;
    private JTable tableSty;
    private AbstractTableModel shedDataModel;
    private AbstractTableModel styDataModel;

    private Batch batch;

    HibernateService hibernateService;
    private String[] shedColumns = {"猪舍编号", "名称", "编码", "创建时间", "修改时间",
    };

    private String[] styColumns = {"栏位编号", "名称", "编码", "创建时间", "修改时间",
    };
    public AddStyAssociationDialog(HibernateService hibernateService, Batch batch) {
        this.hibernateService = hibernateService;
        this.batch = batch;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }

            @Override
            public void windowOpened(WindowEvent e){
                super.windowOpened(e);

                AddStyAssociationDialog.this.onLoad();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onLoad(){
        try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
            final List sheds = dao.findAllSheds();

            DefaultTableModel model = (DefaultTableModel) tableShed.getModel();
            model.getDataVector().removeAllElements();
            for (Object item : sheds) {
                Shed shed = (Shed)item;

                Object[] row = {shed.getId(), shed.getName(), shed.getCode(),
                        shed.getCreateTime(), shed.getModifyTime()
                };
                model.addRow(row);
            }
            model.fireTableDataChanged();

            if(model.getRowCount() >0)
                tableShed.setRowSelectionInterval(0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        shedDataModel = new DefaultTableModel(shedColumns, 0){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        shedDataModel.addTableModelListener( e->{
            int type = e.getType();
            if( type == TableModelEvent.INSERT || type == TableModelEvent.DELETE) {
                if(tableShed.getRowCount() >0)
                    tableShed.setRowSelectionInterval(0, 0);
            }
        });

        tableShed = new JTable(shedDataModel);
        tableShed.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        tableShed.getSelectionModel().addListSelectionListener(e -> {

            int rowIndex = tableShed.getSelectedRow();
            if (rowIndex == -1) {
                return;
            }

            Long id = (Long)(tableShed.getModel().getValueAt(rowIndex, 0));

            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                Shed shed = dao.findShedById(id);

                DefaultTableModel model = (DefaultTableModel) tableSty.getModel();
                model.getDataVector().removeAllElements();
                shed.getSties().stream()
                        .filter(sty -> !batch.getSties().contains(sty))
                        .forEach(sty -> {
                            Object[] row = {sty.getId(), sty.getName(), sty.getCode(),
                            sty.getCreateTime(), sty.getModifyTime()
                    };
                    model.addRow(row);
                });
                model.fireTableDataChanged();

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });



        styDataModel = new DefaultTableModel(styColumns, 0){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        tableSty = new JTable(styDataModel);
        styDataModel.addTableModelListener( e->{
            int type = e.getType();
            if( type == TableModelEvent.INSERT || type == TableModelEvent.DELETE)
                tableSty.setRowSelectionInterval(0, 0);


        });
        tableSty.getSelectionModel().addListSelectionListener(e -> {

            int rowIndex = tableSty.getSelectedRow();
            if (rowIndex == -1) {

                return;
            }


        });

    }
}

