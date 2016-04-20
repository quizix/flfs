package com.dxw.flfs.communication;

/**
 * Created by zhang on 2016-04-19.
 */
public class PlcModelField {
    ////////////////////////////////////////
    //system
    public static final long EMERGENCY_SWITCH = 0x1;
    public static final long SYSTEM_STATUS = 0x2;


    ////////////////////////////////////////
    //material tower
    public static final long MATERIAL_TOWER_ALARM = 0x11;


    ////////////////////////////////////////
    //mixing barrel
    public static final long MIXING_BARREL_STATUS=0x101;


    ////////////////////////////////////////
    //ferment barrel
    public static final long FERMENT_BARREL_STATUS = 0x1001;
    public static final long PH_VALUE= 0x1002;
    public static final long FERMENT_COUNT_DOWN= 0x1003;
    public static final long FERMENT_BARREL_IN_OUT = 0x1004;

    public static final long ACCUMULATE_FLOW_AM= 0x1005;
    public static final long ACCUMULATE_FLOW_PM= 0x1006;
    public static final long ACCUMULATE_FLOWS= 0x1007;


    //////////////////////////////////////////////
    //production params
    public static final long PRODUCTION_INSTRUCTION_AM = 0x10001;
    public static final long PRODUCTION_INSTRUCTION_PM = 0x10001;


}
