package com.dxw.flfs.scheduling;

/**
 * Created by zhang on 2016-04-18.
 */
public class SchedulerParams {
    /**
     * 搅拌桶的容量
     */
    public final static int MIXING_BARREL_CAPACITY = 300;

    /**
     * 饲料密度
     */
    public static final float FEED_DENSITY = 1.0f;

    /**
     * 存栏阶段每天增长的百分比
     */
    public static final float DAILY_INCREASING_PERCENT = 0.03f;


    public static final float[] INIT_STAGE_COMSUMPTIONS = {
            0.35f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f,
    };
}
