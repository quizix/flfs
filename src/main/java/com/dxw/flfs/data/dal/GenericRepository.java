package com.dxw.flfs.data.dal;

import org.hibernate.Session;
import org.hibernate.criterion.CriteriaQuery;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by zhang on 2016-04-28.
 */
public interface GenericRepository<ENTITY, PK_CLASS extends Serializable> {
    ENTITY save(ENTITY entity);
    Boolean delete(ENTITY entity);
    ENTITY edit(ENTITY entity);
    ENTITY find(PK_CLASS id);

    Collection<ENTITY> findAll();
}
