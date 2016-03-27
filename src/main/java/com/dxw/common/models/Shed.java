/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.models;

import java.util.Collection;

/**
 * 猪舍
 *
 * @author Administrator
 */
public class Shed extends DbModel{

    /**
     * 名字
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 地址
     */
    private String address;

    
    /**
     * 栏位
     */
    private Collection<Sty> sties;

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

    public Collection<Sty> getSties() {
        return sties;
    }

    public void setSties(Collection<Sty> sties) {
        this.sties = sties;
    }
    
    
}
