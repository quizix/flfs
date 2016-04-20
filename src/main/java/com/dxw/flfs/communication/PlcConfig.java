/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication;

import com.serotonin.modbus4j.base.ModbusUtils;

/**
 *
 * @author Administrator
 */
public class PlcConfig {

    private String ip;
    private int port;
    int slaveId;

    private PlcConfig(String ip, int port, int slaveId) {
        this.ip = ip;
        this.port = port;
        this.slaveId = slaveId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(int slaveId) {
        this.slaveId = slaveId;
    }

    public final static PlcConfig PRIMARY = new PlcConfig("127.0.0.1", ModbusUtils.TCP_PORT, 1);
    public final static PlcConfig SECONDARY = new PlcConfig("127.0.0.1", ModbusUtils.TCP_PORT, 2);

    public static PlcConfig ACTIVE = PRIMARY;
}
