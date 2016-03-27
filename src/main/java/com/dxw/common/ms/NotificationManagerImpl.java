/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.ms;

import com.dxw.common.services.ServiceException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author pronics3
 */
public class NotificationManagerImpl implements NotificationManager {

    Collection<Receiver> receivers = new ArrayList<>();

    //private static Object syn = new Object();
    @Override
    public void notify(String tag, Notification notification) {
        synchronized(this)
        {
            receivers.stream().forEach((receiver) -> {
                receiver.notify(tag, notification);
            });
        }
    }

    @Override
    public void addReceiver(Receiver receiver) {
        if (!receivers.contains(receiver)) {
            receivers.add(receiver);
        }
    }

    @Override
    public void removeReceiver(Receiver receiver) {

        if (receivers.contains(receiver)) {
            receivers.remove(receiver);
        }
    }

    @Override
    public String getName() {
        return "notification_manager";
    }

    @Override
    public void init() throws ServiceException {

    }

    @Override
    public void destroy() throws ServiceException {
        receivers.clear();
    }

}
