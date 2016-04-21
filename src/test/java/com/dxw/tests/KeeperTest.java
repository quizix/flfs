package com.dxw.tests;

import com.dxw.flfs.app.Keeper;
import org.junit.Test;
import org.quartz.SchedulerException;

/**
 * Created by zhang on 2016-04-21.
 */
public class KeeperTest {
    @Test

    public void testJobs() throws SchedulerException {
         new Keeper()
                 .startJobs();
    }
}
