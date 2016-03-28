/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.Services;
import com.dxw.common.services.ServiceRegistryImpl;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.base.ModbusUtils;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;

/**
 * @author pronics3
 */
public class PlcProxyImpl implements PlcProxy {

    private final String primaryIp;
    private final String secondaryIp;
    private final static int SLAVE_ID = 2;

    private PlcProxyImpl(String primaryIp, String secondaryIp) {
        this.primaryIp = primaryIp;
        this.secondaryIp = secondaryIp;
        init();
    }

    private static PlcProxy instance = null;

    public static PlcProxy getInstance() {
        if (instance == null) {
            instance = new PlcProxyImpl("127.0.0.1", "127.0.0.1");
        }
        return instance;
    }

    NotificationManager notificationManager;

    private void init() {
        notificationManager = (NotificationManager) ServiceRegistryImpl.getInstance()
                .lookupService(Services.NOTIFICATION_MANAGER);

    }

    private ModbusMaster getTcpMaster(String ip) {
        ModbusFactory factory = new ModbusFactory();
        IpParameters primaryParams = new IpParameters();
        primaryParams.setHost(ip);
        primaryParams.setPort(ModbusUtils.TCP_PORT);
        return factory.createTcpMaster(primaryParams, false);
    }

    @Override
    public void start() {
        sendSysCommand((short) 1);
    }

    @Override
    public void halt() {
        sendSysCommand((short) 0);
    }

    @Override
    public void clean() {
        sendSysCommand((short) 2);
    }

    /**
     * 发送系统工作命令 包括0停机；1运行；2清洗
     *
     * @param code
     */
    private void sendSysCommand(short code) {
        ModbusMaster master = getTcpMaster(this.primaryIp);

        try {
            master.init();
            ModbusRequest req = new WriteRegistersRequest(SLAVE_ID, PlcConsts.SYS_COMMAND_ADDRESS,
                    new short[]{code});
            ModbusResponse res = master.send(req);

            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            } else {
                sendNotification("发送指令成功");
            }
        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }

    private void sendNotification(String message) {
        if (notificationManager != null) {
            Notification n = new Notification();
            n.setContent(message);
            n.setWhen(System.currentTimeMillis());
            notificationManager.notify("PLC", n);
        }
    }

    @Override
    public void setProductionParam(float mixingWater, float mixingFeed, float bacteria, short[] fermentBarrelWeight) {
        ModbusMaster master = getTcpMaster(this.primaryIp);

        try {
            master.init();
            short[] data = floatsAndShortsToShorts(new float[]{mixingWater, mixingFeed, bacteria}, fermentBarrelWeight);
            ModbusRequest req = new WriteRegistersRequest(SLAVE_ID, PlcConsts.PRODUCTION_PARAM, data);
            ModbusResponse res = master.send(req);

            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            } else {
                sendNotification("发送指令成功");
            }
        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }

    @Override
    public boolean getEmergenyStopStatus() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadDiscreteInputsRequest(SLAVE_ID, PlcConsts.FERMENT_BARREL_STATUS, 7);
            ReadDiscreteInputsResponse res = (ReadDiscreteInputsResponse) master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());

            } else {
                boolean[] data = res.getBooleanData();
                if ((data != null) && data.length > 0) {
                    return data[0];
                }
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return false;
    }

    @Override
    public void setProductionUpdateFlag() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new WriteRegistersRequest(SLAVE_ID, PlcConsts.PRODUCTION_UPDATE_FLAG,
                    new short[]{1});
            ModbusResponse res = master.send(req);

            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }

    @Override
    public short getProductionUpdateFlag() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadHoldingRegistersRequest(SLAVE_ID, PlcConsts.PRODUCTION_UPDATE_FLAG, 1);
            ReadHoldingRegistersResponse res = (ReadHoldingRegistersResponse) master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            return data[0];

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return -1;
    }

    @Override
    public short getMixingBarrelStatus() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadInputRegistersRequest(SLAVE_ID, PlcConsts.MIXING_BARREL_STATUS, 1);
            ReadInputRegistersResponse res = (ReadInputRegistersResponse) master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            return data[0];

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return -1;
    }

    @Override
    public boolean[] getFermentBarrelStatus() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadDiscreteInputsRequest(SLAVE_ID, PlcConsts.FERMENT_BARREL_STATUS, 7);
            ReadDiscreteInputsResponse res = (ReadDiscreteInputsResponse) master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            } else {
                boolean[] data = res.getBooleanData();
                return data;
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return null;
    }

    @Override
    public float getPumpedVolumn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float[] getPumpedVolumns() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float getPhValue() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadInputRegistersRequest(SLAVE_ID, PlcConsts.PH_VALUE, 2);
            ReadInputRegistersResponse res = (ReadInputRegistersResponse) master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            return shortsToFloat(data);

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return Float.MIN_VALUE;
    }

    @Override
    public boolean[] getMaterialTowerStatus() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadDiscreteInputsRequest(SLAVE_ID, PlcConsts.MATERIAL_TOWER_STATUS, 2);
            ReadDiscreteInputsResponse res = (ReadDiscreteInputsResponse) master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            } else {
                boolean[] data = res.getBooleanData();
                return data;
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return null;
    }

    /**
     * 预留，先不实现 获取水泵工作时间
     *
     * @return
     */
    @Override
    public float[] getValveActionCount() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadHoldingRegistersRequest(SLAVE_ID, PlcConsts.VALVE_ACTION_COUNT, 2 * 15);
            ReadHoldingRegistersResponse res = (ReadHoldingRegistersResponse) master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            return shortsToFloats(data);

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return null;
    }

    /**
     * 预留，先不实现 获取水泵工作时间
     *
     * @return
     */
    @Override
    public float getWaterPumpWorkingTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 预留，先不实现 获取转子泵工作时间
     *
     * @return
     */
    @Override
    public float getRotorPumpWorkingTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStyStatus(short[] status) {
        /*
        设置栏位信息：48个栏位，转化为3个short
         */
        ModbusMaster master = getTcpMaster(this.secondaryIp);
        try {
            master.init();
            ModbusRequest req = new WriteRegistersRequest(SLAVE_ID, PlcConsts.STY_STATUS_ADDRESS, status);
            ModbusResponse res = master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }

    /**
     * 预留，先不实现 获取隔膜泵工作时间
     *
     * @return
     */
    @Override
    public float getDiaphragmPumpWorkingTime() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStyStatusUpdateFlag() {
        ModbusMaster master = getTcpMaster(this.secondaryIp);
        try {
            master.init();
            ModbusRequest req = new WriteRegistersRequest(SLAVE_ID, PlcConsts.STY_STATUS_UPDATE_FLAG,
                    new short[]{1});
            ModbusResponse res = master.send(req);

            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }

    @Override
    public short getStyStatusUpdateFlag() {
        ModbusMaster master = getTcpMaster(this.secondaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadInputRegistersRequest(SLAVE_ID, PlcConsts.STY_STATUS_UPDATE_FLAG, 1);
            ReadInputRegistersResponse res = (ReadInputRegistersResponse) master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            return data[0];

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return -1;
    }

    private float shortsToFloat(short[] data) {
        return shortsToFloat(data, 0);
    }

    private float shortsToFloat(short[] data, int offset) {
        if (data == null || data.length < offset + 2) {
            throw new IllegalArgumentException("invalid argument");
        }
        float f = Float.intBitsToFloat((((data[offset] >> 8) & 0xff) << 24) | ((data[offset] & 0xff) << 16)
                | (((data[offset + 1] >> 8) & 0xff) << 8) | (data[offset + 1] & 0xff));

        return f;
    }

    private float[] shortsToFloats(short[] data) {
        if (data == null || data.length < 2) {
            throw new IllegalArgumentException("from must not be null or from.length must greater than 2");
        }
        int count = data.length / 2;
        float[] value = new float[count];
        for (int i = 0; i < count; i++) {
            value[i] = shortsToFloat(data, i * 2);
        }
        return value;
    }

    private short[] floatToShorts(float data) {
        int x = Float.floatToIntBits(data);
        short[] result = new short[2];
        result[0] = (short) ((x >> 16) & 0xffff);
        result[1] = (short) (x & 0xffff);
        return result;
    }

    private short[] floatsToShorts(float[] data) {
        int length = data.length;

        short[] result = new short[length * 2];
        for (int i = 0; i < length; i++) {
            int x = Float.floatToIntBits(data[i]);

            result[i * 2] = (short) ((x >> 16) & 0xffff);
            result[i * 2 + 1] = (short) (x & 0xffff);
        }
        return result;
    }

    private short[] floatsAndShortsToShorts(float[] floats, short[] shorts) {

        short[] result = new short[floats.length * 2 + shorts.length];
        int offset = 0;
        for (int i = 0; i < floats.length; i++) {
            int x = Float.floatToIntBits(floats[i]);
            result[offset] = (short) ((x >> 16) & 0xffff);
            result[offset + 1] = (short) (x & 0xffff);
            offset += 2;
        }

        for (int i = 0; i < shorts.length; i++) {
            result[offset++] = shorts[i];
        }
        return result;
    }

    @Override
    public boolean getCoil(int offset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCoil(int offset, boolean value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getDiscreteInput(int offset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public short getRegisterShort(int offset, int registerType) {
        if (registerType == 0) {
            //input register
            return 0;
        } else {
            //holding register
            ModbusMaster master = getTcpMaster(this.primaryIp);
            try {
                master.init();
                ModbusRequest req = new ReadHoldingRegistersRequest(SLAVE_ID, offset, 1);
                ReadHoldingRegistersResponse res = (ReadHoldingRegistersResponse) master.send(req);
                if (res.isException()) {
                    sendNotification("Plc发生异常:" + res.getExceptionMessage());
                }
                short[] data = res.getShortData();
                for(int i=0;i<data.length;i++){
                    System.out.println(data[i]);
                }
                return data[0];

            } catch (ModbusInitException | ModbusTransportException ex) {
                sendNotification("Modbus发生异常:" + ex.getMessage());
            }
            return -1;
        }
    }

    @Override
    public float getRegisterFloat(int offset, int registerType) {
        if (registerType == 0) {
            //input register
            return 0;
        } else {
            //holding register
            ModbusMaster master = getTcpMaster(this.primaryIp);
            try {
                master.init();
                ModbusRequest req = new ReadHoldingRegistersRequest(SLAVE_ID, offset, 2);
                ReadHoldingRegistersResponse res = (ReadHoldingRegistersResponse) master.send(req);
                if (res.isException()) {
                    sendNotification("Plc发生异常:" + res.getExceptionMessage());
                }
                short[] data = res.getShortData();
                return shortsToFloat(data);

            } catch (ModbusInitException | ModbusTransportException ex) {
                sendNotification("Modbus发生异常:" + ex.getMessage());
            }
            return Float.MIN_VALUE;
        }
    }

    @Override
    public int getRegisterInt(int offset, int registerType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRegister(int offset, int value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRegister(int offset, short value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRegister(int offset, float value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
