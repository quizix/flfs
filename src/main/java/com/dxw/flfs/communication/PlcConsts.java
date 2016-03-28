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
public class PlcConsts {
    /*final static int COIL_STATUS_BASE = 0;
    final static int INPUT_STATUS_BASE = 10000;
    final static int INPUT_REGISTER_BASE = 30000;
    final static int HOLDING_REGISTER_BASE = 40000;*/

    final static int EMERGENCY_STOP_STATUS = 1;
    final static int MATERIAL_TOWER_STATUS = 2;
    final static int FERMENT_BARREL_STATUS = 4;
    
    final static int PRODUCTION_PARAM = 1;
    final static int SYS_COMMAND_ADDRESS = 13;
    final static int PRODUCTION_UPDATE_FLAG = 14;
    final static int VALVE_ACTION_COUNT = 32;
    
    final static int MIXING_BARREL_STATUS = 320;
    final static int STY_STATUS_ADDRESS = 100;
    final static int STY_STATUS_UPDATE_FLAG = 200;
    
    final static int PH_VALUE = 1;

}
