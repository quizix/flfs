/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication;

/**
 *
 * @author pronics3
 */
public class PlcRegisterAddress {

    /*final static int COIL_STATUS_BASE = 0;
    final static int INPUT_STATUS_BASE = 10000;
    final static int INPUT_REGISTER_BASE = 30000;
    final static int HOLDING_REGISTER_BASE = 40000;*/

    //////////////////////////////////////////////////////
    /////////   primary plc
    /*
        discrete input addresses
    */
    final static int EMERGENCY_STOP_STATUS = 1;
    final static int MATERIAL_TOWER_STATUS = 2;
    final static int FERMENT_BARREL_STATUS = 4;

    /*
        holding register addresses
    */
    final static int PRODUCTION_PARAM = 1;
    final static int SYS_COMMAND_ADDRESS = 13;
    final static int PRODUCTION_UPDATE_FLAG = 14;
    final static int FLOW_VALUES = 15;
    final static int FERMENT_COUNT_DOWN = 29;
    final static int FERMENT_BARREL_WORKING_PARAM = 30;
    final static int VALVE_ACTION_COUNT = 32;
    final static int MIXING_BARREL_STATUS = 68;
    /*
        input register addresses
    */
    final static int PH_VALUE = 1;

    //////////////////////////////////////////////////////
    /////////   secondary plc
    
    final static int STY_STATUS_ADDRESS = 1;
    final static int STY_STATUS_UPDATE_FLAG = 25;

}
