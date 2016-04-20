/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.scheduling;

/**
 *
 * @author pronics3
 */
public interface FlfsScheduler {
    /**
     * 每天上午6点进行排产，计算做料参数
     * @return 排产结果
     */
    ScheduleResult schedule6AM() throws SchedulerException;
    /**
     * 每天下午6点进行排产，计算做料参数
     * @return 排产结果
     */
    ScheduleResult schedule6PM() throws SchedulerException;
}
