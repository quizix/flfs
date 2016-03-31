/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.tests;

import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import com.dxw.common.services.ServiceException;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.HibernateServiceImpl;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pronics3
 */
public class DaoTest {

    static HibernateService hibernateService;

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

            Shed shed = new Shed();
            shed.setCreateTime(new Date());
            shed.setModifyTime(new Date());
            shed.setAddress("江西鄱阳");
            shed.setCode("12345678");
            shed.setName("猪舍1");
            dao.update(shed);
            
            Set<Sty> sties = new HashSet<>();
            for (int i = 0; i < 24; i++) {
                Sty sty = new Sty();
                sty.setCreateTime(new Date());
                sty.setModifyTime(new Date());
                sty.setCode(Integer.toString(i));
                sty.setName("Sty" + i);
                sty.setPigNumber(100 + i);
                sty.setShed(shed);
                sty.setNo(i);
                dao.update(sty);
            }
            shed.setSties(sties);
        }

        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            System.out.println(dao.getTotalPigInShed("12345678"));
        }
        
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            Shed shed = dao.getShedByCode("12345678");
            assertTrue(shed.getName().equals("猪舍1"));
        }
        
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            List sties = dao.getStiesByShed("12345678");
            
            sties.stream().forEach((o) -> {
                Sty s = (Sty)o;
                System.out.println(s.getName());
            });
        }
    }
}
