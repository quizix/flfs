package com.dxw.flfs.ui;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/2.
 */
public class NotificationPanel {

    private NotificationManager notificationManger;
    private JTextPane textPane;
    private JPanel panel1;

    public NotificationPanel() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();

        notificationManger = (NotificationManager) registry.lookupService(Services.NOTIFICATION_MANAGER);

        if (notificationManger != null) {
            notificationManger.addReceiver((String tag, Notification notification) -> {
                onNotify(tag, notification);
            });
        }
    }

    private void onNotify(String tag, Notification notification) {
        long when = notification.getWhen();

        String message = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                .format(new Date(when));
        String m = String.format("[%s] %s %s", tag, message, notification.getContent().toString());
        addText(m);

    }

    private void addText(String text) {
        //kit.insertHTML(doc, HEIGHT, text, WIDTH, WIDTH, HTML.Tag.INPUT);
        try {
            //doc.insertAfterEnd(null, text);
            kit.insertHTML(doc, doc.getLength(), text, 0, 0, null);

        } catch (BadLocationException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    HTMLEditorKit kit;
    HTMLDocument doc;

    private void createUIComponents() {
        this.panel1 = new JPanel();
        textPane = new JTextPane();
        this.panel1.add(textPane);

        kit = new HTMLEditorKit();
        //实例化一个HTMLEditorkit工具包，用来编辑和解析用来显示在jtextpane中的内容。
        doc = (HTMLDocument) kit.createDefaultDocument();
        //使用HTMLEditorKit类的方法来创建一个文档类，HTMLEditorKit创建的类型默认为htmldocument。
        this.textPane.setEditorKit(kit);
        //设置jtextpane组件的编辑器工具包，是其支持html格式。
        this.textPane.setContentType("text/html");
        //设置编辑器要处理的文档内容类型，有text/html,text/rtf.text/plain三种类型。
        this.textPane.setDocument(doc);
    }
}
