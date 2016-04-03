/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data;

import com.dxw.common.models.InStyPlan;
import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import com.dxw.common.models.User;

import java.util.Date;
import java.util.List;

public interface FlfsDao extends AutoCloseable {
    ///////////////////////////////////
    ////reads
    Shed getShedByCode(String code);

    Sty getStyByCode(String code);

    List getSheds();
    
    List getStiesByShed(String code);
    
    long getTotalPigInShed(String code);

    long getTotalPigPlanInShed(String code, Date date);

    User getUserByName(String name);

    List getUsers();
    
    InStyPlan getPlan(Sty sty, Date date);

    List getProductionInstructions(String code, Date start, Date end);

    /////////////////////////////////////
    ////add/update 
    <T> void update(T t);
    
    <T> void delete(T t);

}
