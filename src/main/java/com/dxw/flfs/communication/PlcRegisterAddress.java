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
    final static int EMERGENCY_STOP_STATUS = 1;         //紧停开关（0正常，1紧停）
    final static int MATERIAL_TOWER_STATUS = 2;         //料位低警报（0正常，1警报），料位空警报（0正常，1警报）
    final static int FERMENT_BARREL_STATUS = 4;         //发酵罐状态（0有料，1空）

    /*
        holding register addresses
    */
    final static int MIXING_BARREL_STATUS = 1;          //搅拌桶状态(0空闲，1忙)
    final static int SYSTEM_STATUS = 2;                 //系统状态(1停机2做料3清洗4紧停5冷启动)
    final static int FERMENT_COUNT_DOWN = 3;            //首次做料发酵时间12小时倒计时
    final static int FERMENT_BARREL_WORKING_PARAM = 4;  //预备或正在进料的发酵罐号,预备或正在出料的发酵罐号
    final static int PRODUCTION_PARAM = 6;              //做料参数，加水量 加干料量 加菌液量 7罐做料量
    final static int SYS_COMMAND_ADDRESS = 18;          //系统命令（0停机1运行2清洗）
    final static int PRODUCTION_UPDATE_FLAG = 19;       //数据更新标志（1更新0不更新）
    final static int PLC_DATA_FEEDBACK_FLAG1 = 20;      //PLC数据更新反馈标志（2秒正脉冲）
    final static int FLOW_VALUES = 21;                  //截止6:00或18:00累计流量值
    final static int VALVE_ACTION_COUNT = 35;           //15个阀门动作次数累计，3个泵工作时间累计

    /*
        input register addresses
    */
    final static int PH_VALUE = 3;                      //pH值

    //////////////////////////////////////////////////////
    /////////   secondary plc
    final static int STY_STATUS_ADDRESS = 1;            //24个栏位状态(0空闲1使用)
    final static int STY_STATUS_UPDATE_FLAG = 25;       //数据更新标志（1更新0不更新）
    final static int PLC_DATA_FEEDBACK_FLAG2 = 26;      //PLC数据更新反馈标志（2秒正脉冲）
    final static int FEEDING_PUMP_WORKING_HOURS = 27;   //喂料泵工作时间累计
}
