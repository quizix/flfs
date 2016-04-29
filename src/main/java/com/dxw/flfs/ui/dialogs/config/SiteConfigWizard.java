package com.dxw.flfs.ui.dialogs.config;

import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.data.dal.DefaultGenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.PigletPlan;
import com.dxw.flfs.data.models.SiteConfig;
import com.dxw.flfs.data.models.Sty;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

public class SiteConfigWizard extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton btnAddSty;
    private JButton btnRemoveSty;
    private JTable tableSty;
    private JTable tablePlan;
    private JPanel pnlAssociate;
    private JPanel pnlPlan;
    private JButton btnAddPlan;
    private JButton btnEditPlan;
    private JTextField txtPlanCount;

    private JPanel pnlPigletPlanProperties;
    private JButton btnAddSavePlanOk;
    private JButton btnAddSavePlanCancel;
    private JTextField txtPlanDate;

    UnitOfWork unitOfWork;
    SiteConfig siteConfig;

    public int getDialogResult() {
        return dialogResult;
    }

    int dialogResult = 0;

    public SiteConfigWizard(UnitOfWork unitOfWork) {
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
        });



// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        DefaultGenericRepository<SiteConfig> r = unitOfWork.getSiteConfigRepository();
        Collection<SiteConfig> configs = r.findAll();

        Optional<SiteConfig> config = configs.stream()
                .filter(c -> c.getSiteCode().equals(siteCode))
                .findFirst();
        siteConfig = config.get();

        Set<Sty> sties = siteConfig.getSties();

        for (Sty sty : sties) {
            Object[] row = {sty.getId(), sty.getName(), sty.getCode(),
                    sty.getCreateTime(), sty.getModifyTime()
            };
            styDataModel.addRow(row);
        }
        styDataModel.fireTableDataChanged();

        btnAddSty.addActionListener(e -> onAssociate());

        btnAddPlan.addActionListener( e-> onAddPlan());
        btnEditPlan.addActionListener( e->onEditPlan() );
        btnAddSavePlanOk.addActionListener(e -> {
            onAddSavePlanOk();
        });
        btnAddSavePlanCancel.addActionListener(e -> {
            onAddSavePlanCancel();
        });
    }

    private void onAddSavePlanCancel() {
        this.pnlPigletPlanProperties.setVisible(false);
    }

    private void onAddSavePlanOk() {
        if(this.btnAddSavePlanOk.getText().equals("添加")){
            PigletPlan plan = new PigletPlan();
            Date now = new Date();
            plan.setCreateTime(now);
            plan.setModifyTime(now);

            plan.setDate( TimeUtil.parseDate(this.txtPlanDate.getText()));
            plan.setCount( Integer.parseInt(this.txtPlanCount.getText()));


            DefaultGenericRepository<SiteConfig> r = unitOfWork.getSiteConfigRepository();

            siteConfig.getPlans().add(plan);

            r.save(siteConfig);

            DefaultTableModel model = (DefaultTableModel) tablePlan.getModel();
            model.addRow( new Object[]{
                    plan.getId(), plan.getDate(), plan.getCount()
            });
            model.fireTableRowsInserted(model.getRowCount()-1, model.getRowCount()-1);
        }
        else{
            int rowIndex = tablePlan.getSelectedRow();
            unitOfWork.begin();
            Long id = (Long) (tablePlan.getModel().getValueAt(rowIndex, 0));

            DefaultGenericRepository<PigletPlan> r = unitOfWork.getPigletPlanRepository();
            PigletPlan plan = r.find(id);

            plan.setDate( TimeUtil.parseDate(this.txtPlanDate.getText()));
            plan.setCount( Integer.parseInt(this.txtPlanCount.getText()));
            plan.setModifyTime( new Date());
            r.save(plan);

            unitOfWork.commit();

            DefaultTableModel model = (DefaultTableModel) tablePlan.getModel();

            model.setValueAt(plan.getId(), rowIndex, 0);
            model.setValueAt(plan.getDate(), rowIndex, 1);
            model.setValueAt(plan.getCount(), rowIndex, 2);

            model.fireTableRowsUpdated(rowIndex, rowIndex);

        }


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
        this.txtPlanDate.setText( TimeUtil.formatDate( (Date)model.getValueAt(rowIndex,1)));
        this.txtPlanCount.setText( model.getValueAt(rowIndex,2).toString());

        this.pnlPigletPlanProperties.setVisible(true);
    }

    private void onAssociate() {
        AddStyDialog dialog = new AddStyDialog(this.unitOfWork);
        dialog.setTitle("添加关联");
        dialog.setSize(800,600);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        if( dialog.getResult()){
            Set<Sty> selected = dialog.getSelected();

            siteConfig.getSties().addAll( selected);

            DefaultTableModel model = (DefaultTableModel) tableSty.getModel();
            for (Sty sty : selected) {
                Object[] row = {sty.getId(), sty.getName(), sty.getCode(),
                        sty.getCreateTime(), sty.getModifyTime()
                };
                model.addRow(row);
            }
            model.fireTableDataChanged();
        }
    }

    private void onOK() {
        if(buttonOK.getText().equals("下一步")){
            this.pnlAssociate.setVisible(false);
            this.pnlPlan.setVisible(true);
            this.buttonOK.setText("完成");

            initPlans();
        }
        else {
            unitOfWork.begin();
            siteConfig.setModifyTime(new Date());
            siteConfig.setStatus(1);
            DefaultGenericRepository<SiteConfig> r = unitOfWork.getSiteConfigRepository();
            r.save(siteConfig);
            unitOfWork.commit();


            dispose();

            dialogResult = 1;
        }
    }

    private void initPlans() {
        planDataModel.getDataVector().removeAllElements();
        Set<PigletPlan> plans = siteConfig.getPlans();
        for (PigletPlan item : plans) {
            Object[] row = {item.getId(), item.getDate(), item.getCount()};
            planDataModel.addRow(row);
        }
        planDataModel.fireTableDataChanged();
    }

    private void onCancel() {
        dispose();
    }

    private DefaultTableModel styDataModel;
    private DefaultTableModel planDataModel;

    private String[] styColumns = {"栏位编号", "名称", "编码", "创建时间", "修改时间",
    };

    private String[] planColumns = {"编号", "日期", "数量"
    };

    private String siteCode = FlfsApp.getContext().getSiteCode();

    private void createUIComponents() {
        styDataModel = new DefaultTableModel(styColumns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
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

        planDataModel = new DefaultTableModel(planColumns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablePlan= new JTable(planDataModel);

        planDataModel.addTableModelListener(e -> {
            int type = e.getType();
            if (type == TableModelEvent.INSERT || type == TableModelEvent.DELETE) {
                //tableSty.setRowSelectionInterval(0, 0);
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
