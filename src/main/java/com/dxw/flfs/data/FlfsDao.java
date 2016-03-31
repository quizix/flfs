/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data;

import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import com.dxw.common.models.User;
import java.util.List;

public interface FlfsDao extends AutoCloseable {
    ///////////////////////////////////
    ////reads
    public Shed getShedByCode(String code);

    public Sty getStyByCode(String code);

    public List getSheds();
    
    public List getStiesByShed(String code);
    
    public long getTotalPigInShed(String code);
    
    public User getUserByName(String name);
    
    public List getUsers();
    
    //public InStyPlan getPlan(Shed shed, Date date);

    /////////////////////////////////////
    ////add/update 
    public <T> void update(T t);
    
    public <T> void delete(T t);

}
