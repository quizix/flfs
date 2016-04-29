package com.dxw.flfs.ui.dialogs.config;

import com.dxw.flfs.data.dal.UnitOfWork;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class PlanConfigDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tablePlan;
    private JButton button1;
    private JButton button2;

    UnitOfWork unitOfWork;
    public PlanConfigDialog(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
    private String[] planColumns = {"编号", "日期", "数量"
    };
    private DefaultTableModel planDataModel;
    private void createUIComponents() {
        planDataModel = new DefaultTableModel(planColumns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablePlan = new JTable(planDataModel);

        planDataModel.addTableModelListener(e -> {
            int type = e.getType();
            if (type == TableModelEvent.INSERT || type == TableModelEvent.DELETE) {
            }

        });
        tablePlan.getSelectionModel().addListSelectionListener(e -> {
            int rowIndex = tablePlan.getSelectedRow();
            if (rowIndex == -1) {
                //btnEditPlan.setEnabled(false);
                return;
            }
//            btnEditPlan.setEnabled(true);
        });
    }
}
