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
public interface PlcBase {
    public boolean getCoil(int offset);
    
    public void setCoil(int offset, boolean value);
    
    public boolean getDiscreteInput(int offset);
    
    public short getRegisterShort(int offset, int registerType);
    
    public float getRegisterFloat(int offset, int registerType);
    
    public int getRegisterInt(int offset, int registerType);
    
    public void setRegister(int offset, int value);
    
    public void setRegister(int offset, short value);
    
    public void setRegister(int offset, float value);
}
