/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.models;

import javax.persistence.*;
import java.util.Date;

/**
 * 工况信息
 * 包括15个阀门和3个泵的累计时间
 * @author zhang
 */
@Entity
@Table(name="flfs_working_condition")
public class WorkingCondition{
    /**
     * 内部id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    protected Long id;

    /**
     * 创建时间
     */
    @Column(name="createTime")
    protected Date createTime;

    /**
     * 修改时间
     */
    @Column(name="modifyTime")
    protected Date modifyTime;

    private int value1;
    private int value2;
    private int value3;
    private int value4;
    private int value5;
    private int value6;
    private int value7;
    private int value8;
    private int value9;
    private int value10;
    private int value11;
    private int value12;
    private int value13;
    private int value14;
    private int value15;
    private int pump1;
    private int pump2;
    private int pump3;
    private int pump4;

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public int getValue4() {
        return value4;
    }

    public void setValue4(int value4) {
        this.value4 = value4;
    }

    public int getValue5() {
        return value5;
    }

    public void setValue5(int value5) {
        this.value5 = value5;
    }

    public int getValue6() {
        return value6;
    }

    public void setValue6(int value6) {
        this.value6 = value6;
    }

    public int getValue7() {
        return value7;
    }

    public void setValue7(int value7) {
        this.value7 = value7;
    }

    public int getValue8() {
        return value8;
    }

    public void setValue8(int value8) {
        this.value8 = value8;
    }

    public int getValue9() {
        return value9;
    }

    public void setValue9(int value9) {
        this.value9 = value9;
    }

    public int getValue10() {
        return value10;
    }

    public void setValue10(int value10) {
        this.value10 = value10;
    }

    public int getValue11() {
        return value11;
    }

    public void setValue11(int value11) {
        this.value11 = value11;
    }

    public int getValue12() {
        return value12;
    }

    public void setValue12(int value12) {
        this.value12 = value12;
    }

    public int getValue13() {
        return value13;
    }

    public void setValue13(int value13) {
        this.value13 = value13;
    }

    public int getValue14() {
        return value14;
    }

    public void setValue14(int value14) {
        this.value14 = value14;
    }

    public int getValue15() {
        return value15;
    }

    public void setValue15(int value15) {
        this.value15 = value15;
    }

    public int getPump1() {
        return pump1;
    }

    public void setPump1(int pump1) {
        this.pump1 = pump1;
    }

    public int getPump2() {
        return pump2;
    }

    public void setPump2(int pump2) {
        this.pump2 = pump2;
    }

    public int getPump3() {
        return pump3;
    }

    public void setPump3(int pump3) {
        this.pump3 = pump3;
    }

    public int getPump4() {
        return pump4;
    }

    public void setPump4(int pump4) {
        this.pump4 = pump4;
    }
    
}
