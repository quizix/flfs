package com.dxw.flfs.ui.wizards;

import com.dxw.common.models.Shed;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class SelectShedDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;

    public boolean getDialogResult() {
        return dialogResult;
    }

    private boolean dialogResult = false;

    public SelectShedDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();
        this.dialogResult = true;
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        SelectShedDialog dialog = new SelectShedDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        comboBox1 = new JComboBox();
        HibernateService s = (HibernateService) ServiceRegistryImpl.getInstance().lookupService(Services.HIBERNATE_SERVICE);
        try (FlfsDao dao = new FlfsDaoImpl(s)) {
            List sheds = dao.findAllSheds();
            for(Object o: sheds){
                Shed shed = (Shed)o;
                comboBox1.addItem(shed.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
