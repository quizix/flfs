package com.dxw.flfs.scheduling;

import com.dxw.flfs.app.FlfsApp;
import com.dxw.flfs.communication.PlcDelegate;
import com.dxw.flfs.communication.PlcDelegateFactory;
import com.dxw.flfs.communication.PlcModel;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.dal.GenericRepository;
import com.dxw.flfs.data.dal.UnitOfWork;
import com.dxw.flfs.data.models.PigletPlan;
import com.dxw.flfs.data.models.SiteConfig;
import com.dxw.flfs.data.models.Sty;

import java.util.Optional;

/**
 * Created by zhang on 2016/4/3.
 */
public class AdaptiveScheduler implements FlfsScheduler {


    private HibernateService hibernateService;
    private String siteCode = FlfsApp.getContext().getSiteCode();
    private boolean amOrPm;
    Distributor distributor;


    public AdaptiveScheduler(HibernateService hibernateService, Distributor distributor) {
        this.hibernateService = hibernateService;
        this.distributor = distributor;
    }

    UnitOfWork uow = null;
    SiteConfig siteConfig = null;

    @Override
    public ScheduleResult schedule6AM() throws SchedulerException {
        amOrPm = true;
        return doSchedule();
    }

    @Override
    public ScheduleResult schedule6PM() throws SchedulerException {
        amOrPm = false;
        return doSchedule();
    }

    private ScheduleResult doSchedule() throws SchedulerException {
        try (UnitOfWork uow = new UnitOfWork(hibernateService.getSession())) {

            GenericRepository<SiteConfig> repository = uow.getSiteConfigRepository();
            Optional<SiteConfig> config =
                    repository.findAll().stream().filter(x -> x.getSiteCode().equals(siteCode)).findFirst();

            if (config.isPresent()) {
                this.siteConfig = config.get();
            } else
                throw new SchedulerException("无法获取站点信息！");

            float total = calcCurrentProduction();
            return distributor.distribute(total);
        } catch (Exception ex) {
            throw new SchedulerException(ex.getMessage());
        }
    }

    /**
     * 计算当前需要生产的
     *
     * @return
     */
    private float calcCurrentProduction() throws SchedulerException {
        try {
            //=猪的数量*每头猪预计消耗量 - 剩余的饲料
            return getPigCount() * calcEstimatedAverageConsumption() - calcCurrentFeedRemaining();
        } catch (Exception ex) {
            throw new SchedulerException(ex);
        }
    }

    /**
     * 统计当前在猪圈里总数
     *
     * @return
     */
    private float getPigCount() throws Exception {
        //如果是起始阶段，数据从计划里来，否则从实际数据过来

        if (siteConfig.getStage() == 0) {
            //阶段0：小猪陆续入场
            int total =
                    siteConfig.getPlans().stream()
                            .mapToInt(PigletPlan::getCount)
                            .sum();
            return total;
        } else {
            //阶段1： 小猪入场结束
            int total =
                    siteConfig.getSties().stream()
                            .mapToInt(Sty::getCurrentNumber)
                            .sum();
            return total;
        }
    }


    /**
     * 上一次下达的生产量
     *
     * @return
     */
    private float calcLastProduction() {
        PlcDelegate proxy = PlcDelegateFactory.getPlcDelegate();
        PlcModel model = proxy.getModel();

        short[] barrels;
        float dry,water;
        if(amOrPm) {
            barrels = model.getProductionAmountsPm();
            water = model.getWaterPm();
            dry = model.getDryPm();
        }
        else {
            barrels = model.getProductionAmountsAm();
            water = model.getWaterAm();
            dry = model.getDryAm();
        }

        if (barrels == null)
            return 0;
        float total = 0;
        for (int i = 0; i < barrels.length; i++) {
            total += barrels[i];
        }

        return total*(water+dry);
    }

    /**
     * 计算当前饲料剩余量
     *
     * @return
     */

    private float lastRemaining = 0;

    private float calcCurrentFeedRemaining() {
        float currentRemaining = calcLastProduction() - calcLastConsumed();
        lastRemaining += currentRemaining;
        if (lastRemaining < 0)
            lastRemaining = 0;
        return lastRemaining;
    }


    /**
     * 获取上一次的总泵出量
     *
     * @return
     */
    private float getLastPumpedVolume() {
        PlcDelegate proxy = PlcDelegateFactory.getPlcDelegate();
        PlcModel model = proxy.getModel();

        float[] data = proxy.getFlowValues();

        float last;
        if (amOrPm)
            last = model.getAccumulateFlowPm();
        else
            last = model.getAccumulateFlowAm();

        float volume = data[0] - last;
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
    private float getLastInSty() throws Exception {
        int total =
                siteConfig.getSties().stream()
                        .mapToInt(Sty::getLastNumber)
                        .sum();
        return total;
    }

    /**
     * 计算预计每头猪所要消耗的饲料量
     *
     * @return
     */
    private float calcEstimatedAverageConsumption() throws Exception {
        if( siteConfig.getStage() == 0){
            //小猪阶段
            return SchedulerParams.PIGLET_STAGE_COMSUMPTION;
        }
        else{
            float c = getLastAverageConsumption();
            return c * (1 + SchedulerParams.DAILY_INCREASING_PERCENT);
        }
    }
}
