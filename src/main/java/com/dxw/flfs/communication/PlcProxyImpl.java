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
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ModbusRequest;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import java.util.Calendar;

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
    public static PlcProxy getInstance(){
        if(instance==null){
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
        primaryParams.setPort(502);
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

            if (res.getExceptionCode() != 0) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
                
            }
        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }
    
    private void sendNotification(String message){
        Notification n = new Notification();
        n.setContent(message);
        n.setWhen( System.currentTimeMillis());
        
        notificationManager.notify("PLC", n);
        
    }

    @Override
    public void setProductionParam(float mixingWater, float mixingFeed, float bacteria, float[] fermentBarrelWeight) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTimeCalibration() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        Calendar c = Calendar.getInstance();
        try {
            master.init();
            ModbusRequest req;
            req = new WriteRegistersRequest(1, 101,
                    new short[]{(short) c.get(Calendar.HOUR),
                        (short) c.get(Calendar.MINUTE), (short) c.get(Calendar.SECOND)});
            ModbusResponse res = master.send(req);

            if (res.getExceptionCode() != 0) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
    }

    @Override
    public void setProductionUpdateFlag() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new WriteRegistersRequest(SLAVE_ID, PlcConsts.PRODUCTION_UPDATE_FLAG,
                    new short[]{1});
            ModbusResponse res = master.send(req);

            if (res.getExceptionCode() != 0) {
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
            ModbusRequest req = new ReadInputRegistersRequest(SLAVE_ID, PlcConsts.PRODUCTION_UPDATE_FLAG, 1);
            ReadInputRegistersResponse res = (ReadInputRegistersResponse) master.send(req);
            if (res.getExceptionCode() != 0) {
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
            if (res.getExceptionCode() != 0) {
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
    public short[] getFermentBarrelStatus() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadInputRegistersRequest(SLAVE_ID, PlcConsts.FERMENT_BARREL_STATUS, 13 + 2);
            ReadInputRegistersResponse res = (ReadInputRegistersResponse) master.send(req);
            if (res.getExceptionCode() != 0) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            return data;

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
    public float[] getPhValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public short[] getMaterialTowerAlarm() {
        ModbusMaster master = getTcpMaster(this.primaryIp);
        try {
            master.init();
            ModbusRequest req = new ReadInputRegistersRequest(SLAVE_ID, PlcConsts.MATERIAL_TOWER_ALARM, 2);
            ReadInputRegistersResponse res = (ReadInputRegistersResponse) master.send(req);
            if (res.getExceptionCode() != 0) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            return data;

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            if (res.getExceptionCode() != 0) {
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

            if (res.getExceptionCode() != 0) {
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
            if (res.getExceptionCode() != 0) {
                sendNotification("Plc发生异常:" + res.getExceptionMessage());
            }
            short[] data = res.getShortData();
            return data[0];

        } catch (ModbusInitException | ModbusTransportException ex) {
            sendNotification("Modbus发生异常:" + ex.getMessage());
        }
        return -1;
    }
}
