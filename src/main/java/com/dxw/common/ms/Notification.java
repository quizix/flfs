/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.ms;

/**
 *
 * @author pronics3
 */
public class Notification {
    private int id;
    
    private Object content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
    
    /**
     * 什么时候
     */
    private long when;

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }
    
    
}
