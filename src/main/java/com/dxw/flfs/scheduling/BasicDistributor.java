package com.dxw.flfs.scheduling;

/**
 * Created by zhang on 2016-04-20.
 */
public class BasicDistributor implements Distributor {
    /**
     * 根据总共所需要做的饲料量，将其分布到6个搅拌桶
     * @param total
     * @return
     */
    @Override
    public  ScheduleResult distribute(float total) {
        //分布到6个发酵罐
        int fermentBarrelNumber = SchedulerParams.FERMENT_BARREL_NUMBER;
        if (total < SchedulerParams.MIXING_BARREL_CAPACITY * fermentBarrelNumber) {
            //起始阶段：量小于搅拌桶容量*6
            float each = total / fermentBarrelNumber;
            if (each < SchedulerParams.MIXING_BARREL_MIN_WEIGHT)
                each = SchedulerParams.MIXING_BARREL_MIN_WEIGHT;

            float dry = each / (1 + SchedulerParams.WATER_MATERIAL_RATIO);
            float water = each - dry;
            float bacteria = each * SchedulerParams.BACTERIA_RATIO;
            short[] barrels = new short[fermentBarrelNumber];
            for (int i = 0; i < barrels.length; i++)
                barrels[i] = 1;

            return new ScheduleResult(dry, water, bacteria, barrels);
        } else {
            float each = total / fermentBarrelNumber;

            float rough = each / SchedulerParams.MIXING_BARREL_CAPACITY;

            int base = Math.round(rough);

            if (base > SchedulerParams.FERMENT_MIXING_RATIO)
                base = SchedulerParams.FERMENT_MIXING_RATIO;

            short[] barrels = new short[fermentBarrelNumber];
            for (int i = 0; i < barrels.length; i++)
                barrels[i] = (short) base;

            float dry = SchedulerParams.MIXING_BARREL_CAPACITY / (1 + SchedulerParams.WATER_MATERIAL_RATIO);
            float water = SchedulerParams.MIXING_BARREL_CAPACITY - dry;
            float bacteria = SchedulerParams.MIXING_BARREL_CAPACITY * SchedulerParams.BACTERIA_RATIO;

            return new ScheduleResult(dry, water, bacteria, barrels);

        }
    }
}
