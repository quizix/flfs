/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.scheduling;

/**
 * 排产结果
 *
 * @author pronics3
 */
public class ScheduleResult {
    /**
     * 加水量
     */
    private float water;
    /**
     * 干料量
     */
    private float dry;
    /**
     * 菌液量
     */
    private float bacteria;
    /**
     * 发酵罐每罐做量
     */
    private short[] barrels;

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public float getDry() {
        return dry;
    }

    public void setDry(float dry) {
        this.dry = dry;
    }

    public float getBacteria() {
        return bacteria;
    }

    public void setBacteria(float bacteria) {
        this.bacteria = bacteria;
    }

    public short[] getBarrels() {
        return barrels;
    }

    public void setBarrels(short[] barrels) {
        this.barrels = barrels;
    }


}
