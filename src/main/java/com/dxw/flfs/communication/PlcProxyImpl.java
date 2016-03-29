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
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.ReadResponse;
import com.serotonin.modbus4j.msg.WriteCoilsRequest;
import com.serotonin.modbus4j.msg.WriteCoilsResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

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
            short[] data = Converter.floatsAndShortsToShorts(new float[]{mixingWater, mixingFeed, bacteria}, fermentBarrelWeight);
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
            return Converter.shortsToFloat(data);

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

    /***
     * 获取15个阀门的动作次数和
     * @return 
     * 
     */
    @Override
    public int[] getValveAndPumpCondition() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadHoldingRegistersRequest(SLAVE_ID, PlcConsts.VALVE_ACTION_COUNT, 2 * (15+3));
            ReadHoldingRegistersResponse res = (ReadHoldingRegistersResponse) master.send(req);
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            return Converter.shortsToInts(data);

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return null;
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

    

    @Override
    public boolean getCoil(int offset) {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadCoilsRequest(SLAVE_ID, offset, 1);
            ReadResponse res = (ReadCoilsResponse) master.send(req);
            
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            
            boolean[] result = res.getBooleanData();
            if( result != null && result.length >0)
                return result[0];

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return false;
    }

    @Override
    public void setCoil(int offset, boolean value) {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new WriteCoilsRequest(SLAVE_ID, offset, new boolean[]{value});
            WriteCoilsResponse res = (WriteCoilsResponse) master.send(req);
            
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        
    }

    @Override
    public boolean getDiscreteInput(int offset) {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadDiscreteInputsRequest(SLAVE_ID, offset, 1);
            ReadResponse res = (ReadDiscreteInputsResponse) master.send(req);
            
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            
            boolean[] result = res.getBooleanData();
            if( result != null && result.length >0)
                return result[0];

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return false;
    }

    @Override
    public short getRegisterShort(int offset, int registerType) {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req;
            ReadResponse res;
            if (registerType == 0) {
                req = new ReadInputRegistersRequest(SLAVE_ID, offset, 1);
                res = (ReadInputRegistersResponse) master.send(req);
            } else {
                req = new ReadHoldingRegistersRequest(SLAVE_ID, offset, 1);
                res = (ReadHoldingRegistersResponse) master.send(req);
            }

            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            if (data != null && data.length > 0) {
                return data[0];
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return -1;

    }

    @Override
    public float getRegisterFloat(int offset, int registerType) {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req;
            ReadResponse res;
            if (registerType == 0) {
                req = new ReadInputRegistersRequest(SLAVE_ID, offset, 2);
                res = (ReadInputRegistersResponse) master.send(req);
            } else {
                req = new ReadHoldingRegistersRequest(SLAVE_ID, offset, 2);
                res = (ReadHoldingRegistersResponse) master.send(req);
            }

            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            if( data != null && data.length >=2)
                return Converter.shortsToFloat(data);

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return Float.MIN_VALUE;
    }

    @Override
    public int getRegisterInt(int offset, int registerType) {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req;
            ReadResponse res;
            if (registerType == 0) {
                req = new ReadInputRegistersRequest(SLAVE_ID, offset, 2);
                res = (ReadInputRegistersResponse) master.send(req);
            } else {
                req = new ReadHoldingRegistersRequest(SLAVE_ID, offset, 2);
                res = (ReadHoldingRegistersResponse) master.send(req);
            }

            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            if( data != null && data.length >=2)
                return Converter.shortsToInt(data);

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return -1;
    }

    @Override
    public void setRegister(int offset, int value) {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            
            short[] data = Converter.intToShorts(value);
            ModbusRequest req = new WriteRegistersRequest(SLAVE_ID, offset, data);
            WriteRegistersResponse res = (WriteRegistersResponse) master.send(req);
        
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }

    @Override
    public void setRegister(int offset, short value) {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            
            ModbusRequest req = new WriteRegistersRequest(SLAVE_ID, offset, new short[]{value});
            WriteRegistersResponse res = (WriteRegistersResponse) master.send(req);
        
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }

    @Override
    public void setRegister(int offset, float value) {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            
            short[] data = Converter.floatToShorts(value);
            ModbusRequest req = new WriteRegistersRequest(SLAVE_ID, offset, data);
            WriteRegistersResponse res = (WriteRegistersResponse) master.send(req);
        
            if (res.isException()) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }

}
