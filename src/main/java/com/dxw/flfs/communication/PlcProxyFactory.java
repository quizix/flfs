/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication;

/**
 *
 * @author Administrator
 */
public class PlcProxyFactory {
    
    private static PlcProxy primaryProxy;
    private static PlcProxy secondaryProxy;
    public static PlcProxy getPrimaryPlcProxy(){
        if(primaryProxy ==null){
            Plc plc = PlcFactory.getPlc(PlcConfig.PRIMARY);
            primaryProxy = new PlcProxyImpl(plc);
        }
        return primaryProxy;
    }
    
    
    public static PlcProxy getSecondaryPlcProxy(){
        if(secondaryProxy ==null){
            Plc plc = PlcFactory.getPlc(PlcConfig.SECONDARY);
            secondaryProxy = new PlcProxyImpl(plc);
        }
        return secondaryProxy;
    }
}
