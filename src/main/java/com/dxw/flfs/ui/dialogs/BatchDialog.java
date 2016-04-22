package com.dxw.flfs.ui.dialogs;

import com.dxw.common.models.Batch;
import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class BatchDialog extends JDialog {
    private JPanel contentPane;
    private JButton btnAddBatch;
    private JButton btnAddSty;
    private JTable tableSty;
    private JTable tableBatch;
    private JButton btnRemoveSty;
    private JTextField txtEndDate;
    private JTextField txtStartDate;
    private JTextField txtCode;
    private JTextField txtInStockNumber;
    private JButton btnAddOrSave;
    private JPanel pnlBatchProperties;
    private JButton btnEditBatch;
    private JButton btnDeleteBatch;
    HibernateService hibernateService;
    private AbstractTableModel batchDataModel;
    private AbstractTableModel styDataModel;

    private String[] batchColumns = {"批次编号", "编码", "开始时间", "结束时间", "入栏数量"
    };
    private String[] styColumns = {"栏位编号", "名称", "编码", "创建时间", "修改时间",
    };
    public BatchDialog(HibernateService hibernateService) {
        this.hibernateService = hibernateService;
        setContentPane(contentPane);
        setModal(true);


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);

                BatchDialog.this.load();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnAddBatch.addActionListener(e -> {
            this.btnAddOrSave.setText("添加");
            this.txtCode.setText("");
            this.txtStartDate.setText(TimeUtil.getCurrentDate());
            this.txtEndDate.setText(TimeUtil.getCurrentDate());
            this.txtInStockNumber.setText("");

            this.pnlBatchProperties.setVisible(true);
            this.txtCode.requestFocus();
        });
        btnEditBatch.addActionListener(e -> {
            this.btnAddOrSave.setText("修改");
            int rowIndex = tableBatch.getSelectedRow();

            Long id = (Long)(tableBatch.getModel().getValueAt(rowIndex, 0));

            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                Batch batch = dao.findBatchById(id);

                if( batch != null){
                    this.txtCode.setText( batch.getCode());
                    this.txtStartDate.setText(TimeUtil.formatDate(batch.getStartDate()));
                    this.txtEndDate.setText(TimeUtil.formatDate(batch.getEndDate()));
                    this.txtInStockNumber.setText(Integer.toString(batch.getInStockNumber()));

                    this.pnlBatchProperties.setVisible(true);
                    this.txtCode.requestFocus();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        btnAddOrSave.addActionListener( e->{
            if (btnAddOrSave.getText().equals("添加")) {
                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {

                    dao.begin();

                    Batch batch = new Batch();
                    batch.setCode(this.txtCode.getText());
                    batch.setInStockNumber( Integer.parseInt(this.txtInStockNumber.getText()));

                    batch.setStartDate( TimeUtil.parseDate(this.txtStartDate.getText()));
                    batch.setEndDate( TimeUtil.parseDate(this.txtEndDate.getText()));
                    Date now = new Date();
                    batch.setModifyTime( now );
                    batch.setCreateTime( now );
                    dao.update(batch);
                    dao.commit();

                    this.pnlBatchProperties.setVisible(false);

                    DefaultTableModel model = (DefaultTableModel) tableBatch.getModel();
                    Object[] row = {batch.getId(), batch.getCode(), batch.getStartDate(),
                            batch.getEndDate(), batch.getInStockNumber()
                    };
                    model.addRow(row);
                    int size = model.getRowCount();
                    model.fireTableRowsInserted(size-1, size-1);


                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            /*else if (btnStyAddOrSave.getText().equals("修改")) {
                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                    dao.begin();


                    Long id = (Long)(tableBatch.getModel().getValueAt(rowIndex, 0));

                    dao.begin();

                    Batch batch = dao.findBatchById(id);

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
            }*/
        });
        btnDeleteBatch.addActionListener(e -> {

        });
        btnAddSty.addActionListener(e -> {

        });
        btnRemoveSty.addActionListener(e -> {

        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private void createUIComponents() {
        batchDataModel = new DefaultTableModel(batchColumns, 0){
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        batchDataModel.addTableModelListener( e->{
            int type = e.getType();
            if( type == TableModelEvent.INSERT || type == TableModelEvent.DELETE) {
                /*if(tableBatch.getRowCount() >0)
                    tableBatch.setRowSelectionInterval(0, 0);*/
            }
        });

        tableBatch = new JTable(batchDataModel);
        tableBatch.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableBatch.getSelectionModel().addListSelectionListener(e -> {

            int rowIndex = tableBatch.getSelectedRow();
            if (rowIndex == -1) {
                tableBatch.setEnabled(false);
                btnAddSty.setEnabled(false);
                return;
            }
            btnEditBatch.setEnabled(true);
            btnAddSty.setEnabled(true);

            Long id = (Long)(tableBatch.getModel().getValueAt(rowIndex, 0));


            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                Batch batch = dao.findBatchById(id);

                DefaultTableModel model = (DefaultTableModel) tableSty.getModel();
                model.getDataVector().removeAllElements();
                for (Sty sty : batch.getSties()) {
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

        styDataModel.addTableModelListener( e->{
            int type = e.getType();
            if( type == TableModelEvent.INSERT || type == TableModelEvent.DELETE) {
                //tableSty.setRowSelectionInterval(0, 0);
            }

        });
        tableSty.getSelectionModel().addListSelectionListener(e -> {

            int rowIndex = tableSty.getSelectedRow();
            if (rowIndex == -1) {
                btnRemoveSty.setEnabled(false);

                return;
            }
            btnRemoveSty.setEnabled(true);
        });
    }

    private void load(){
        try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
            final List batches = dao.findAllBatches();

            DefaultTableModel model = (DefaultTableModel) tableBatch.getModel();
            model.getDataVector().removeAllElements();
            for (Object item : batches) {
                Batch batch = (Batch)item;

                Object[] row = {batch.getId(), batch.getCode(), batch.getStartDate(),
                        batch.getEndDate(), batch.getInStockNumber()
                };
                model.addRow(row);
            }
            model.fireTableDataChanged();

            if(model.getRowCount() >0)
                tableBatch.setRowSelectionInterval(0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
