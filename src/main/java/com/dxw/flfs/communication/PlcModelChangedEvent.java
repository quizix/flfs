package com.dxw.flfs.communication;

/**
 * Created by zhang on 2016-04-19.
 */
public class PlcModelChangedEvent {

    public PlcModelChangedEvent(long when, long field, PlcModel model){
        this.when = when;
        this.field = field;
        this.model = model;
    }

    public long getWhen() {
        return when;
    }

    private long when;

    private PlcModel model;
    public long getField() {
        return field;
    }

    private long field;

    public PlcModel getModel() {
        return model;
    }



}
