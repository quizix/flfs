/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication.base;

import com.dxw.flfs.communication.RegisterType;

/**
 * 对Modbus的读写寄存器操作进行封装
 *
 * @author Administrator
 */
public interface Plc {

    public boolean getCoil(int offset) throws PlcException;

    public boolean[] getCoils(int offset, int count) throws PlcException;

    public void setCoil(int offset, boolean value) throws PlcException;

    public void setCoils(int offset, boolean[] value) throws PlcException;

    public boolean getDiscreteInput(int offset) throws PlcException;

    public boolean[] getDiscreteInputs(int offset, int count) throws PlcException;

    public short getRegisterShort(int offset, RegisterType type) throws PlcException;

    public int getRegisterInt(int offset, RegisterType type) throws PlcException;

    public float getRegisterFloat(int offset, RegisterType type) throws PlcException;

    public short[] getRegistersShort(int offset, RegisterType type, int count) throws PlcException;

    public int[] getRegistersInt(int offset, RegisterType type, int count) throws PlcException;

    public float[] getRegistersFloat(int offset, RegisterType type, int count) throws PlcException;

    public void setRegister(int offset, short value) throws PlcException;

    public void setRegister(int offset, int value) throws PlcException;

    public void setRegister(int offset, float value) throws PlcException;

    public void setRegisters(int offset, short[] value) throws PlcException;

    public void setRegisters(int offset, int[] value) throws PlcException;

    public void setRegisters(int offset, float[] value) throws PlcException;

}
