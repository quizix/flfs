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
public interface PlcProxy {

    /**
     * 发送运行命令
     */
    void start();

    /**
     * 发送停机命令
     */
    void halt();

    /**
     * 发送清洗命令
     */
    void clean();

    /**
     * 获取紧急停止状态
     * @return 
     */
    boolean getEmergenyStopStatus();
    /**
     * 发送做料参数，单位kg 一天两次，上午6:00及下午6:00
     *
     * @param mixingWater 加水量
     * @param mixingFeed 干料量
     * @param bacteria 菌液量
     * @param fermentBarrelWeight 发酵罐每罐做量
     */
    void setProductionParam(float mixingWater, float mixingFeed, float bacteria,
            short[] fermentBarrelWeight);

    /**
     * 时间校准 一天两次，上午6:00及下午6:00
     */
    //void setTimeCalibration();

    /**
     * 设置更新标志
     */
    void setProductionUpdateFlag();

    /**
     * 获取更新标志
     *
     * @return
     */
    short getProductionUpdateFlag();

    /**
     * 获取搅拌桶状态 0：空闲 1：工作
     *
     * @return
     */
    short getMixingBarrelStatus();

    /**
     * 获取7个发酵罐的状态 
     * @return 
     * boolean数组，可能返回的数组长度为8
     */
    boolean[] getFermentBarrelStatus();


    /**
     * 获取发酵桶当前PH值
     *
     * @return
     */
    float getPhValue();

    /**
     * 获取料塔低位和空位预警信号
     * 
     * @return 
     * boolean数组，可能返回的数组长度为8
     * data[0]: 料位低警报 
     * data[1]: 料位空警报
     */
    boolean[] getMaterialTowerStatus();

    /**
     * 获取15个阀门累计动作次数和泵工作时间
     *
     * @return
     */
    int[] getValveAndPumpCondition();

    /**
     * 发送栏位状态
     *
     * @param status
     */
    void setStyStatus(boolean[] status);

    

    /**
     * 设置更新标志
     */
    void setStyStatusUpdateFlag();

    /**
     * 获取更新标志
     * @return 
     */
    short getStyStatusUpdateFlag();
    
    /**
     * 获取流量数据
     * @return 
     */
    float[] getFlowValues();
    
    /**
     * 获取发酵罐工作参数
     * 预备或正在进料的发酵罐号
     * 正在出料的发酵罐号
     * @return 
     */
    short[] getFermentBarrelWorkingParam();
    
    /**
     * 首次做料发酵时间12小时倒计时
     * @return 
     */
    short getFermentCountDown();
}
