package com.dxw.common.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 批次管理
 * Created by zhang on 2016-04-18.
 */
@Entity
@Table(name="flfs_batch")
@Access(AccessType.FIELD)
public class Batch{
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
     * 编码
     */
    @Column(name="code")
    private String code;

    /**
     * 入栏时间
     */
    @Column(name="inStockDate")
    private Date inStockDate;

    /**
     * 入栏时间：先简化模型，每天入栏数量一样
     */
    @Column(name="inStockDuration")
    private int inStockDuration;

    /**
     * 每天入栏数量
     */
    @Column(name="inStockNumber")
    private int inStockNumber;

    /**
     * 本批次所对应的栏位
     */
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="flfs_batch_sty",
            joinColumns=@JoinColumn(name="batchId"), inverseJoinColumns=@JoinColumn(name="styId"))
    private Set<Sty> sties;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public Set<Sty> getSties() {
        return sties;
    }

    public void setSties(Set<Sty> sties) {
        this.sties = sties;
    }

    public int getInStockNumber() {
        return inStockNumber;
    }

    public void setInStockNumber(int inStockNumber) {
        this.inStockNumber = inStockNumber;
    }

    public int getInStockDuration() {
        return inStockDuration;
    }

    public void setInStockDuration(int inStockDuration) {
        this.inStockDuration = inStockDuration;
    }

    public Date getInStockDate() {
        return inStockDate;
    }

    public void setInStockDate(Date inStockDate) {
        this.inStockDate = inStockDate;
    }

}
