package com.dxw.flfs.app;

import com.dxw.common.services.ServiceException;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.Services;
import com.dxw.common.utils.TimeUtil;
import com.dxw.flfs.data.FlfsDao;
import com.dxw.flfs.data.FlfsDaoImpl;
import com.dxw.flfs.data.HibernateService;
import com.dxw.flfs.data.HibernateServiceImpl;
import com.dxw.flfs.data.models.Batch;
import com.dxw.flfs.data.models.Shed;
import com.dxw.flfs.data.models.SiteConfig;
import com.dxw.flfs.data.models.Sty;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhang on 2016-04-21.
 */
public class DbInitiator {
    ServiceRegistry registry;

    public DbInitiator(ServiceRegistry registry){
        this.registry = registry;
    }

    public void registerService() throws ServiceException {
        HibernateService hibernateService = new HibernateServiceImpl();
        hibernateService.init();
        registry.register(hibernateService);
    }

    public void prepareData(){
        HibernateService hibernateService = (HibernateService)
                this.registry.getService(Services.HIBERNATE_SERVICE);
        try (FlfsDao dao = new FlfsDaoImpl(hibernateService)) {
            dao.begin();

            Shed shed = new Shed();
            shed.setCreateTime(new Date());
            shed.setModifyTime(new Date());
            shed.setAddress("江西鄱阳");
            shed.setCode("12345678");
            shed.setActive(true);
            shed.setName("猪舍1");
            dao.update(shed);

            Date now = new Date();
            LocalDate later = TimeUtil.fromDate(now);
            later.plusDays(10);
            Batch batch = new Batch();
            batch.setCode("1");
            batch.setInStockNumber(100);
            batch.setStartDate(now);
            batch.setEndDate(now);
            batch.setCreateTime(now);
            batch.setModifyTime(now);

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

            SiteConfig config = new SiteConfig();
            config.setCreateTime(new Date());
            config.setModifyTime(new Date());
            //config.setBatchCode("1");
            config.setSiteCode(FlfsApp.getContext().getSiteCode());
            config.setHost("192.168.1.10");
            dao.update(config);

            dao.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
