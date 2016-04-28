package com.dxw.flfs.data;

import com.dxw.common.services.Service;
import com.dxw.common.services.ServiceException;

import javax.persistence.EntityManager;

/**
 * Created by zhang on 2016-04-28.
 */
public interface EntityManagerService extends Service {
    EntityManager getEntityManager();
}
