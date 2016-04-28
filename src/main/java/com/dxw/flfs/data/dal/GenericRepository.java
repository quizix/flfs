package com.dxw.flfs.data.dal;

import java.util.Collection;

/**
 * Created by zhang on 2016-04-28.
 */
public interface GenericRepository<ENTITY> {
    ENTITY save(ENTITY entity);
    Boolean delete(ENTITY entity);
    ENTITY edit(ENTITY entity);
    ENTITY find(Long id);

    Collection<ENTITY> findAll();
}
