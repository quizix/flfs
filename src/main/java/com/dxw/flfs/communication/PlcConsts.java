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

    /*
        Discretes Input: 只读，二进制，寄存器地址在
    */
    //final static 
    final static int SYS_COMMAND_ADDRESS = 100;
    final static int STY_STATUS_ADDRESS = 200;
    final static int MATERIAL_TOWER_ALARM = 300;
    final static int PRODUCTION_UPDATE_FLAG = 330;
    final static int MIXING_BARREL_STATUS = 320;
    final static int FERMENT_BARREL_STATUS = 340;
    final static int STY_STATUS_UPDATE_FLAG = 200;

}
