/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.scheduling;

import java.util.Arrays;

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

    ScheduleResult(float dry, float water, float bacteria, short[] barrels){
        this.water = water;
        this.dry = dry;
        this.bacteria = bacteria;
        this.barrels = barrels;
    }

    public float getWater() {
        return water;
    }

    public float getDry() {
        return dry;
    }

    public float getBacteria() {
        return bacteria;
    }

    public short[] getBarrels() {
        return barrels;
    }

    @Override
    public String toString(){
        String s = "dry:"+dry + " water:"+water + " bacteria: "+bacteria
                + " each barrels: " + Arrays.toString(barrels);
        return s;
    }

}
