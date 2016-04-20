package com.dxw.common.models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhang on 2016-04-19.
 */
@Entity
@Table(name="flfs_sty_operation")
public class StyOperation {
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
     * 猪舍
     */
    @ManyToOne
    @JoinColumn(name="styId")
    private Sty sty;

    @Column(name="delta")
    protected int delta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sty getSty() {
        return sty;
    }

    public void setSty(Sty sty) {
        this.sty = sty;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }
}
