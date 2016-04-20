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

    private static PlcProxy proxy;

    public static PlcProxy getPlcProxy(){
        if(proxy ==null){
            Plc plcPrimary = PlcFactory.getPlc(PlcConfig.PRIMARY);
            Plc plcSecondary = PlcFactory.getPlc(PlcConfig.SECONDARY);

            proxy = new PlcProxyImpl(plcPrimary, plcSecondary);
        }
        return proxy;
    }

}
