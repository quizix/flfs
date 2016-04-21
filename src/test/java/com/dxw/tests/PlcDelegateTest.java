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
public class PlcDelegateTest {

    public PlcDelegateTest() {
    }

    static PlcDelegate delegate;
    @BeforeClass
    public static void setUpClass() {
        delegate = PlcDelegateFactory.getPlcDelegate();
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
        delegate.start();
        delegate.halt();
        delegate.clean();
    }

    @Test
    public void testSetProductionParam() {
        System.out.println("set production param:");
        delegate.setProductionParam(1000, 1001, 1002, new short[]{200, 201, 202, 203, 204, 205});
    }

    @Test
    public void testMaterialTowerStatus() {
        System.out.println("material tower status:");
        boolean[] data = delegate.getMaterialTowerStatus();
        assertNotNull(data);
        for (boolean b : data) {
            System.out.println(b);
        }
    }

    @Test
    public void testFermentBarrelStatus() {
        System.out.println("7 ferment barrels status:");
        boolean[] data = delegate.getFermentBarrelStatus();
        assertNotNull(data);
        for (boolean b : data) {
            System.out.println(b);
        }
    }

    @Test
    public void testEmergencyStopStatus() {
        System.out.println("emergency stop:");
        boolean data = delegate.getEmergencyStopStatus();
        System.out.println(data);
    }

    @Test
    public void testGetPhValue() {
        System.out.println("ph value:");
        float f = delegate.getPhValue();
        assertTrue(f == 6.45f);
        System.out.println(f);
    }

    @Test
    public void testGetValveAndPumpCondition() {
        System.out.println("Valve and pump condition:");
        int[] value = delegate.getValveAndPumpCondition();

        for (int f : value) {
            System.out.println(f);
        }
    }

    @Test
    public void testSetProductionUpdateFlag() {
        System.out.println("set production update flag:");
        delegate.setProductionUpdateFlag();
    }

    @Test
    public void testGetProductionUpdateFlag() {
        System.out.println("get production update flag:");
        short v = delegate.getProductionUpdateFlag();
        System.out.println(v);
    }

    @Test
    public void testFlowValues() {
        System.out.println("flow values:");
        float[] value = delegate.getFlowValues();

        for (float f : value) {
            System.out.println(f);
        }
    }
    
    @Test
    public void testGetFermentBarrelAction(){
        System.out.println("get ferment barrel working param:");
        short[] value = delegate.getFermentBarrelAction();

        for (short f : value) {
            System.out.println(f);
        }
    }
    
    @Test
    public void testGetFermentCountDown(){
        System.out.println("get ferment count down:");
        short s = delegate.getFermentCountDown();
        System.out.println(s);
    }

    @Test
    public void testGetMixingBarrelStatus(){
        System.out.println("get mixing barrel status:");
        Short s = delegate.getMixingBarrelStatus();
        System.out.println(s);
    }

    @Test
    public void testSetStyStatus(){
        System.out.println("set sty status:");
        short[] status = new short[24];
        delegate.setStyStatus(status);
    }
}
