package com.dxw.tests;

import com.dxw.flfs.scheduling.AdvancedDistributor;
import com.dxw.flfs.scheduling.BasicDistributor;
import com.dxw.flfs.scheduling.ScheduleResult;
import org.junit.Test;

/**
 * Created by zhang on 2016-04-20.
 */
public class DistributorTest {
    @Test
    public void testBasicDistributor(){
        for(int i=1000;i<50000;i+=1000){

            ScheduleResult result = new BasicDistributor().distribute(i);

            System.out.println("for :" + i + " " +result);
        }
    }

    @Test
    public void testAdvancedDistributor(){
        for(int i=1000;i<50000;i+=1000){

            ScheduleResult result = new AdvancedDistributor().distribute(i);

            System.out.println("for :" + i + " " +result);
        }
    }
}
