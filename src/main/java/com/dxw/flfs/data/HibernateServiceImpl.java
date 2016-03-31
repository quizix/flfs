/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data;

import com.dxw.common.services.ServiceException;
import com.dxw.common.services.Services;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 *
 * @author Administrator
 */
public class HibernateServiceImpl implements HibernateService {

    private SessionFactory sessionFactory;

    @Override
    public void init() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Override
    public Session getSession() {
        if (sessionFactory != null) {
            return sessionFactory.openSession();
        }
        return null;
    }

    @Override
    public String getName() {
        return Services.HIBERNATE_SERVICE;
    }

    @Override
    public void destroy() throws ServiceException {
       if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

}
