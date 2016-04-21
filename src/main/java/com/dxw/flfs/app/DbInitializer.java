package com.dxw.flfs.app;

import com.dxw.common.models.AppConfig;
import com.dxw.common.models.Batch;
import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.Services;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.HibernateServiceImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhang on 2016-04-21.
 */
public class DbInitializer {
    ServiceRegistry registry;

    public DbInitializer(ServiceRegistry registry){
        this.registry = registry;
    }

    public void registerService() throws ServiceException {
        HibernateService hibernateService = new HibernateServiceImpl();
        hibernateService.init();
        registry.register(hibernateService);
    }

    public void prepareData(){
        HibernateService hibernateService = (HibernateService)
                this.registry.lookupService(Services.HIBERNATE_SERVICE);
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            dao.begin();

            Shed shed = new Shed();
            shed.setCreateTime(new Date());
            shed.setModifyTime(new Date());
            shed.setAddress("江西鄱阳");
            shed.setCode("12345678");
            shed.setName("猪舍1");
            dao.update(shed);

            Batch batch = new Batch();
            batch.setCode("1");
            batch.setInStockNumber(100);
            batch.setInStockDate(new Date());
            batch.setInStockDuration(10);
            batch.setCreateTime(new Date());
            batch.setModifyTime(new Date());

            dao.update(batch);

            Set<Batch> batches = new HashSet<>();
            batches.add(batch);

            Set<Sty> sties = new HashSet<>();
            for (int i = 0; i < 24; i++) {
                Sty sty = new Sty();
                sty.setCreateTime(new Date());
                sty.setModifyTime(new Date());
                sty.setCode(Integer.toString(i));
                sty.setName("Sty" + i);
                sty.setLastNumber(80 + i);
                sty.setCurrentNumber(100 + i);
                sty.setShed(shed);
                sties.add(sty);
                sty.setNo(i);

                dao.update(sty);
            }

            batch.setSties(sties);

            dao.update(batch);

            AppConfig config = new AppConfig();
            config.setCreateTime(new Date());
            config.setModifyTime(new Date());
            config.setBatchCode("1");
            config.setAppId(FlfsApp.getContext().getAppId());
            config.setHost("192.168.1.10");
            dao.update(config);

            dao.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
