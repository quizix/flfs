/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data.models;

import javax.persistence.*;
import java.util.Date;

/**
 * 小猪入栏计划
 *
 * @author pronics3
 */
@Entity
@Table(name="flfs_in_sty_plan")
public class InStyPlan{
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

    /**
     * 计划入栏日期
     */
    @Column(name="date")
    private Date date;
    /**
     * 计划栏位
     */
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name="styId")
    private Sty sty;

    /**
     * 计划入栏数量
     */
    @Column(name="value")
    private int value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Sty getSty() {
        return sty;
    }

    public void setSty(Sty sty) {
        this.sty = sty;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("{id:%d, createTime:%s, modifyTime:%s}",
                this.id, this.createTime, this.modifyTime);
    }
}
