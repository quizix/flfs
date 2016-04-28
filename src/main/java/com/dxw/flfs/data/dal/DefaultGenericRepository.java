package com.dxw.flfs.data.dal;

import org.hibernate.Criteria;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by zhang on 2016-04-28.
 */
public class DefaultGenericRepository<ENTITY, PK_CLASS extends Serializable>
        implements GenericRepository<ENTITY,PK_CLASS> {

    Session session;
    Class<ENTITY> entityClass;
    Class<PK_CLASS> pkClass;

    public DefaultGenericRepository(Session session, Class<ENTITY> entityClass, Class<PK_CLASS> pkClass){
        this.session = session;
        this.entityClass = entityClass;
        this.pkClass = pkClass;
    }
    @Override
    public ENTITY save(ENTITY entity) {
        session.persist(entity);
        session.flush();
        return entity;
    }

    @Override
    public Boolean delete(ENTITY entity) {
        try{
            session.delete(entity);
            return true;
        }
        catch(Exception ex){
            return false;
        }
    }

    /*@Override
    public Boolean delete(PK_CLASS id) {
        try{
            ENTITY entity = session.load(entityClass,id);
            return delete(entity);
        }
        catch(Exception ex){
            return false;
        }
    }*/

    @Override
    public ENTITY edit(ENTITY entity) {
        try{
            return (ENTITY)session.merge(entity);
        }
        catch(Exception ex){
            return  null;
        }

    }

    @Override
    public ENTITY find(Serializable id) {
        return session.load(entityClass, id);
    }


    @Override
    public Collection<ENTITY> findAll() {
        Criteria query = session
                .createCriteria(entityClass);
        return query.list();
    }
}
