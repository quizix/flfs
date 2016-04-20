/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.scheduling;

/**
 *
 * @author pronics3
 */
public class SchedulerException extends Exception {
    
    public SchedulerException(String message){
        super(message);
    }

    public SchedulerException(Exception innerException){
        super(innerException);
    }
}
