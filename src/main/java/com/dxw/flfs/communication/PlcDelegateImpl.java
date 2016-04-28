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
import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.communication.base.Plc;
import com.dxw.flfs.communication.base.PlcException;
import com.dxw.flfs.communication.base.RegisterType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pronics3
 */
class PlcDelegateImpl implements PlcDelegate {
    Plc plcPrimary, plcSecondary;

    PlcDelegateImpl(Plc plcPrimary, Plc plcSecondary) {
        this.plcPrimary = plcPrimary;
        this.plcSecondary = plcSecondary;
        notificationManager = (NotificationManager)
                ServiceRegistryImpl.getInstance().getService(Services.NOTIFICATION_MANAGER);
    }
    //region event related.
    List<PlcModelChangedListener> listeners = new ArrayList<>();
    public void addModelChangedListener(PlcModelChangedListener l){
        synchronized (listeners){
            this.listeners.add(l);
        }
    }

    public void removeModelChangedListener(PlcModelChangedListener l){
        synchronized (listeners){
            if( listeners.contains(l))
                listeners.remove(l);
        }
    }

    @Override
    public void fireModelChanged(PlcModelChangedEvent event){
        synchronized (listeners) {
            listeners.stream().forEach(l -> {
                l.onChanged(event);
            });
        }
    }
    //endregion

    private PlcModel model = new PlcModel(this);

    //region 做料PLC
    //region Discrete input
    @Override
    public Boolean getEmergencyStopStatus() {
        try {
            boolean status = plcPrimary.getDiscreteInput(PlcRegisterAddress.EMERGENCY_STOP_STATUS);

            model.setEmergencySwitch(status);

            return status;
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public boolean[] getMaterialTowerStatus() {
        try {
            boolean[] result = plcPrimary.getDiscreteInputs(PlcRegisterAddress.MATERIAL_TOWER_STATUS, 2);

            model.setMaterialTowerAlarm(result[0], result[1]);

            return result;
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }
    @Override
    public boolean[] getFermentBarrelStatus() {

        try {
            boolean[] status =  plcPrimary.getDiscreteInputs(PlcRegisterAddress.FERMENT_BARREL_STATUS, 7);

            model.setFermentBarrelStatus(status);

            return status;
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }
    //endregion

    //region Holding registers
    @Override
    public Short getMixingBarrelStatus() {
        try{
            short result = plcPrimary.getRegisterShort(PlcRegisterAddress.MIXING_BARREL_STATUS, RegisterType.HoldingRegister );

            model.setMixingBarrelStatus(result);

            return result;
        }
        catch (PlcException ex){
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public Short getSystemStatus() {
        try{
            short result = plcPrimary.getRegisterShort(PlcRegisterAddress.SYSTEM_STATUS, RegisterType.HoldingRegister );

            model.setSystemStatus(result);

            return result;
        }
        catch (PlcException ex){
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public Short getFermentCountDown() {
        try {
            short result = plcPrimary.getRegisterShort(PlcRegisterAddress.FERMENT_COUNT_DOWN, RegisterType.HoldingRegister);

            model.setFermentCountDown(result);

            return result;
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public short[] getFermentBarrelAction() {
        try {
            short[] data = plcPrimary.getRegistersShort(PlcRegisterAddress.FERMENT_BARREL_WORKING_PARAM, RegisterType.HoldingRegister, 2);
            if( data != null)
                model.setFermentBarrelInNo(data[0], data[1]);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;

    }

    @Override
    public void setProductionParam(float water, float dry, float bacteria, short[] fermentBarrelWeight) {

        float[] data = new float[]{water, dry, bacteria};
        try {
            plcPrimary.setRegisters(PlcRegisterAddress.PRODUCTION_PARAM, data);
            plcPrimary.setRegisters(PlcRegisterAddress.PRODUCTION_PARAM+6, fermentBarrelWeight);

            //update plc proxy model
            if(TimeUtil.isAmOrPm()){
                model.setProductionInstructionAm(water, dry,bacteria,fermentBarrelWeight);
            }
            else{
                model.setProductionInstructionPm(water, dry,bacteria,fermentBarrelWeight);
            }

            sendNotification("发送指令成功");
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
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
            plcPrimary.setRegister(PlcRegisterAddress.SYS_COMMAND_ADDRESS, (short)code);
            sendNotification("发送指令成功");
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
    }
    @Override
    public void setProductionUpdateFlag() {
        try {
            plcPrimary.setRegister(PlcRegisterAddress.PRODUCTION_UPDATE_FLAG, (short)1);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
    }

    @Override
    public Short getProductionUpdateFlag() {
        try {
            return plcPrimary.getRegisterShort(PlcRegisterAddress.PRODUCTION_UPDATE_FLAG, RegisterType.HoldingRegister);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public Short getDataFeedbackFlag1() {
        try {
            return plcPrimary.getRegisterShort(PlcRegisterAddress.PLC_DATA_FEEDBACK_FLAG1, RegisterType.HoldingRegister);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }
    @Override
    public float[] getFlowValues() {
        try {
            return plcPrimary.getRegistersFloat(PlcRegisterAddress.FLOW_VALUES, RegisterType.HoldingRegister, 7);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public int[] getValveAndPumpCondition() {
        try {
            return plcPrimary.getRegistersInt(PlcRegisterAddress.VALVE_ACTION_COUNT, RegisterType.HoldingRegister, 15+3);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }
    //endregion

    //region Input registers
    @Override
    public float getPhValue() {
        try {
            float ph = plcPrimary.getRegisterFloat(PlcRegisterAddress.PH_VALUE, RegisterType.InputRegister);
            model.setPh(ph);
            return ph;
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return -1;
    }
    //endregion
    //endregion

    //region 送料PLC
    @Override
    public void setStyStatus(short[] status) {
         /*
            设置栏位信息：24个栏位，转化为2个short
            */
        try {
            plcSecondary.setRegisters(PlcRegisterAddress.STY_STATUS_ADDRESS, status);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
    }
    @Override
    public void setStyStatusUpdateFlag() {

        try {
            plcSecondary.setRegister(PlcRegisterAddress.STY_STATUS_UPDATE_FLAG, (short)1);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
    }

    @Override
    public Short getStyStatusUpdateFlag() {
        try {
            return plcSecondary.getRegisterShort(PlcRegisterAddress.STY_STATUS_UPDATE_FLAG, RegisterType.HoldingRegister);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }
    //region Secondary plc command




    @Override
    public Short getDataFeedbackFlag2() {
        try {
            return plcSecondary.getRegisterShort(PlcRegisterAddress.PLC_DATA_FEEDBACK_FLAG2, RegisterType.HoldingRegister);
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public Integer getPumpCondition() {
        try {
            int result = plcSecondary.getRegisterInt(PlcRegisterAddress.FEEDING_PUMP_WORKING_HOURS, RegisterType.HoldingRegister);

            return result;
        } catch (PlcException ex) {
            sendNotification(ex.getMessage());
        }
        return null;
    }

    @Override
    public PlcModel getModel() {
        return this.model;
    }
    //endregion

    //endregion



    NotificationManager notificationManager;
    private void sendNotification(String message) {
        if (notificationManager != null) {
            Notification n = new Notification();
            n.setContent(message);
            n.setWhen(System.currentTimeMillis());
            notificationManager.notify("PLC", n);
        }
    }


}
