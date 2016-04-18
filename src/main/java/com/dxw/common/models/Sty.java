package com.dxw.common.models;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 栏位
 *
 * @author pronics3
 *
 */
@Entity
@Table(name="flfs_sty")
public class Sty{
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
    private Shed shed;

    /**
     * 名字
     */
    @Column(name="name")
    private String name;

    /**
     * 编码
     */
    @Column(name="code")
    private String code;
    
    /**
     * 编号
     */
    @Column(name="no")
    private int no;

    /**
     * 猪的数量，并由此推断栏位状态
     */
    @Column(name="pigNumber")
    private int pigNumber;

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

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Shed getShed() {
        return shed;
    }

    public void setShed(Shed shed) {
        this.shed = shed;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPigNumber() {
        return pigNumber;
    }

    public void setPigNumber(int pigNumber) {
        this.pigNumber = pigNumber;
    }
}
