package com.dxw.flfs.ui.dialogs.config;

import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.Shed;
import com.dxw.flfs.data.models.SiteConfig;
import com.dxw.flfs.data.models.Sty;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.*;

public class AddStyDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tableShed;
    private JTable tableSty;

    private AbstractTableModel shedDataModel;
    private AbstractTableModel styDataModel;
    private String[] shedColumns = {"猪舍编号", "名称", "编码", "创建时间", "修改时间",
    };

    private String[] styColumns = {"栏位编号", "名称", "编码", "创建时间", "修改时间",
    };

    public Set<Sty> getSelected() {
        return selected;
    }

    private boolean result = false;

    private Set<Sty> selected;

    UnitOfWork unitOfWork;
    private String siteCode = FlfsApp.getContext().getSiteCode();
    public AddStyDialog(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
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

                onLoad();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onLoad() {

        DefaultGenericRepository<Shed> shedRepository = unitOfWork.getShedRepository();
        Collection<Shed> sheds = shedRepository.findAll();

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
    }

    private void onOK() {
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

        DefaultGenericRepository<Shed> shedRepository = unitOfWork.getShedRepository();

        Shed shed = shedRepository.find(id);

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

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
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

            DefaultGenericRepository<SiteConfig> r = unitOfWork.getSiteConfigRepository();
            Collection<SiteConfig> configs = r.findAll();

            Optional<SiteConfig> config = configs.stream()
                    .filter(c -> c.getSiteCode().equals(siteCode))
                    .findFirst();
            SiteConfig siteConfig = config.get();

            Set<Sty> sties = siteConfig.getSties();

            Long id = (Long) (tableShed.getModel().getValueAt(rowIndex, 0));

            DefaultGenericRepository<Shed> shedRepository = unitOfWork.getShedRepository();

            Shed shed = shedRepository.find(id);
            DefaultTableModel model = (DefaultTableModel) tableSty.getModel();
            model.getDataVector().removeAllElements();
            if (shed.getSties() != null) {
                shed.getSties().stream()
                        .filter(sty -> {
                            for (Sty sty2 : sties) {
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

    public boolean getResult() {
        return result;
    }
}
