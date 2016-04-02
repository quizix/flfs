package com.dxw.flfs.ui;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;

import javax.swing.*;
import java.io.File;

/**
 * Created by Administrator on 2016/4/2.
 */
public class SvgPanel {
    private JPanel panel;

    private JSVGCanvas canvas;

    private void createUIComponents() {

        panel = new JPanel();
        panel.setBackground(new java.awt.Color(255, 255, 255));
        panel.setLayout(new java.awt.BorderLayout());
        initSvg();
    }

    private void initSvg() {
        canvas = new JSVGCanvas();
        canvas.setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);

        panel.add("Center", canvas);

        // Set the JSVGCanvas listeners.
        canvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
            public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
                //label.setText("Document Loading...");

            }

            public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                //label.setText("Document Loaded.");
                /*SVGDocument document = e.getSVGDocument();

                Element ele = document.getElementById("xxx");

                ele.setTextContent("oh my god");*/

            }
        });

        canvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
            public void gvtBuildStarted(GVTTreeBuilderEvent e) {

                //label.setText("Build Started...");
            }

            public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
                //label.setText("Build Done.");
//                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        canvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
            }

            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
            }
        });
        try {
            File f = new File("f:\\flfs.svg");
            canvas.setURI(f.toURI().toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
