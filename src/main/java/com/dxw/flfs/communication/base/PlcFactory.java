/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication.base;

/**
 *
 * @author Administrator
 */
public class PlcFactory {
    public static Plc getPlc(PlcConfig config){
        return new PlcImpl(config);
    }
}
