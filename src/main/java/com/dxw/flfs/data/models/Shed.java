/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 猪舍
 *
 * @author Administrator
 */
@Entity
@Table(name="flfs_shed")
public class Shed{
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
     * 名字
     */
    @Column(name="name")
    private String name;

    /**
     * 编码
     */
    @Column(name="code")
    private String code;

    /**
     * 地址
     */
    @Column(name="address")
    private String address;


    /**
     * 猪舍是否属于激活状态
     */
    @Column(name="active")
    private boolean active;

    /**
     * 栏位
     */
    @OneToMany(mappedBy = "shed")
    @OrderBy("id")
    private Set<Sty> sties;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Sty> getSties() {
        return sties;
    }

    public void setSties(Set<Sty> sties) {
        this.sties = sties;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
