package com.dxw.flfs.app;

/**
 * Created by zhang on 2016-04-21.
 */
public class AppContext {

    private String appId;

    private String batchCode;

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
