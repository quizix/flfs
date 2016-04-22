/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.tests;

import com.dxw.common.models.Batch;
import com.dxw.common.models.InStyPlan;
import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import com.dxw.common.services.ServiceException;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.HibernateServiceImpl;
import org.junit.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 *
 * @author pronics3
 */
public class DaoTest {

    private static HibernateService hibernateService;

    @BeforeClass
    public static void setUpClass() throws ServiceException {
        hibernateService = new HibernateServiceImpl();
        hibernateService.init();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws ServiceException {

    }

    @After
    public void tearDown() throws ServiceException {

    }

    @Test
    public void first() throws Exception {
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {

            dao.begin();

            Shed shed = new Shed();
            shed.setCreateTime(new Date());
            shed.setModifyTime(new Date());
            shed.setAddress("江西鄱阳");
            shed.setCode("12345678");
            shed.setName("猪舍1");
            shed.setActive(true);

            dao.update(shed);
            
            Set<Sty> sties = new HashSet<>();
            for (int i = 0; i < 24; i++) {
                Sty sty = new Sty();
                sty.setCreateTime(new Date());
                sty.setModifyTime(new Date());
                sty.setCode(Integer.toString(i));
                sty.setName("Sty" + i);
                sty.setLastNumber(80 + i);
                sty.setCurrentNumber(100 + i);
                sty.setShed(shed);
                sty.setNo(i);
                sties.add(sty);
                //dao.update(sty);

            }

            Batch batch = new Batch();

            batch.setCreateTime(new Date());
            batch.setCode("1");
            batch.setModifyTime(new Date());
            batch.setInStockDuration(100);
            batch.setStartDate(new Date());
            batch.setInStockNumber(100);
            batch.setSties(sties);

            dao.update(batch);
            dao.commit();


            //shed.setSties(sties);
        }

        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            System.out.println(dao.findCurrentPigsByBatch( dao.findBatchByCode("1")));
        }
        
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            Shed shed = dao.findShedByCode("12345678");
            assertTrue(shed.getName().equals("猪舍1"));
        }
        
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            List sties = dao.findStiesByShed("12345678");
            
            sties.stream().forEach((o) -> {
                Sty s = (Sty)o;
                System.out.println(s.getName());
            });
        }

        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {

            Sty sty = dao.findStyByCode("0");

            InStyPlan plan = new InStyPlan();
            plan.setSty(sty);

            plan.setCreateTime( new Date());
            plan.setModifyTime( new Date());
            Date current = new Date();

            plan.setDate( new Date(current.getYear(), current.getMonth(), current.getDate()));
            plan.setValue(1000);

            dao.update(plan);

            InStyPlan planQuery = dao.findPlan(sty, new Date(current.getYear(), current.getMonth(), current.getDate()));
            System.out.println(planQuery.getValue());
        }

        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            Date current = new Date();
            long number = dao.findTotalPigPlanInShed("12345678" , new Date(current.getYear(), current.getMonth(), current.getDate()));
            System.out.println(number);
        }
    }
}
