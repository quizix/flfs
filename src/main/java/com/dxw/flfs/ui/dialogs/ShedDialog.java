package com.dxw.flfs.ui.dialogs;

import com.dxw.flfs.data.models.Shed;
import com.dxw.flfs.data.models.Sty;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;

public class ShedDialog extends JDialog {
    private JPanel contentPane;

    private JTable tableShed;
    private JTable tableSty;
    private JButton btnAddShed;
    private JButton btnAddSty;
    private JTextField txtShedName;
    private JTextField txtShedCode;
    private JPanel pnlShedProperties;
    private JTextField txtShedAddress;
    private JButton btnShedAddOrSave;
    private JButton btnEditShed;
    private JButton btnEditSty;
    private JButton btnDeleteSty;
    private JTextField txtStyNo;
    private JTextField txtStyName;
    private JTextField txtStyCode;
    private JButton btnStyAddOrSave;
    private JPanel pnlStyProperties;
    private AbstractTableModel shedDataModel;
    private AbstractTableModel styDataModel;

    private String[] shedColumns = {"猪舍编号", "名称", "编码", "创建时间", "修改时间",
    };

    private String[] styColumns = {"栏位编号", "名称", "编码", "创建时间", "修改时间",
    };

    HibernateService hibernateService;

    public ShedDialog(HibernateService hibernateService) {
        this.hibernateService = hibernateService;

        setContentPane(contentPane);
        setModal(true);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);

                ShedDialog.this.onLoad();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        btnAddShed.addActionListener(e -> {
            this.btnShedAddOrSave.setText("添加");
            this.txtShedCode.setText("");
            this.txtShedAddress.setText("");
            this.txtShedName.setText("");
            this.pnlShedProperties.setVisible(true);
            this.txtShedName.requestFocus();
        });
        btnEditShed.addActionListener(e -> {
            this.btnShedAddOrSave.setText("修改");
            int rowIndex = tableShed.getSelectedRow();

            Long id = (Long)(tableShed.getModel().getValueAt(rowIndex, 0));

            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                Shed shed = dao.findShedById(id);

                if( shed != null){
                    this.txtShedName.setText( shed.getName());
                    this.txtShedCode.setText(shed.getCode());
                    this.txtShedAddress.setText(shed.getAddress());

                    this.pnlShedProperties.setVisible(true);
                    this.txtShedName.requestFocus();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });
        btnShedAddOrSave.addActionListener(e -> {
            if (btnShedAddOrSave.getText().equals("添加")) {
                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                    dao.begin();
                    Shed shed = new Shed();
                    shed.setCode(this.txtShedCode.getText());
                    shed.setAddress(this.txtShedAddress.getText());
                    shed.setName(this.txtShedName.getText());

                    shed.setActive(true);
                    Date now = new Date();
                    shed.setCreateTime(now);
                    shed.setModifyTime(now);

                    dao.update(shed);
                    dao.commit();

                    this.pnlShedProperties.setVisible(false);

                    DefaultTableModel model = (DefaultTableModel) tableShed.getModel();

                    Object[] row = {shed.getId(), shed.getName(), shed.getCode(),
                            shed.getCreateTime(), shed.getModifyTime()
                    };
                    model.addRow(row);
                    model.fireTableDataChanged();


                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            else if (btnShedAddOrSave.getText().equals("修改")) {
                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                    dao.begin();

                    Long id = getSelectedShedId();
                    Shed shed = dao.findShedById(id);
                    shed.setCode(this.txtShedCode.getText());
                    shed.setAddress(this.txtShedAddress.getText());
                    shed.setName(this.txtShedName.getText());
                    Date now = new Date();
                    shed.setModifyTime(now);
                    dao.update(shed);
                    dao.commit();

                    this.pnlShedProperties.setVisible(false);

                    DefaultTableModel model = (DefaultTableModel) tableShed.getModel();
                    int rowIndex = tableShed.getSelectedRow();
                    model.setValueAt(shed.getId(), rowIndex, 0);
                    model.setValueAt(shed.getName(), rowIndex, 1);
                    model.setValueAt(shed.getCode(), rowIndex, 2);
                    model.setValueAt(shed.getCreateTime(), rowIndex, 3);
                    model.setValueAt(shed.getModifyTime(), rowIndex, 4);

                    model.fireTableRowsUpdated(rowIndex, rowIndex);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        btnAddSty.addActionListener(e -> {
            this.btnStyAddOrSave.setText("添加");
            this.txtStyCode.setText("");
            this.txtStyNo.setText("");
            this.txtStyName.setText("");
            this.pnlStyProperties.setVisible(true);
            this.txtStyName.requestFocus();
        });
        btnEditSty.addActionListener(e -> {
            this.btnStyAddOrSave.setText("修改");
            Long id = getSelectedStyId();

            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                Sty sty = dao.findStyById(id);

                if( sty != null){
                    this.txtStyName.setText( sty.getName());
                    this.txtStyCode.setText(sty.getCode());
                    this.txtStyNo.setText(Integer.toString(sty.getNo()));

                    this.pnlStyProperties.setVisible(true);
                    this.txtStyName.requestFocus();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        btnStyAddOrSave.addActionListener(e -> {
            if (btnStyAddOrSave.getText().equals("添加")) {
                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                    Long id = getSelectedShedId();

                    dao.begin();

                    Shed shed = dao.findShedById(id);

                    Sty sty = new Sty();
                    sty.setCode(this.txtStyCode.getText());
                    sty.setNo(Integer.parseInt(this.txtStyNo.getText()));
                    sty.setName(this.txtStyName.getText());

                    sty.setCurrentNumber(0);
                    sty.setLastNumber(0);
                    sty.setShed(shed);
                    Date now = new Date();
                    sty.setCreateTime(now);
                    sty.setModifyTime(now);

                    dao.update(sty);
                    dao.commit();

                    this.pnlStyProperties.setVisible(false);

                    DefaultTableModel model = (DefaultTableModel) tableSty.getModel();

                    Object[] row = {sty.getId(), sty.getName(), sty.getCode(),
                            sty.getCreateTime(), sty.getModifyTime()
                    };
                    model.addRow(row);
                    model.fireTableDataChanged();


                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            else if (btnStyAddOrSave.getText().equals("修改")) {
                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                    dao.begin();

                    Long id = getSelectedStyId();
                    Sty sty = dao.findStyById(id);
                    sty.setCode(this.txtStyCode.getText());
                    sty.setNo(Integer.parseInt(this.txtStyNo.getText()));
                    sty.setName(this.txtStyName.getText());
                    Date now = new Date();
                    sty.setModifyTime(now);

                    dao.update(sty);
                    dao.commit();

                    DefaultTableModel model = (DefaultTableModel) tableSty.getModel();
                    int rowIndex = tableSty.getSelectedRow();
                    model.setValueAt(sty.getId(), rowIndex, 0);
                    model.setValueAt(sty.getName(), rowIndex, 1);
                    model.setValueAt(sty.getCode(), rowIndex, 2);
                    model.setValueAt(sty.getCreateTime(), rowIndex, 3);
                    model.setValueAt(sty.getModifyTime(), rowIndex, 4);

                    model.fireTableRowsUpdated(rowIndex, rowIndex);

                    this.pnlStyProperties.setVisible(false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnDeleteSty.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(null, "确定要删除此栏位?", "删除栏位",JOptionPane.YES_NO_OPTION)
                !=JOptionPane.YES_OPTION)
                return;

            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                dao.begin();
                Sty sty = new Sty();
                Long id = getSelectedStyId();
                sty.setId(id);
                dao.delete(sty);
                dao.commit();
                DefaultTableModel model = (DefaultTableModel) tableSty.getModel();
                int rowIndex = tableSty.getSelectedRow();

                model.removeRow(rowIndex);
                model.fireTableRowsDeleted(rowIndex, rowIndex);

            } catch (Exception e1) {
                e1.printStackTrace();
            }


        });
    }

    private Long getSelectedStyId() {
        int rowIndex = tableSty.getSelectedRow();
        return  (Long)(tableSty.getModel().getValueAt(rowIndex, 0));
    }

    private Long getSelectedShedId() {
        int rowIndex = tableShed.getSelectedRow();
        return  (Long)(tableShed.getModel().getValueAt(rowIndex, 0));
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void onLoad() {

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
                btnEditShed.setEnabled(false);
                btnAddSty.setEnabled(false);
                return;
            }
            btnEditShed.setEnabled(true);
            btnAddSty.setEnabled(true);

            Long id = (Long)(tableShed.getModel().getValueAt(rowIndex, 0));

            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                Shed shed = dao.findShedById(id);

                DefaultTableModel model = (DefaultTableModel) tableSty.getModel();
                model.getDataVector().removeAllElements();
                for (Sty sty : shed.getSties()) {
                    Object[] row = {sty.getId(), sty.getName(), sty.getCode(),
                            sty.getCreateTime(), sty.getModifyTime()
                    };
                    model.addRow(row);
                }
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
        tableSty.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styDataModel.addTableModelListener( e->{
            int type = e.getType();
            if( type == TableModelEvent.INSERT || type == TableModelEvent.DELETE)
                tableSty.setRowSelectionInterval(0, 0);


        });
        tableSty.getSelectionModel().addListSelectionListener(e -> {

            int rowIndex = tableSty.getSelectedRow();
            if (rowIndex == -1) {
                btnEditSty.setEnabled(false);
                btnDeleteSty.setEnabled(false);
                return;
            }
            btnEditSty.setEnabled(true);
            btnDeleteSty.setEnabled(true);

        });


    }
}
