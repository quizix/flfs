/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data;

import com.dxw.flfs.data.models.*;

import java.util.Date;
import java.util.List;

public interface FlfsDao extends AutoCloseable {
    ///////////////////////////////////
    ////reads
    Shed findShedByCode(String code);

    Sty findStyByCode(String code);

    Batch findBatchByCode(String code);

    Shed findShedById(Long id);

    Sty findStyById(Long id);

    Batch findBatchById(Long id);

    SiteConfig findSiteConfig(String siteCode);

    List findAllSheds();

    List findAllBatches();
    
    List findStiesByShed(String code);

    long findCurrentPigsByBatch(Batch batch);

    long findLastPigsByBatch(Batch batch);

    long findTotalPigPlanInShed(String code, Date date);

    User findUserByName(String name);

    List findAllUsers();
    
    InStyPlan findPlan(Sty sty, Date date);

    List findProductionInstructions(String code, Date start, Date end);

    /////////////////////////////////////
    ////add/update 
    <T> void update(T t);
    
    <T> void delete(T t);

    void begin();

    void commit();

}
