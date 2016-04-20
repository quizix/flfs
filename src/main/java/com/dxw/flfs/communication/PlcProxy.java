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
public interface PlcProxy extends PlcProxyEventSource{

    //region 做料PLC

    //region Discrete input
    /**
     * 获取紧急停止状态
     * @return
     */
    Boolean getEmergencyStopStatus();

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
     * 获取7个发酵罐的状态
     * @return
     * boolean数组，可能返回的数组长度为8
     */
    boolean[] getFermentBarrelStatus();

    //endregion

    //region Holding registers
    /**
     * 获取搅拌桶状态 0：空闲 1：工作
     *
     * @return
     */
    Short getMixingBarrelStatus();

    /**
     * 获取系统状态(1停机2做料3清洗4紧停5冷启动)
     * @return
     */
    Short getSystemStatus();

    /**
     * 首次做料发酵时间12小时倒计时
     * @return
     */
    Short getFermentCountDown();

    /**
     * 获取发酵罐工作参数
     * 预备或正在进料的发酵罐号
     * 正在出料的发酵罐号
     * @return
     */
    short[] getFermentBarrelAction();

    /**
     * 发送做料参数，单位kg 一天两次，上午6:00及下午6:00
     *
     * @param water 加水量
     * @param dry 干料量
     * @param bacteria 菌液量
     * @param fermentBarrelWeight 发酵罐每罐做量
     */
    void setProductionParam(float water, float dry, float bacteria,
                            short[] fermentBarrelWeight);

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
    Short getProductionUpdateFlag();

    /**
     * PLC数据更新反馈标志（2秒正脉冲）
     * @return
     */
    Short getDataFeedbackFlag1();
    /**
     * 获取流量数据
     * @return
     */
    float[] getFlowValues();

    /**
     * 获取15个阀门累计动作次数和泵工作时间
     *
     * @return
     */
    int[] getValveAndPumpCondition();
    //endregion

    //region Input registers

    /**
     * 获取发酵桶当前PH值
     *
     * @return
     */
    float getPhValue();
    //endregion

    //endregion

    //region 送料PLC

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
    Short getStyStatusUpdateFlag();

    /**
     * PLC数据更新反馈标志（2秒正脉冲）
     * @return
     */
    Short getDataFeedbackFlag2();

    /**
     * 喂料泵工作时间累计
     * @return
     */
    Integer getPumpCondition();
    //endregion
}
