package com.dxw.flfs.ui;

import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.communication.PlcModel;
import com.dxw.flfs.communication.PlcModelField;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * Created by Administrator on 2016/4/2.
 */
public class SvgPanel {
    private JPanel panel;

    private JSVGCanvas canvas;
    public SvgPanel(){
        initSvg();

        PlcDelegate delegate = PlcDelegateFactory.getPlcDelegate();
        delegate.addModelChangedListener(event -> {
            long field = event.getField();
            PlcModel model = event.getModel();

            SVGDocument document = canvas.getSVGDocument();
            if (field == PlcModelField.SYSTEM_STATUS) {
                Short status = model.getSystemStatus();
                Element ele = document.getElementById("textSystemStatus");

                switch (status) {
                    case 1:
                        ele.setTextContent("停机");
                        break;
                    case 2:
                        ele.setTextContent("做料");
                        break;
                    case 3:
                        ele.setTextContent("清洗");
                        break;
                    case 4:
                        ele.setTextContent("紧停");
                        break;
                    case 5:
                        ele.setTextContent("冷启动");
                        break;
                }

            } else if (field == PlcModelField.FERMENT_BARREL_STATUS) {
                boolean[] data = model.getFermentBarrelStatus();
                String status = "";
                for (int i = 0; i < Math.min(data.length,7); i++) {
                    status += ((data[i]) ? "满" : "空") + " ";
                }
                Element ele = document.getElementById("textFermentBarrelStatus");
                ele.setTextContent(status);

            } /*else if (field == PlcModelField.MATERIAL_TOWER_ALARM) {
                Boolean lowAlarm = model.getMaterialTowerLowAlarm();
                Boolean emptyAlarm = model.getMaterialTowerEmptyAlarm();

                if (lowAlarm) {
                    lblMaterialTowerLow.setIcon(iconAlert);
                    lblMaterialTowerLow.setText("");
                } else {
                    lblMaterialTowerLow.setIcon(null);
                    lblMaterialTowerLow.setText("正常");
                }

                if (emptyAlarm) {
                    lblMaterialTowerEmpty.setIcon(iconAlert);
                    lblMaterialTowerEmpty.setText("");
                } else {
                    lblMaterialTowerEmpty.setIcon(null);
                    lblMaterialTowerEmpty.setText("正常");
                }
            } else if (field == PlcModelField.FERMENT_BARREL_IN_OUT) {
                short in = model.getFermentBarrelInNo();
                short out = model.getFermentBarrelOutNo();
                lblFermentBarrelIn.setText(Short.toString(in));
                lblFermentBarrelOut.setText(Short.toString(out));
            } else if (field == PlcModelField.MIXING_BARREL_STATUS) {
                Short status = model.getMixingBarrelStatus();
                lblMixingBarrelStatus.setText(status == 0 ? "空闲" : "运行");
            } else if (field == PlcModelField.PH_VALUE) {
                float ph = model.getPh();
                lblPh.setText(Float.toString(ph));
            }*/
            //canvas.repaint();
            //canvas.getCanvasGraphicsNode().fireGraphicsNodeChangeCompleted();
            canvas.getUpdateManager().forceRepaint();
        });
    }

    private void createUIComponents() {
        panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(new BorderLayout());
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
            URL url = this.getClass().getResource("/svg/flfs.svg");
            File f = new File(url.toURI());
            canvas.setURI(f.toURI().toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
