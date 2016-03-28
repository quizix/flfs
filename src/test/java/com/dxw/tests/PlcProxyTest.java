/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.tests;

import com.dxw.common.ms.NotificationManager;
import com.dxw.common.services.ServiceException;
import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyImpl;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
        plcProxy = PlcProxyImpl.getInstance();
        System.out.println("------------------------------");
    }
    
    @After
    public void tearDown() throws ServiceException {
        
    }

     @Test
     public void testSendSysCommand(){
         System.out.println("System command:");
         plcProxy.start();
         plcProxy.halt();
         plcProxy.clean();
     }
     
     @Test
     public void testSetProductionParam(){
         System.out.println("set production param:");
         plcProxy.setProductionParam(1000, 1001, 1002, new short[] {200,201,202,203,204,205,206});
     }
     
     @Test
     public void testMaterialTowerStatus(){
         System.out.println("material tower status:");
         boolean[] data = plcProxy.getMaterialTowerStatus();
         assertNotNull(data);
         for(boolean b: data)
             System.out.println(b);
     }
     
     @Test
     public void testFermentBarrelStatus(){
         System.out.println("7 ferment barrels status:");
         boolean[] data = plcProxy.getFermentBarrelStatus();
         assertNotNull(data);
         for(boolean b: data)
             System.out.println(b);
     }
     
     @Test
     public void testEmergencyStopStatus(){
         System.out.println("emergency stop:");
         boolean data = plcProxy.getEmergenyStopStatus();
         System.out.println(data);
     }
     
     @Test
     public void testGetPhValue(){
         System.out.println("ph value:");
         float f = plcProxy.getPhValue();
         assertTrue(f==6.3f);
         System.out.println(f);
     }
     
     @Test
     public void testGetValveActionAcount(){
         System.out.println("Valve action count:");
         float[] fs = plcProxy.getValveActionCount();
         
         for(float f: fs){
             System.out.println(f);
         }
     }
     @Test
     public void testSetProductionUpdateFlag(){
         System.out.println("set production update flag:");
         plcProxy.setProductionUpdateFlag();
     }
     
     @Test
     public void testGetProductionUpdateFlag(){
         System.out.println("get production update flag:");
         short v = plcProxy.getProductionUpdateFlag();
         System.out.println(v);
     }
}
