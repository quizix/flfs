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
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusInitException;

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
        notificationManager = (NotificationManager) registry.lookupService(Services.NOTIFICATION_MANAGER);

    }

    public void start() throws ModbusInitException {
        ModbusFactory factory = new ModbusFactory();

        ModbusSlaveSet listener = factory.createTcpSlave(false);
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

        processImage.setCoil(10, true);
        processImage.setCoil(11, false);
        processImage.setCoil(12, true);
        processImage.setCoil(13, true);
        processImage.setCoil(14, false);

        processImage.setInput(10, false);
        processImage.setInput(11, false);
        processImage.setInput(12, true);
        processImage.setInput(13, false);
        processImage.setInput(14, true);

        processImage.setBinary(16, true);
        processImage.setBinary(10016, true);

        processImage.setHoldingRegister(10, (short) 1);
        processImage.setHoldingRegister(11, (short) 10);
        processImage.setHoldingRegister(12, (short) 100);
        processImage.setHoldingRegister(13, (short) 1000);
        processImage.setHoldingRegister(14, (short) 10000);

        processImage.setInputRegister(10, (short) 10000);
        processImage.setInputRegister(11, (short) 1000);
        processImage.setInputRegister(12, (short) 100);
        processImage.setInputRegister(13, (short) 10);
        processImage.setInputRegister(14, (short) 1);

        processImage.setBit(RegisterRange.HOLDING_REGISTER, 15, 0, true);
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

        processImage.setExceptionStatus((byte) 151);

        // Add an image listener.
        processImage.addListener(new BasicProcessImageListener(this.notificationManager));

        return processImage;
    }
}
