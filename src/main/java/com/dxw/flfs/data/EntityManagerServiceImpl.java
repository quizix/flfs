package com.dxw.flfs.data;

import com.dxw.common.services.ServiceException;
import com.dxw.common.services.Services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by zhang on 2016-04-28.
 */
public class EntityManagerServiceImpl implements EntityManagerService {
    @Override
    public EntityManager getEntityManager() {
        return null;
    }

    @Override
    public String getName() {
        return Services.ENTITY_MANAGER_SERVICE;
    }

    @Override
    public void init() throws ServiceException {
        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("jpaUnit");
    }

    @Override
    public void destroy() throws ServiceException {

    }
}
