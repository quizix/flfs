/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.server.app;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.ProcessImageListener;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.tcp.TcpSlave;

/**
 *
 * @author pronics3
 */
public class Simulator {

    class BasicProcessImageListener implements ProcessImageListener {

        NotificationManager notificationManager;

        public BasicProcessImageListener(NotificationManager notificationManager) {
            this.notificationManager = notificationManager;
        }

        @Override
        public void coilWrite(int offset, boolean oldValue, boolean newValue) {
            System.out.println("Coil at " + offset + " was set from " + oldValue + " to " + newValue);
            Notification n = new Notification();
            n.setContent("Coil at " + offset + " was set from " + oldValue + " to " + newValue);
            n.setWhen(System.currentTimeMillis());
            notificationManager.notify("coilWrite", n);
        }

        @Override
        public void holdingRegisterWrite(int offset, short oldValue, short newValue) {
            // Add a small delay to the processing.
            //            try {
            //                Thread.sleep(500);
            //            }
            //            catch (InterruptedException e) {
            //                // no op
            //            }
            System.out.println("HR at " + offset + " was set from " + oldValue + " to " + newValue);
            Notification n = new Notification();
            n.setContent("HR at " + offset + " was set from " + oldValue + " to " + newValue);
            n.setWhen(System.currentTimeMillis());
            notificationManager.notify("holdingRegisterWrite", n);
        }
    }

    NotificationManager notificationManager;

    public Simulator() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        notificationManager = (NotificationManager) registry.getService(Services.NOTIFICATION_MANAGER);

    }

    public void start() throws ModbusInitException {
        ModbusFactory factory = new ModbusFactory();

        //ModbusSlaveSet listener = factory.createTcpSlave(false);
        ModbusSlaveSet listener = new TcpSlave(ModbusUtils.TCP_PORT, false);
        listener.addProcessImage(getModscanProcessImage(1));
        listener.addProcessImage(getModscanProcessImage(2));
        new Thread(() -> {
            try {
                listener.start();
            } catch (ModbusInitException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    BasicProcessImage getModscanProcessImage(int slaveId) {
        BasicProcessImage processImage = new BasicProcessImage(slaveId);
        //processImage.setAllowInvalidAddress(true);
        processImage.setInvalidAddressValue(Short.MIN_VALUE);

        if( slaveId == 1) {
            //coil register, only for test.
            processImage.setCoil(1, true);
            processImage.setCoil(2, false);
            processImage.setCoil(3, true);
            processImage.setCoil(4, true);
            processImage.setCoil(5, false);


            //discrete input
            processImage.setInput(1, false);    //紧停开关
            processImage.setInput(2, false);    //料位低警报
            processImage.setInput(3, true);    //料位空警报
            processImage.setInput(4, false);    //发酵罐1#空
            processImage.setInput(5, false);     //发酵罐2#空
            processImage.setInput(6, false);     //发酵罐3#空
            processImage.setInput(7, true);     //发酵罐4#空
            processImage.setInput(8, true);     //发酵罐5#空
            processImage.setInput(9, true);     //发酵罐6#空
            processImage.setInput(10, true);    //发酵罐7#空

            //Input registers
            processImage.setNumeric(RegisterRange.INPUT_REGISTER, 3, DataType.FOUR_BYTE_FLOAT, new Float(
                    6.45)); // pH值


            //Holding registers

            processImage.setHoldingRegister(1, (short) 0); //搅拌桶状态(0空闲，1忙)
            processImage.setHoldingRegister(2, (short) 4); //// 系统状态(1停机2做料3清洗4紧停5冷启动)
            processImage.setHoldingRegister(3, (short) 5); //首次做料发酵时间12小时倒计时
            processImage.setHoldingRegister(4, (short) 1); //预备或正在进料的发酵罐号
            processImage.setHoldingRegister(5, (short) 2); //正在出料的发酵罐号

            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 6, DataType.FOUR_BYTE_FLOAT, new Float(
                    100)); // 加水量（kg）
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 8, DataType.FOUR_BYTE_FLOAT, new Float(
                    101)); // 加干料量（kg）
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 10, DataType.FOUR_BYTE_FLOAT, new Float(
                    102)); // 加菌液量（kg）
            processImage.setHoldingRegister(12, (short) 0); //任务第1罐做量（桶）
            processImage.setHoldingRegister(13, (short) 0); //任务第2罐做量（桶）
            processImage.setHoldingRegister(14, (short) 0); //任务第3罐做量（桶）
            processImage.setHoldingRegister(15, (short) 0); //任务第4罐做量（桶）
            processImage.setHoldingRegister(16, (short) 0); //任务第5罐做量（桶）
            processImage.setHoldingRegister(17, (short) 0); //任务第6罐做量（桶）
            processImage.setHoldingRegister(18, (short) 0); //系统命令（0停机1运行2清洗）
            processImage.setHoldingRegister(19, (short) 0); //数据更新标志（1更新0清除）
            processImage.setHoldingRegister(20, (short) 0); //PLC数据更新反馈标志（2秒正脉冲）

            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 21, DataType.FOUR_BYTE_FLOAT, new Float(
                    100)); // 截止6:00或18:00累计流量值
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 23, DataType.FOUR_BYTE_FLOAT, new Float(
                    33)); // 吃完1罐累计流量值
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 25, DataType.FOUR_BYTE_FLOAT, new Float(
                    44)); // 吃完2罐累计流量值
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 27, DataType.FOUR_BYTE_FLOAT, new Float(
                    55)); // 吃完3罐累计流量值
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 29, DataType.FOUR_BYTE_FLOAT, new Float(
                    66)); // 吃完4罐累计流量值
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 31, DataType.FOUR_BYTE_FLOAT, new Float(
                    77)); // 吃完5罐累计流量值
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 33, DataType.FOUR_BYTE_FLOAT, new Float(
                    88)); // 吃完6罐累计流量值

            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 35, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    101)); // 阀门1动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 37, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    102)); // 阀门2动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 39, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    103)); // 阀门3动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 41, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    104)); // 阀门4动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 43, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    105)); // 阀门5动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 45, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    106)); // 阀门6动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 47, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    107)); // 阀门7动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 49, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    108)); // 阀门8动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 51, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    109)); // 阀门9动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 53, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    110)); // 阀门10动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 55, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    111)); // 阀门11动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 57, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    112)); // 阀门12动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 59, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    113)); // 阀门13动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 61, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    114)); // 阀门14动作次数累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 63, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    115)); // 阀门15动作次数累计

            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 65, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    1000)); // 泵1工作时间累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 67, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    1001)); // 泵2工作时间累计
            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 69, DataType.FOUR_BYTE_INT_UNSIGNED, new Integer(
                    1002)); // 泵3工作时间累计


            //processImage.setBinary(16, true);
            //processImage.setBinary(10016, true);

            //processImage.setHoldingRegister(10, (short) 1);
            //processImage.setHoldingRegister(11, (short) 10);
            //processImage.setHoldingRegister(12, (short) 100);
            //processImage.setHoldingRegister(13, (short) 1000);
            //processImage.setHoldingRegister(14, (short) 10000);

            //processImage.setInputRegister(10, (short) 10000);
            //processImage.setInputRegister(11, (short) 1000);
            //processImage.setInputRegister(12, (short) 100);
            //processImage.setInputRegister(13, (short) 10);
            //processImage.setInputRegister(14, (short) 1);

        /*processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 0, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 3, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 7, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 8, true);
        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 14, true);

        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 0, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 7, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 8, true);
        processImage.setBit(RegisterRange.INPUT_REGISTER, 15, 15, true);

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 16, DataType.TWO_BYTE_INT_SIGNED, new Integer(-1968));
        processImage
                .setNumeric(RegisterRange.HOLDING_REGISTER, 17, DataType.FOUR_BYTE_INT_SIGNED, new Long(-123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 19, DataType.FOUR_BYTE_INT_SIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 21, DataType.FOUR_BYTE_FLOAT, new Float(1968.1968));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 23, DataType.EIGHT_BYTE_INT_SIGNED,
                new Long(-123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 27, DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 31, DataType.EIGHT_BYTE_FLOAT, new Double(1968.1968));

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 80, DataType.TWO_BYTE_BCD, new Short((short) 1234));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 81, DataType.FOUR_BYTE_BCD, new Integer(12345678));

        processImage.setString(RegisterRange.HOLDING_REGISTER, 100, DataType.VARCHAR, 20, "Serotonin Software");

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 12288, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Float(
                1968.1968));
        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 12290, DataType.FOUR_BYTE_FLOAT_SWAPPED, new Float(
                -1968.1968));

        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 16, DataType.TWO_BYTE_INT_UNSIGNED, new Integer(0xfff0));
        processImage
                .setNumeric(RegisterRange.INPUT_REGISTER, 17, DataType.FOUR_BYTE_INT_UNSIGNED, new Long(-123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 19, DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 21, DataType.FOUR_BYTE_FLOAT_SWAPPED,
                new Float(1968.1968));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 23, DataType.EIGHT_BYTE_INT_UNSIGNED,
                new Long(-123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 27, DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED, new Long(
                -123456789));
        processImage.setNumeric(RegisterRange.INPUT_REGISTER, 31, DataType.EIGHT_BYTE_FLOAT_SWAPPED, new Double(
                1968.1968));

        processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 50, DataType.EIGHT_BYTE_INT_UNSIGNED, 0);

        processImage.setString(RegisterRange.INPUT_REGISTER, 100, DataType.CHAR, 15, "Software de la Serotonin");
        */
        }
        else if( slaveId == 2){
            processImage.setHoldingRegister(1, (short) 0);
            processImage.setHoldingRegister(2, (short) 1);
            processImage.setHoldingRegister(3, (short) 1);
            processImage.setHoldingRegister(4, (short) 0);
            processImage.setHoldingRegister(5, (short) 0);
            processImage.setHoldingRegister(6, (short) 0);
            processImage.setHoldingRegister(7, (short) 0);
            processImage.setHoldingRegister(8, (short) 0);
            processImage.setHoldingRegister(9, (short) 0);
            processImage.setHoldingRegister(10, (short) 0);
            processImage.setHoldingRegister(11, (short) 0);
            processImage.setHoldingRegister(12, (short) 1);
            processImage.setHoldingRegister(13, (short) 0);
            processImage.setHoldingRegister(14, (short) 0);
            processImage.setHoldingRegister(15, (short) 1);
            processImage.setHoldingRegister(16, (short) 0);
            processImage.setHoldingRegister(17, (short) 0);
            processImage.setHoldingRegister(18, (short) 1);
            processImage.setHoldingRegister(19, (short) 0);
            processImage.setHoldingRegister(20, (short) 1);
            processImage.setHoldingRegister(21, (short) 0);
            processImage.setHoldingRegister(22, (short) 0);
            processImage.setHoldingRegister(23, (short) 0);
            processImage.setHoldingRegister(24, (short) 1);

            processImage.setHoldingRegister(25, (short) 1);
            processImage.setHoldingRegister(26, (short) 1);

            processImage.setNumeric(RegisterRange.HOLDING_REGISTER, 27, DataType.FOUR_BYTE_INT_UNSIGNED,
                    new Long(1234));
        }
        processImage.setExceptionStatus((byte) 151);

        // Add an image listener.
        processImage.addListener(new BasicProcessImageListener(this.notificationManager));

        
        return processImage;
    }
}
