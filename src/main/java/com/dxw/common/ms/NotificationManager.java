/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.ms;

import com.dxw.common.services.Service;

/**
 *
 * @author pronics3
 */
public interface NotificationManager extends Service {
    void notify(String tag, Notification notification);
    
    void addReceiver(Receiver receiver);
    
    void removeReceiver(Receiver receiver);
}
