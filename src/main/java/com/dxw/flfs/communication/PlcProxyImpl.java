/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication;

import com.dxw.common.ms.Notification;
import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;

/**
 * @author pronics3
 */
class PlcProxyImpl implements PlcProxy {
    Plc plc;
    PlcProxyImpl(Plc plc) {
        this.plc = plc;
        notificationManager = (NotificationManager)
                ServiceRegistryImpl.getInstance().lookupService(Services.NOTIFICATION_MANAGER);
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
        try {
            plc.setRegister(PlcRegisterAddress.SYS_COMMAND_ADDRESS, (short)code);
            sendNotification("发送指令成功");
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
    }

    

    @Override
    public void setProductionParam(float mixingWater, float mixingFeed, float bacteria, short[] fermentBarrelWeight) {
        
        float[] data = new float[]{mixingWater, mixingFeed, bacteria};
        try {
            plc.setRegisters(PlcRegisterAddress.PRODUCTION_PARAM, data);
            plc.setRegisters(PlcRegisterAddress.PRODUCTION_PARAM+6, fermentBarrelWeight);
            sendNotification("发送指令成功");
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }

        
    }

    @Override
    public boolean getEmergenyStopStatus() {
        try {
            return plc.getDiscreteInput(PlcRegisterAddress.FERMENT_BARREL_STATUS);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return false;
    }

    @Override
    public void setProductionUpdateFlag() {
        try {
            plc.setRegister(PlcRegisterAddress.PRODUCTION_UPDATE_FLAG, (short)1);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
    }

    @Override
    public short getProductionUpdateFlag() {
        try {
            return plc.getRegisterShort(PlcRegisterAddress.PRODUCTION_UPDATE_FLAG, RegisterType.HoldingRegister);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return -1;
    }

    
    
    @Override
    public boolean[] getFermentBarrelStatus() {
        
        try {
            return plc.getDiscreteInputs(PlcRegisterAddress.FERMENT_BARREL_STATUS, 7);
        } catch (PlcException ex) {
           sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public float getPhValue() {
        try {
            return plc.getRegisterFloat(PlcRegisterAddress.PH_VALUE, RegisterType.InputRegister);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return -1;
    }

    @Override
    public boolean[] getMaterialTowerStatus() {
        try {
            return plc.getDiscreteInputs(PlcRegisterAddress.MATERIAL_TOWER_STATUS, 2);
        } catch (PlcException ex) {
           sendNotification(ex.getMessage());
        }
        return null;
    }

    /**
     * *
     * 获取15个阀门的动作次数和
     *
     * @return
     *
     */
    @Override
    public int[] getValveAndPumpCondition() {
        try {
            return plc.getRegistersInt(PlcRegisterAddress.VALVE_ACTION_COUNT, RegisterType.HoldingRegister, 15+3);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public void setStyStatus(boolean[] status) {
         /*
            设置栏位信息：24个栏位，转化为2个short
            */
        try {
            plc.setCoils(PlcRegisterAddress.STY_STATUS_ADDRESS, status);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
    }

    @Override
    public void setStyStatusUpdateFlag() {
        
        try {
            plc.setRegister(PlcRegisterAddress.STY_STATUS_UPDATE_FLAG, (short)1);
        } catch (PlcException ex) {
           sendNotification(ex.getMessage());
        }
    }

    @Override
    public short getStyStatusUpdateFlag() {
       try {
            return plc.getRegisterShort(PlcRegisterAddress.STY_STATUS_UPDATE_FLAG, RegisterType.HoldingRegister);
        } catch (PlcException ex) {
           sendNotification(ex.getMessage());
        }
       return -1;
    }

    @Override
    public float[] getFlowValues() {
        try {
            return plc.getRegistersFloat(PlcRegisterAddress.FLOW_VALUES, RegisterType.HoldingRegister, 7);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public short getFermentCountDown() {
        try {
            return plc.getRegisterShort(PlcRegisterAddress.FERMENT_COUNT_DOWN, RegisterType.HoldingRegister);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return -1;
    }

    

    @Override
    public short[] getFermentBarrelWorkingParam() {
        try {
            return plc.getRegistersShort(PlcRegisterAddress.FERMENT_BARREL_WORKING_PARAM, RegisterType.HoldingRegister, 2);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
        
    }

    NotificationManager notificationManager;
    private void sendNotification(String message) {
        if (notificationManager != null) {
            Notification n = new Notification();
            n.setContent(message);
            n.setWhen(System.currentTimeMillis());
            notificationManager.notify("PLC", n);
        }
    }

    @Override
    public short getMixingBarrelStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
