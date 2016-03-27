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
    private float mixingWater;
    /**
     * 干料量
     */
    private float mixingFeed;
    /**
     * 菌液量
     */
    private float bacteria;
    /**
     * 发酵罐每罐做量
     */
    private float[] fermentBarrelWeight;

    public float getMixingWater() {
        return mixingWater;
    }

    public void setMixingWater(float mixingWater) {
        this.mixingWater = mixingWater;
    }

    public float getMixingFeed() {
        return mixingFeed;
    }

    public void setMixingFeed(float mixingFeed) {
        this.mixingFeed = mixingFeed;
    }

    public float getBacteria() {
        return bacteria;
    }

    public void setBacteria(float bacteria) {
        this.bacteria = bacteria;
    }

    public float[] getFermentBarrelWeight() {
        return fermentBarrelWeight;
    }

    public void setFermentBarrelWeight(float[] fermentBarrelWeight) {
        this.fermentBarrelWeight = fermentBarrelWeight;
    }

}
