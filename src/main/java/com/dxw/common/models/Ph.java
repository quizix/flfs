package com.dxw.common.models;

/**
 * PH值
 *
 * @author pronics3
 *
 */
public class Ph extends DbModel {

    /**
     * 猪舍
     */
    private Shed shed;

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
    
    /**
     * ph值
     */
    private float value;
	
}
