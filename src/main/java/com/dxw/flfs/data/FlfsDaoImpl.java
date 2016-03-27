/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data;

import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import com.dxw.common.services.ServiceRegistry;
import com.dxw.common.services.ServiceRegistryImpl;
import com.dxw.common.services.Services;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Administrator
 */
public class FlfsDaoImpl implements FlfsDao {

    Session session;

    public FlfsDaoImpl() {
        this.session = getSession();
    }

    private Session getSession() {
        ServiceRegistry registry = ServiceRegistryImpl.getInstance();
        HibernateService service = (HibernateService) registry.lookupService(Services.HIBERNATE_SERVICE);

        if (service != null) {
            return service.getSession();
        }
        return null;
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
        List list = query.list();
        return list;

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
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }

}
