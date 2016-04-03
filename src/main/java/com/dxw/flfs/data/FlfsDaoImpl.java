/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data;

import com.dxw.common.models.InStyPlan;
import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import com.dxw.common.models.User;
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
    public Shed getShedByCode(String code) {
        Query query = session.createQuery(String.format("from Shed where code='%s'", code));

        List list = query.list();
        if (list.size() > 0) {
            return (Shed) list.get(0);
        }
        return null;
    }

    @Override
    public List getSheds() {
        Query query = session.createQuery("from Shed");
        return query.list();
    }

    @Override
    public List getUsers() {
        Query query = session.createQuery("from User");
        return query.list();
    }

    @Override
    public InStyPlan getPlan(Sty sty, Date date) {
        String sql = String.format("from InStyPlan s where s.sty.id=%d and s.date='%s'",
                sty.getId(), date.toLocaleString());
        Query query = session.createQuery(sql);

        List list = query.list();

        if( list.size() >0)
            return (InStyPlan) list.get(0);
        return null;
    }

    @Override
    public Sty getStyByCode(String code) {

        Query query = session.createQuery(String.format("from Sty where code='%s'", code));

        List list = query.list();
        if (list.size() > 0) {
            return (Sty) list.get(0);
        }
        return null;
    }

    @Override
    public User getUserByName(String name) {
        String sql = String.format("from User u where u.name='%s'", name);
        Query query = session.createQuery(sql);

        List result = query.list();
        if (result != null && result.size() > 0) {
            return (User) result.get(0);
        }
        return null;
    }

    @Override
    public long getTotalPigInShed(String code) {
        String sql = String.format("select sum(s.pigNumber) from Sty s where s.shed.code='%s'", code);
        Query query = session.createQuery(sql);
        return (long) query.uniqueResult();
    }

    @Override
    public List getStiesByShed(String code) {
        String sql = String.format("from Sty s where s.shed.code='%s' order by s.no", code);
        Query query = session.createQuery(sql);

        return query.list();
    }

    @Override
    public <T> void update(T t) {
        System.out.println(session);
        session.save(t);
    }

    @Override
    public <T> void delete(T t) {
        session.delete(t);
    }

    @Override
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }

}
