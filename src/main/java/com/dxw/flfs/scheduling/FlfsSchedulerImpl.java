package com.dxw.flfs.scheduling;

import com.dxw.common.models.Batch;
import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.communication.PlcProxy;
import com.dxw.flfs.communication.PlcProxyFactory;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by zhang on 2016/4/3.
 */
public class FlfsSchedulerImpl implements FlfsScheduler {

    private Batch batch;
    private HibernateService hibernateService;

    public FlfsSchedulerImpl(HibernateService hibernateService, Batch batch){
        this.hibernateService = hibernateService;
        this.batch = batch;
    }

    ScheduleResult lastScheduleResult = null;

    @Override
    public ScheduleResult schedule6AM() throws SchedulerException{
        float total = calcCurrentProduction();
        lastScheduleResult = Distributer.distribute(total);
        return lastScheduleResult;
    }

    @Override
    public ScheduleResult schedule6PM() throws SchedulerException{
        float total = calcCurrentProduction();
        lastScheduleResult = Distributer.distribute(total);
        return lastScheduleResult;
    }

    /**
     * 第一阶段：猪入栏
     * 第二阶段：存栏
     *
     * @return
     */
    private boolean isInInitStage() {
        Date date = batch.getInStockDate();

        LocalDate start = TimeUtil.fromDate(date);
        LocalDate end = start.plus(batch.getInStockDuration(), ChronoUnit.DAYS);
        LocalDate now = LocalDate.now();

        if(now.compareTo(end)<=0){
            return true;
        }
        return false;
    }

    /**
     * 计算上一次的产量
     *
     * @return
     */
    private float calcLastProduction() {
        if( this.lastScheduleResult == null)
            return 0;
        float total = 0;

        short[] barrels = lastScheduleResult.getBarrels();
        for(int i=0;i<barrels.length;i++){
            total += barrels[i];
        }
        return total;
    }


    /**
     * 获取上一次的总泵出量
     *
     * @return
     */
    float lastTotalPumpedVolume =0;
    private float getLastPumpedVolume() {
        PlcProxy proxy = PlcProxyFactory.getPlcProxy();
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
        return getLastPumpedVolume() * SchedulerParams.FEED_DENSITY;
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
    private float getLastAverageConsumption() throws Exception {
        return calcLastConsumed() / getLastInSty();
    }

    /**
     * 统计上一次在猪圈里的总数
     *
     * @return
     */
    private float getLastInSty() throws Exception{
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            return dao.findLastPigsByBatch(batch);
        }
    }

    /**
     * 统计当前在猪圈里总数
     *
     * @return
     */
    private float getCurrentInSty() throws Exception{
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            return dao.findLastPigsByBatch(batch);
        }

    }

    /**
     * 计算预计每头猪所要消耗的饲料量
     *
     * @return
     */
    private float calcEstimatedAverageConsumption() throws Exception{
        if (isInInitStage()) {
            //在入栏阶段，先采用理论值
            LocalDate now = LocalDate.now();
            LocalDate start = TimeUtil.fromDate(batch.getInStockDate());
            int index = now.getDayOfYear() - start.getDayOfYear();
            return SchedulerParams.INIT_STAGE_COMSUMPTIONS[index];
        } else {
            float c = getLastAverageConsumption();
            return c * (1 + SchedulerParams.DAILY_INCREASING_PERCENT);
        }
    }

    /**
     * 计算当前需要生产的
     *
     * @return
     */
    private float calcCurrentProduction() throws SchedulerException{
        try{
            return getCurrentInSty() * calcEstimatedAverageConsumption() - calcCurrentFeedRemaining();
        }
        catch(Exception ex){
            throw new SchedulerException(ex);
        }
    }
}
