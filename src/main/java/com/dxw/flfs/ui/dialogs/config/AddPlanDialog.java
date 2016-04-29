package com.dxw.flfs.ui.dialogs.config;

import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.PigletPlan;
import com.dxw.flfs.data.models.SiteConfig;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

public class AddPlanDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel pnlPlan;
    private JTable tablePlan;
    private JButton btnAddPlan;
    private JButton btnEditPlan;
    private JPanel pnlPigletPlanProperties;
    private JTextField txtPlanCount;
    private JTextField txtPlanDate;
    private JButton btnAddSavePlanOk;
    private JButton btnAddSavePlanCancel;

    UnitOfWork unitOfWork;
    private String siteCode = FlfsApp.getContext().getSiteCode();

    public AddPlanDialog(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }


        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        btnAddPlan.addActionListener(e -> {
            onAddPlan();
        });
        btnEditPlan.addActionListener(e -> {
            onEditPlan();
        });
        btnAddSavePlanOk.addActionListener(e -> {
            onAddSavePlanOk();
        });
        btnAddSavePlanCancel.addActionListener(e -> {
            onAddSavePlanCancel();
        });

        /*DefaultGenericRepository<SiteConfig> r = unitOfWork.getSiteConfigRepository();
        Collection<SiteConfig> configs = r.findAll();

        Optional<SiteConfig> config = configs.stream()
                .filter(c -> c.getSiteCode().equals(siteCode))
                .findFirst();
        siteConfig = config.get();

        planDataModel.getDataVector().removeAllElements();
        Set<PigletPlan> plans = siteConfig.getPlans();
        for (PigletPlan item : plans) {
            Object[] row = {item.getId(), item.getDate(), item.getCount()};
            planDataModel.addRow(row);
        }
        planDataModel.fireTableDataChanged();*/
    }

    private void onAddPlan() {
        this.btnAddSavePlanOk.setText("添加");
        this.txtPlanDate.setText(TimeUtil.getCurrentDate());
        this.txtPlanCount.setText("0");

        this.pnlPigletPlanProperties.setVisible(true);
    }


    private void onEditPlan() {
        this.btnAddSavePlanOk.setText("修改");
        int rowIndex = tablePlan.getSelectedRow();


        DefaultTableModel model = (DefaultTableModel) tablePlan.getModel();
        this.txtPlanDate.setText(TimeUtil.formatDate((Date) model.getValueAt(rowIndex, 1)));
        this.txtPlanCount.setText(model.getValueAt(rowIndex, 2).toString());

        this.pnlPigletPlanProperties.setVisible(true);
    }

    private void onAddSavePlanCancel() {
        this.pnlPigletPlanProperties.setVisible(false);
    }

    private void onAddSavePlanOk() {
        if (this.btnAddSavePlanOk.getText().equals("添加")) {
            PigletPlan plan = new PigletPlan();
            Date now = new Date();
            plan.setCreateTime(now);
            plan.setModifyTime(now);

            plan.setDate(TimeUtil.parseDate(this.txtPlanDate.getText()));
            plan.setCount(Integer.parseInt(this.txtPlanCount.getText()));


            DefaultGenericRepository<SiteConfig> r = unitOfWork.getSiteConfigRepository();

            siteConfig.getPlans().add(plan);

            r.save(siteConfig);

            DefaultTableModel model = (DefaultTableModel) tablePlan.getModel();
            model.addRow(new Object[]{
                    plan.getId(), plan.getDate(), plan.getCount()
            });
            model.fireTableRowsInserted(model.getRowCount() - 1, model.getRowCount() - 1);
        } else {
            int rowIndex = tablePlan.getSelectedRow();
            unitOfWork.begin();
            Long id = (Long) (tablePlan.getModel().getValueAt(rowIndex, 0));

            DefaultGenericRepository<PigletPlan> r = unitOfWork.getPigletPlanRepository();
            PigletPlan plan = r.find(id);

            plan.setDate(TimeUtil.parseDate(this.txtPlanDate.getText()));
            plan.setCount(Integer.parseInt(this.txtPlanCount.getText()));
            plan.setModifyTime(new Date());
            r.save(plan);

            unitOfWork.commit();

            DefaultTableModel model = (DefaultTableModel) tablePlan.getModel();

            model.setValueAt(plan.getId(), rowIndex, 0);
            model.setValueAt(plan.getDate(), rowIndex, 1);
            model.setValueAt(plan.getCount(), rowIndex, 2);

            model.fireTableRowsUpdated(rowIndex, rowIndex);

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

    private DefaultTableModel planDataModel;


    private String[] planColumns = {"编号", "日期", "数量"
    };

    SiteConfig siteConfig;
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
                btnEditPlan.setEnabled(false);
                return;
            }
            btnEditPlan.setEnabled(true);
        });


    }


}
