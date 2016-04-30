package com.dxw.flfs.data.models;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhang on 2016/4/6.
 */
@Entity
@Table(name="flfs_site_config")
public class SiteConfig{
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
    @Column(name="siteCode")
    private String siteCode;

    /**
     * host
     */
    @Column(name="host")
    private String host;

    /**
     * 0:表示系统已经启动做料程序
     * 1：表示系统已经停止做料程序
     */
    @Column(name="status")
    @ColumnDefault("0")
    private int status;

    /**
     * 0：表示小猪入场阶段
     * 1：表示小猪入场结束
     */
    @Column(name="stage")
    @ColumnDefault("0")
    private int stage;

    /**
     * 本批次所对应的栏位
     */
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="flfs_site_sty",
            joinColumns=@JoinColumn(name="siteId"), inverseJoinColumns=@JoinColumn(name="styId"))
    @OrderBy("id")
    private Set<Sty> sties = new HashSet<>();

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name="flfs_site_plan",
            joinColumns=@JoinColumn(name="siteId"), inverseJoinColumns=@JoinColumn(name="planId"))
    @OrderBy("id")
    private Set<PigletPlan> plans = new HashSet<>();


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

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String appId) {
        this.siteCode = siteCode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<Sty> getSties() {
        return sties;
    }

    public void setSties(Set<Sty> sties) {
        this.sties = sties;
    }

    public Set<PigletPlan> getPlans() {
        return plans;
    }

    public void setPlans(Set<PigletPlan> plans) {
        this.plans = plans;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
