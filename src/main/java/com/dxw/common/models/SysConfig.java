package com.dxw.common.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhang on 2016/4/6.
 */
@Entity
@Table(name="flfs_sys_config")
public class SysConfig{
    /**
     * 内部id
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    protected Long id;

    /**
     * 创建时间
     */
    @Column(name="createTime")
    protected Date createTime;

    /**
     * 修改时间
     */
    @Column(name="modifyTime")
    protected Date modifyTime;

    /**
     * app Id
     */
    @Column(name="appId")
    private String appId;

    /**
     * host
     */
    @Column(name="host")
    private String host;

    /**
     * batchCode
     */
    @Column(name="batchCode")
    private String batchCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
