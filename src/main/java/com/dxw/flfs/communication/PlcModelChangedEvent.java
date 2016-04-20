package com.dxw.flfs.communication;

/**
 * Created by zhang on 2016-04-19.
 */
public class PlcModelChangedEvent {

    public PlcModelChangedEvent(long when, long field, PlcProxyModel model){
        this.when = when;
        this.field = field;
        this.model = model;
    }

    public long getWhen() {
        return when;
    }

    private long when;

    private PlcProxyModel model;
    public long getField() {
        return field;
    }

    private long field;

    public PlcProxyModel getModel() {
        return model;
    }



}
