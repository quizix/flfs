/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication;

import com.dxw.flfs.communication.base.Plc;
import com.dxw.flfs.communication.base.PlcConfig;
import com.dxw.flfs.communication.base.PlcFactory;

/**
 *
 * @author Administrator
 */
public class PlcDelegateFactory {

    private static PlcDelegate proxy;

    public static PlcDelegate getPlcDelegate(){
        if(proxy ==null){
            Plc plcPrimary = PlcFactory.getPlc(PlcConfig.PRIMARY);
            Plc plcSecondary = PlcFactory.getPlc(PlcConfig.SECONDARY);

            proxy = new PlcDelegateImpl(plcPrimary, plcSecondary);
        }
        return proxy;
    }

}
