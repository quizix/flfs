package com.dxw.flfs.ui.dialogs;

import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.models.Batch;
import com.dxw.flfs.data.models.Sty;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

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

            Long id = (Long) (tableBatch.getModel().getValueAt(rowIndex, 0));

            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                Batch batch = dao.findBatchById(id);

                if (batch != null) {
                    this.txtCode.setText(batch.getCode());
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
        btnAddOrSave.addActionListener(e -> {
            if (btnAddOrSave.getText().equals("添加")) {
                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {

                    dao.begin();

                    Batch batch = new Batch();
                    batch.setCode(this.txtCode.getText());
                    batch.setInStockNumber(Integer.parseInt(this.txtInStockNumber.getText()));

                    batch.setStartDate(TimeUtil.parseDate(this.txtStartDate.getText()));
                    batch.setEndDate(TimeUtil.parseDate(this.txtEndDate.getText()));
                    Date now = new Date();
                    batch.setModifyTime(now);
                    batch.setCreateTime(now);
                    dao.update(batch);
                    dao.commit();

                    this.pnlBatchProperties.setVisible(false);

                    DefaultTableModel model = (DefaultTableModel) tableBatch.getModel();
                    Object[] row = {batch.getId(), batch.getCode(), batch.getStartDate(),
                            batch.getEndDate(), batch.getInStockNumber()
                    };
                    model.addRow(row);
                    int size = model.getRowCount();
                    model.fireTableRowsInserted(size - 1, size - 1);


                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else if (btnAddOrSave.getText().equals("修改")) {

                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                    dao.begin();

                    int rowIndex = tableBatch.getSelectedRow();

                    Long id = (Long) (tableBatch.getModel().getValueAt(rowIndex, 0));

                    dao.begin();

                    Batch item = dao.findBatchById(id);

                    item.setCode(this.txtCode.getText());
                    item.setStartDate(TimeUtil.parseDate(this.txtStartDate.getText()));
                    item.setEndDate(TimeUtil.parseDate(this.txtEndDate.getText()));
                    item.setInStockNumber(Integer.parseInt(this.txtInStockNumber.getText()));

                    Date now = new Date();
                    item.setModifyTime(now);

                    dao.update(item);
                    dao.commit();

                    DefaultTableModel model = (DefaultTableModel) tableBatch.getModel();
                    model.setValueAt(item.getId(), rowIndex, 0);
                    model.setValueAt(item.getCode(), rowIndex, 1);
                    model.setValueAt(item.getStartDate(), rowIndex, 2);
                    model.setValueAt(item.getEndDate(), rowIndex, 3);
                    model.setValueAt(item.getInStockNumber(), rowIndex, 4);

                    model.fireTableRowsUpdated(rowIndex, rowIndex);

                    this.pnlBatchProperties.setVisible(false);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        btnAddSty.addActionListener(e -> {
            int batchIndex = tableBatch.getSelectedRow();
            Long id = (Long) (tableBatch.getModel().getValueAt(batchIndex, 0));

            Set<Sty> sties = new HashSet<>();

            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                Batch batch = dao.findBatchById(id);
                sties.addAll(batch.getSties());

            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }

            AddStyAssociationDialog dialog = new AddStyAssociationDialog(this.hibernateService,
                    sties);
            dialog.setSize(800, 600);
            dialog.setModal(true);
            dialog.setVisible(true);
            if (dialog.getResult()) {
                Set<Sty> selected = dialog.getSelected();

                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                    dao.begin();
                    Batch batch = dao.findBatchById(id);
                    batch.getSties().addAll(selected);
                    dao.update(batch);
                    dao.commit();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                DefaultTableModel model = (DefaultTableModel) tableSty.getModel();
                for (Sty sty : selected) {
                    Object[] row = {sty.getId(), sty.getName(), sty.getCode(),
                            sty.getCreateTime(), sty.getModifyTime()
                    };
                    model.addRow(row);
                }
                model.fireTableDataChanged();

            }

        });
        btnRemoveSty.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) tableSty.getModel();

            //取消关联

            int[] rowIndices = tableSty.getSelectedRows();
            if (rowIndices != null && rowIndices.length != 0) {
                List<Long> ids = new ArrayList<>();
                for (int i = 0; i < rowIndices.length; i++) {
                    ids.add((Long) model.getValueAt(rowIndices[i], 0));
                }

                int batchIndex = tableBatch.getSelectedRow();
                Long id = (Long) (tableBatch.getModel().getValueAt(batchIndex, 0));

                try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                    dao.begin();

                    Batch item = dao.findBatchById(id);
                    item.getSties().removeIf(sty -> ids.contains(sty.getId()));

                    dao.update(item);
                    dao.commit();

                    removeRows(model, rowIndices);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void removeRows(DefaultTableModel model, int[] indices) {
        Arrays.sort(indices);
        for (int i = indices.length - 1; i >= 0; i--) {
            model.removeRow(indices[i]);
            model.fireTableRowsDeleted(indices[i], indices[i]);
        }
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
        batchDataModel = new DefaultTableModel(batchColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        batchDataModel.addTableModelListener(e -> {
            int type = e.getType();
            if (type == TableModelEvent.INSERT || type == TableModelEvent.DELETE) {
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

            Long id = (Long) (tableBatch.getModel().getValueAt(rowIndex, 0));


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

        styDataModel = new DefaultTableModel(styColumns, 0) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        tableSty = new JTable(styDataModel);

        styDataModel.addTableModelListener(e -> {
            int type = e.getType();
            if (type == TableModelEvent.INSERT || type == TableModelEvent.DELETE) {
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

    private void load() {
        try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
            final List batches = dao.findAllBatches();

            DefaultTableModel model = (DefaultTableModel) tableBatch.getModel();
            model.getDataVector().removeAllElements();
            for (Object item : batches) {
                Batch batch = (Batch) item;

                Object[] row = {batch.getId(), batch.getCode(), batch.getStartDate(),
                        batch.getEndDate(), batch.getInStockNumber()
                };
                model.addRow(row);
            }
            model.fireTableDataChanged();

            if (model.getRowCount() > 0)
                tableBatch.setRowSelectionInterval(0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
