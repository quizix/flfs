/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.models;

import java.util.Date;

/**
 * 小猪入栏计划
 *
 * @author pronics3
 */
public class InStyPlan extends DbModel {

    /**
     * 计划入栏日期
     */
    private Date plannedDate;
    /**
     * 计划栏位
     */
    private Sty sty;

    /**
     * 计划入栏数量
     */
    private int plannedNumber;

    public Date getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(Date plannedDate) {
        this.plannedDate = plannedDate;
    }

    public Sty getSty() {
        return sty;
    }

    public void setSty(Sty sty) {
        this.sty = sty;
    }

    public int getPlannedNumber() {
        return plannedNumber;
    }

    public void setPlannedNumber(int plannedNumber) {
        this.plannedNumber = plannedNumber;
    }

    @Override
    public String toString() {
        return String.format("{id:%d, createTime:%s, modifyTime:%s}",
                this.id, this.createTime, this.modifyTime);
    }
}
