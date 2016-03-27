/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.ms;

/**
 *
 * @author pronics3
 */
public interface Receiver {
    void notify(String tag, Notification notification);
}
