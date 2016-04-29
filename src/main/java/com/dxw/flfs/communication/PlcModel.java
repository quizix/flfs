package com.dxw.flfs.communication;

import java.util.Date;

/**
 * 表示PLC的状态
 * Created by zhang on 2016-04-19.
 */
public class PlcModel {

    PlcDelegate proxy;
    public PlcModel(PlcDelegate proxy){
        this.proxy = proxy;
    }

    private void fireModelChange(PlcModelChangedEvent event){
        proxy.fireModelChanged(event);
    }

    private void fireModelChange(long field){
        PlcModelChangedEvent event
                 = new PlcModelChangedEvent(new Date().getTime(), field, this);
        proxy.fireModelChanged(event);
    }

    private static final int FERMENT_BARREL_COUNT = 7;


    public Boolean getEmergencySwitch() {
        return emergencySwitch;
    }

    void setEmergencySwitch(Boolean emergencySwitch) {
        this.emergencySwitch = emergencySwitch;

        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.EMERGENCY_SWITCH);
    }

    public Short getSystemStatus() {
        return systemStatus;
    }

    void setSystemStatus(Short systemStatus) {
        this.systemStatus = systemStatus;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.SYSTEM_STATUS);
    }

    public Boolean getMaterialTowerLowAlarm() {
        return materialTowerLowAlarm;
    }

    public Boolean getMaterialTowerEmptyAlarm() {
        return materialTowerEmptyAlarm;
    }

    void setMaterialTowerAlarm(Boolean materialTowerLowAlarm,Boolean materialTowerEmptyAlram) {
        this.materialTowerLowAlarm = materialTowerLowAlarm;
        this.materialTowerEmptyAlarm = materialTowerEmptyAlram;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.MATERIAL_TOWER_ALARM);
    }


    public Short getMixingBarrelStatus() {
        return mixingBarrelStatus;
    }

    void setMixingBarrelStatus(Short mixingBarrelStatus) {
        this.mixingBarrelStatus = mixingBarrelStatus;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.MIXING_BARREL_STATUS);
    }

    public boolean[] getFermentBarrelStatus() {
        return fermentBarrelStatus;
    }

    void setFermentBarrelStatus(boolean[] fermentBarrelStatus) {
        this.fermentBarrelStatus = fermentBarrelStatus;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.FERMENT_BARREL_STATUS);
    }

    public Float getPh() {
        return ph;
    }

    void setPh(Float ph) {
        this.ph = ph;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.PH_VALUE);
    }

    public Short getFermentCountDown() {
        return fermentCountDown;
    }

    void setFermentCountDown(Short fermentCountDown) {
        this.fermentCountDown = fermentCountDown;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.FERMENT_COUNT_DOWN);
    }

    public Short getFermentBarrelInNo() {
        return fermentBarrelInNo;
    }
    public Short getFermentBarrelOutNo() {
        return fermentBarrelOutNo;
    }

    void setFermentBarrelInNo(Short fermentBarrelInNo,Short fermentBarrelOutNo) {
        this.fermentBarrelInNo = fermentBarrelInNo;
        this.fermentBarrelOutNo = fermentBarrelOutNo;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.FERMENT_BARREL_IN_OUT);
    }


    public Short getAccumulateFlowAm() {
        return accumulateFlowAm;
    }

    void setAccumulateFlowAm(Short accumulateFlowAm) {
        this.accumulateFlowAm = accumulateFlowAm;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.ACCUMULATE_FLOW_AM);
    }

    public Short getAccumulateFlowPm() {
        return accumulateFlowPm;
    }

    void setAccumulateFlowPm(Short accumulateFlowPm) {
        this.accumulateFlowPm = accumulateFlowPm;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.ACCUMULATE_FLOW_PM);
    }

    public short[] getAccumulateFlows() {
        return accumulateFlows;
    }

    void setAccumulateFlows(short[] accumulateFlows) {
        this.accumulateFlows = accumulateFlows;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.ACCUMULATE_FLOWS);
    }

    public Float getWaterAm() {
        return waterAm;
    }

    public Float getDryAm() {
        return dryAm;
    }

    public Float getBacteriaAm() {
        return bacteriaAm;
    }

    public short[] getProductionAmountsAm() {
        return productionAmountsAm;
    }

    void setProductionInstructionAm(Float waterAm, Float dryAm, Float bacteriaAm, short[] productionAmountsAm) {
        this.waterAm = waterAm;
        this.dryAm = dryAm;
        this.bacteriaAm = bacteriaAm;
        this.productionAmountsAm = productionAmountsAm;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.PRODUCTION_INSTRUCTION_AM);
    }

    void setProductionInstructionPm(Float waterPm, Float dryPm, Float bacteriaPm, short[] productionAmountsPm) {
        this.waterPm = waterPm;
        this.dryPm = dryPm;
        this.bacteriaPm = bacteriaPm;
        this.productionAmountsPm = productionAmountsPm;
        lastUpdateTime = new Date().getTime();
        fireModelChange(PlcModelField.PRODUCTION_INSTRUCTION_PM);
    }

    public Float getWaterPm() {
        return waterPm;
    }


    public Float getDryPm() {
        return dryPm;
    }


    public Float getBacteriaPm() {
        return bacteriaPm;
    }


    public short[] getProductionAmountsPm() {
        return productionAmountsPm;
    }


    ////////////////////////////////////////
    //Plc system
    private Boolean emergencySwitch = null;
    private Short systemStatus = null;

    ////////////////////////////////////////
    //material tower
    private Boolean materialTowerLowAlarm = null;
    private Boolean materialTowerEmptyAlarm = null;

    ////////////////////////////////////////
    //mixing barrel
    private Short mixingBarrelStatus = null;

    ////////////////////////////////////////
    //ferment barrel
    private boolean[] fermentBarrelStatus = new boolean[FERMENT_BARREL_COUNT];
    private Float ph = 0F;
    private Short fermentCountDown = 0;
    private Short fermentBarrelInNo = 0;
    private Short fermentBarrelOutNo = 0;

    private Short accumulateFlowAm = 0;
    private Short accumulateFlowPm = 0;
    private short[] accumulateFlows = new short[FERMENT_BARREL_COUNT];

    //////////////////////////////////////////////
    //production params
    private Float waterAm = 0f;
    private Float dryAm = 0f;
    private Float bacteriaAm = 0F;
    private short[] productionAmountsAm= new short[FERMENT_BARREL_COUNT];

    private Float waterPm = 0f;
    private Float dryPm = 0f;
    private Float bacteriaPm = 0f;
    private short[] productionAmountsPm= new short[FERMENT_BARREL_COUNT];

    private Long lastUpdateTime = null;

}
