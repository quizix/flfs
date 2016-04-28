package com.dxw.flfs.ui.dialogs;

import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.models.Shed;
import com.dxw.flfs.data.models.Sty;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddStyAssociationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tableShed;
    private JTable tableSty;
    private JPanel panel1;
    private AbstractTableModel shedDataModel;
    private AbstractTableModel styDataModel;

    private Set<Sty> sties;
    private Set<Sty> selected;


    public Set<Sty> getSelected() {

        return selected;
    }

    public boolean getResult() {
        return result;
    }

    boolean result = true;

    HibernateService hibernateService;
    private String[] shedColumns = {"猪舍编号", "名称", "编码", "创建时间", "修改时间",
    };

    private String[] styColumns = {"栏位编号", "名称", "编码", "创建时间", "修改时间",
    };

    public AddStyAssociationDialog(HibernateService hibernateService, Set<Sty> sties) {
        this.hibernateService = hibernateService;

        this.sties = sties;

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
            public void windowOpened(WindowEvent e) {
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
        this.result = true;

        DefaultTableModel model = (DefaultTableModel) tableSty.getModel();

        List<Long> ids = new ArrayList<>();

        int[] rowIndices = tableSty.getSelectedRows();
        if (rowIndices != null && rowIndices.length != 0) {
            for (int i = 0; i < rowIndices.length; i++) {
                ids.add((Long) model.getValueAt(rowIndices[i], 0));
            }
        }
        int rowIndex = tableShed.getSelectedRow();
        Long id = (Long) (tableShed.getModel().getValueAt(rowIndex, 0));


        selected = new HashSet<>();
        try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
            Shed shed = dao.findShedById(id);

            if (shed.getSties() != null) {
                shed.getSties().stream()
                        .filter(sty -> {
                            if (ids.contains(sty.getId()))
                                return true;
                            return false;
                        })
                        .forEach(sty -> {
                            selected.add(sty);
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //selected

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        this.result = false;
        dispose();
    }

    private void onLoad() {
        try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
            final List sheds = dao.findAllSheds();

            DefaultTableModel model = (DefaultTableModel) tableShed.getModel();
            model.getDataVector().removeAllElements();
            for (Object item : sheds) {
                Shed shed = (Shed) item;

                Object[] row = {shed.getId(), shed.getName(), shed.getCode(),
                        shed.getCreateTime(), shed.getModifyTime()
                };
                model.addRow(row);
            }
            model.fireTableDataChanged();

            if (model.getRowCount() > 0)
                tableShed.setRowSelectionInterval(0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        shedDataModel = new DefaultTableModel(shedColumns, 0) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        shedDataModel.addTableModelListener(e -> {
            int type = e.getType();
            if (type == TableModelEvent.INSERT || type == TableModelEvent.DELETE) {
                if (tableShed.getRowCount() > 0)
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

            Long id = (Long) (tableShed.getModel().getValueAt(rowIndex, 0));

            try (FlfsDao dao = new FlfsDaoImpl(this.hibernateService)) {
                Shed shed = dao.findShedById(id);

                DefaultTableModel model = (DefaultTableModel) tableSty.getModel();
                model.getDataVector().removeAllElements();
                if (shed.getSties() != null) {
                    shed.getSties().stream()
                            .filter(sty -> {
                                for (Sty sty2 : this.sties) {
                                    if (sty.getId().equals(sty2.getId()))
                                        return false;
                                }
                                return true;
                            })
                            .forEach(sty -> {
                                Object[] row = {sty.getId(), sty.getName(), sty.getCode(),
                                        sty.getCreateTime(), sty.getModifyTime()
                                };
                                model.addRow(row);
                            });
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
            if (type == TableModelEvent.INSERT || type == TableModelEvent.DELETE)
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

