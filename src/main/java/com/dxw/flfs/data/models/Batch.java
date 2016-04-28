package com.dxw.flfs.data.models;

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
     * 入栏开始时间
     */
    @Column(name="startDate")
    private Date startDate;

    /**
     * 入栏结束时间
     */
    @Column(name="endDate")
    private Date endDate;

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
    @OrderBy("id")
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


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
