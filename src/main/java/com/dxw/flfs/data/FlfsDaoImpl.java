/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data;

import com.dxw.common.models.*;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class FlfsDaoImpl implements FlfsDao {

    Session session;

    public FlfsDaoImpl(HibernateService hibernateService) {
        this.session = hibernateService.getSession();
    }

    @Override
    public Shed findShedByCode(String code) {
        Query query =
                session.createQuery("from Shed where code=:code")
                    .setParameter("code",code);

        List list = query.list();
        if (list.size() > 0) {
            return (Shed) list.get(0);
        }
        return null;
    }

    @Override
    public Batch findBatchByCode(String code) {
        Query query =
                session.createQuery("from Batch where code=:code")
                        .setParameter("code",code);

        List list = query.list();
        if (list.size() > 0) {
            return (Batch) list.get(0);
        }
        return null;
    }

    @Override
    public AppConfig findAppConfig(String appId) {
        Query query =
                session.createQuery("from AppConfig where appId=:appId")
                        .setParameter("appId",appId);

        List list = query.list();
        if (list.size() > 0) {
            return (AppConfig) list.get(0);
        }
        return null;
    }

    @Override
    public List findAllSheds() {
        Query query = session.createQuery("from Shed");
        return query.list();
    }

    @Override
    public List findAllUsers() {
        Query query = session.createQuery("from User");
        return query.list();
    }

    @Override
    public InStyPlan findPlan(Sty sty, Date date) {
        Query query = session.createQuery("from InStyPlan s where s.sty=:sty and s.date=:date")
                .setParameter("sty", sty)
                .setParameter("date", date);

        List list = query.list();

        if( list.size() >0)
            return (InStyPlan) list.get(0);
        return null;
    }

    @Override
    public List findProductionInstructions(String code, Date start, Date end) {
        Query query = session.createQuery("from ProductionInstruction pi where pi.shed.code=:code and pi.date >=:start and pi.date<=:end")
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("code", code);

        return query.list();

    }

    @Override
    public Sty findStyByCode(String code) {
        Query query = session.createQuery("from Sty where code=:code")
                .setParameter("code", code);

        List list = query.list();
        if (list.size() > 0) {
            return (Sty) list.get(0);
        }
        return null;
    }



    @Override
    public User findUserByName(String name) {
        Query query = session.createQuery("from User u where u.name=:name")
                .setParameter("name", name);

        List result = query.list();
        if (result != null && result.size() > 0) {
            return (User) result.get(0);
        }
        return null;
    }

    @Override
    public long findTotalPigPlanInShed(String code, Date date) {
        Query query = session.createQuery("select sum(s.value) from InStyPlan s where s.date=:date and s.sty.shed.code=:code")
                .setParameter("date", date)
                .setParameter("code", code);
        return (long) query.uniqueResult();
    }

    @Override
    public long findCurrentPigsByBatch(Batch batch) {
        Query query = session.createQuery("select sum(sties.currentNumber) from Batch b join b.sties sties where b.code=:code")
                .setParameter("code", batch.getCode());
        return (long) query.uniqueResult();
    }

    @Override
    public long findLastPigsByBatch(Batch batch) {
        Query query = session.createQuery("select sum(sties.lastNumber) from Batch b join b.sties sties where b.code=:code")
                .setParameter("code", batch.getCode());
        return (long) query.uniqueResult();
    }

    @Override
    public List findStiesByShed(String code) {
        Query query = session.createQuery("from Sty s where s.shed.code=:code order by s.no")
                .setParameter("code", code);

        return query.list();
    }

    @Override
    public <T> void update(T t) {
        session.save(t);
    }

    @Override
    public <T> void delete(T t) {
        session.delete(t);
    }

    @Override
    public void begin() {
        if (session != null)
            session.beginTransaction();

    }

    @Override
    public void commit() {
        if( session != null)
            session.getTransaction().commit();
    }

    @Override
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }

}
