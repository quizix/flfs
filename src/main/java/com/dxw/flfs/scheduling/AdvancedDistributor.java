package com.dxw.flfs.scheduling;

import static com.dxw.flfs.scheduling.SchedulerParams.FERMENT_BARREL_NUMBER;

/**
 * Created by zhang on 2016-04-20.
 */
public class AdvancedDistributor implements Distributor {
    /**
     * 根据总共所需要做的饲料量，将其分布到6个搅拌桶
     *
     * @param total
     * @return
     */
    @Override
    public ScheduleResult distribute(float total) {
        //分布到6个发酵罐罐子
        int fermentBarrelNumber = FERMENT_BARREL_NUMBER;
        if (total < SchedulerParams.MIXING_BARREL_CAPACITY * fermentBarrelNumber) {
            //起始阶段：量小于搅拌桶容量*6
            float each = total / fermentBarrelNumber;
            if (each < SchedulerParams.MIXING_BARREL_MIN_WEIGHT) {
                //如果小于 搅拌桶最小容量（140*6),那么不需要全部都做
                int count = (int)Math.floor(total/SchedulerParams.MIXING_BARREL_MIN_WEIGHT);

                short[] barrels = new short[fermentBarrelNumber];
                for(int i=0;i<count;i++)
                    barrels[i] = 1;
                float dry = SchedulerParams.MIXING_BARREL_MIN_WEIGHT / (1 + SchedulerParams.WATER_MATERIAL_RATIO);
                float water = SchedulerParams.MIXING_BARREL_MIN_WEIGHT - dry;
                float bacteria = each * SchedulerParams.BACTERIA_RATIO;

                return new ScheduleResult(dry, water, bacteria, barrels);

            } else {
                float dry = each / (1 + SchedulerParams.WATER_MATERIAL_RATIO);
                float water = each - dry;
                float bacteria = each * SchedulerParams.BACTERIA_RATIO;
                short[] barrels = new short[fermentBarrelNumber];
                for (int i = 0; i < barrels.length; i++)
                    barrels[i] = 1;

                return new ScheduleResult(dry, water, bacteria, barrels);
            }
        } else {

            //计算一共需要做多少个搅拌桶
            int count = Math.round(
                    total / SchedulerParams.MIXING_BARREL_CAPACITY);

            float dry = SchedulerParams.MIXING_BARREL_CAPACITY / (1 + SchedulerParams.WATER_MATERIAL_RATIO);
            float water = SchedulerParams.MIXING_BARREL_CAPACITY - dry;
            float bacteria = SchedulerParams.MIXING_BARREL_CAPACITY * SchedulerParams.BACTERIA_RATIO;

            if (count > SchedulerParams.FERMENT_MIXING_RATIO * fermentBarrelNumber)
                count = SchedulerParams.FERMENT_MIXING_RATIO * fermentBarrelNumber;

            int base = count / fermentBarrelNumber;

            short[] barrels = new short[fermentBarrelNumber];
            for (int i = 0; i < barrels.length; i++)
                barrels[i] = (short) base;

            for (int i = 0; i < count % fermentBarrelNumber; i++)
                barrels[i] += 1;

            return new ScheduleResult(dry, water, bacteria, barrels);
        }
    }
}
