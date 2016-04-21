/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.tests;

import com.dxw.common.services.ServiceException;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import org.junit.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Administrator
 */
public class PlcProxyTest {

    public PlcProxyTest() {
    }

    static PlcDelegate plcProxy;
    @BeforeClass
    public static void setUpClass() {
        plcProxy = PlcDelegateFactory.getPlcProxy();
    }

    @AfterClass
    public static void tearDownClass() {
    }


    @Before
    public void setUp() throws ServiceException {
        
        System.out.println("------------------------------");
    }

    @After
    public void tearDown() throws ServiceException {

    }

    @Test
    public void testSendSysCommand() {
        System.out.println("System command:");
        plcProxy.start();
        plcProxy.halt();
        plcProxy.clean();
    }

    @Test
    public void testSetProductionParam() {
        System.out.println("set production param:");
        plcProxy.setProductionParam(1000, 1001, 1002, new short[]{200, 201, 202, 203, 204, 205});
    }

    @Test
    public void testMaterialTowerStatus() {
        System.out.println("material tower status:");
        boolean[] data = plcProxy.getMaterialTowerStatus();
        assertNotNull(data);
        for (boolean b : data) {
            System.out.println(b);
        }
    }

    @Test
    public void testFermentBarrelStatus() {
        System.out.println("7 ferment barrels status:");
        boolean[] data = plcProxy.getFermentBarrelStatus();
        assertNotNull(data);
        for (boolean b : data) {
            System.out.println(b);
        }
    }

    @Test
    public void testEmergencyStopStatus() {
        System.out.println("emergency stop:");
        boolean data = plcProxy.getEmergencyStopStatus();
        System.out.println(data);
    }

    @Test
    public void testGetPhValue() {
        System.out.println("ph value:");
        float f = plcProxy.getPhValue();
        assertTrue(f == 6.45f);
        System.out.println(f);
    }

    @Test
    public void testGetValveAndPumpCondition() {
        System.out.println("Valve and pump condition:");
        int[] value = plcProxy.getValveAndPumpCondition();

        for (int f : value) {
            System.out.println(f);
        }
    }

    @Test
    public void testSetProductionUpdateFlag() {
        System.out.println("set production update flag:");
        plcProxy.setProductionUpdateFlag();
    }

    @Test
    public void testGetProductionUpdateFlag() {
        System.out.println("get production update flag:");
        short v = plcProxy.getProductionUpdateFlag();
        System.out.println(v);
    }

    @Test
    public void testFlowValues() {
        System.out.println("flow values:");
        float[] value = plcProxy.getFlowValues();

        for (float f : value) {
            System.out.println(f);
        }
    }
    
    @Test
    public void testGetFermentBarrelAction(){
        System.out.println("get ferment barrel working param:");
        short[] value = plcProxy.getFermentBarrelAction();

        for (short f : value) {
            System.out.println(f);
        }
    }
    
    @Test
    public void testGetFermentCountDown(){
        System.out.println("get ferment count down:");
        short s = plcProxy.getFermentCountDown();
        System.out.println(s);
    }

    @Test
    public void testGetMixingBarrelStatus(){
        System.out.println("get mixing barrel status:");
        Short s = plcProxy.getMixingBarrelStatus();
        System.out.println(s);
    }
    
    
}
