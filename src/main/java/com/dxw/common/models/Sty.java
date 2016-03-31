package com.dxw.common.models;

/**
 * 栏位
 *
 * @author pronics3
 *
 */
public class Sty extends DbModel{
    /**
     * 猪舍
     */
    private Shed shed;

    /**
     * 名字
     */
    private String name;

    /**
     * 编码
     */
    private String code;
    
    /**
     * 编号
     */
    private int no;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
    

    /**
     * 猪的数量，并由此推断栏位状态
     */
    private int pigNumber;

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
