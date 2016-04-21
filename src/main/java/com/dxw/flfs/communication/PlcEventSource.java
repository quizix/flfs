package com.dxw.flfs.communication;

/**
 * Created by zhang on 2016-04-20.
 */
public interface PlcEventSource {
    void fireModelChanged(PlcModelChangedEvent event);

    void addModelChangedListener(PlcModelChangedListener l);

    void removeModelChangedListener(PlcModelChangedListener l);

}
