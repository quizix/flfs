/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.models;

/**
 *
 * @author Administrator
 */
public class User extends DbModel {
    /**
     * 用户名
     */
    private String name;

     /**
     * 密码
     */
    private String password;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
   
}
