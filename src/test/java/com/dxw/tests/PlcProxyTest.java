/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.tests;

import com.dxw.common.ms.NotificationManager;
import com.dxw.common.ms.NotificationManagerImpl;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyImpl;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Administrator
 */

public class PlcProxyTest {
    
    public PlcProxyTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    PlcProxy plcProxy;
    NotificationManager notificationManager;
            
    @Before
    public void setUp() throws ServiceException {
        /*System.out.println("..................");
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
         notificationManager = new NotificationManagerImpl();
        notificationManager.init();
        registry.register(notificationManager);*/
        
        plcProxy = PlcProxyImpl.getInstance();
    }
    
    @After
    public void tearDown() throws ServiceException {
        System.out.println("---------------");
        //ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        //registry.unregister(notificationManager);
    }

     @Test
     public void testSendSysCommand(){
         plcProxy.start();
         plcProxy.halt();
         plcProxy.clean();
     }
     
     @Test
     public void testMaterialTowerStatus(){
         boolean[] data = plcProxy.getMaterialTowerStatus();
         assertNotNull(data);
         for(boolean b: data)
             System.out.println(b);
     }
     
     @Test
     public void testFermentBarrelStatus(){
         boolean[] data = plcProxy.getFermentBarrelStatus();
         assertNotNull(data);
         for(boolean b: data)
             System.out.println(b);
     }
}
