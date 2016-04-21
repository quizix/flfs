/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.communication.base;

/**
 *
 * @author pronics3
 */
public class PlcException extends Exception {
    public PlcException(String message, Throwable cause){
        super(message, cause);
    }
}
