package com.dxw.flfs.scheduling;

import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyFactory;

/**
 * Created by zhang on 2016/4/3.
 */
public class FlfsSchedulerImpl implements FlfsScheduler {

    /**
     * 搅拌桶的容量
     */
    private final static int MIXING_BARREL_CAPACITY = 300;

    /**
     * 饲料密度
     */
    private final float FEED_DENSITY = 1.0f;

    /**
     * 存栏阶段每天增长的百分比
     */
    private final float INCREASING_PERCENT = 0.03f;
    private int day = 0;

    @Override
    public ScheduleResult schedule6AM() {
        float total = calcCurrentProduction();
        return Distributer.distribute(total);
    }

    @Override
    public ScheduleResult schedule6PM() {
        float total = calcCurrentProduction();
        day++;
        return Distributer.distribute(total);
    }

    /**
     * 第一阶段：猪入栏
     * 第二阶段：存栏
     *
     * @return
     */
    private boolean isInStage1() {
        if (day < 10)
            return false;
        else
            return true;
    }

    /**
     * 计算上一次的产量
     *
     * @return
     */
    private float calcLastProduction() {
        return MIXING_BARREL_CAPACITY * getLastMixingBarrelCount();
    }

    /**
     * 获取上一次所做的搅拌桶数
     *
     * @return
     */
    private int getLastMixingBarrelCount() {
        return 100;
    }

    /**
     * 获取上一次的总泵出量
     *
     * @return
     */
    float lastTotalPumpedVolume =0;
    private float getLastPumpedVolume() {
        PlcProxy proxy = PlcProxyFactory.getPrimaryPlcProxy();
        float[] data = proxy.getFlowValues();

        float volume = data[0] - lastTotalPumpedVolume;
        lastTotalPumpedVolume = data[0];
        return volume;
    }

    /**
     * 计算上一次的总消耗量
     *
     * @return
     */
    private float calcLastConsumed() {
        return getLastPumpedVolume() * FEED_DENSITY;
    }

    /**
     * 计算当前饲料剩余量
     *
     * @return
     */
    private float calcCurrentFeedRemaining() {
        return calcLastProduction() - calcLastConsumed();
    }

    /**
     * 计算每头猪上一次的平均消耗量
     *
     * @return
     */
    private float getLastAverageConsumption() {
        return calcLastConsumed() / getLastInSty();
    }

    /**
     * 统计上一次在猪圈里的总数
     *
     * @return
     */
    private float getLastInSty() {
        return 1000;
    }

    /**
     * 统计当前在猪圈里总数
     *
     * @return
     */
    private float getCurrentInSty() {
        return 1000;
    }

    /**
     * 计算预计每头猪所要消耗的饲料量
     *
     * @return
     */
    private float calcEstimatedAverageConsumption() {
        if (isInStage1()) {
            //在入栏阶段，先采用理论值
            return 1;
        } else {
            float c = getLastAverageConsumption();
            return c * (1 + INCREASING_PERCENT);
        }
    }

    /**
     * 计算当前要生产的
     *
     * @return
     */
    private float calcCurrentProduction() {
        return getCurrentInSty() * calcEstimatedAverageConsumption() - calcCurrentFeedRemaining();
    }
}
