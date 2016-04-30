package com.dxw.flfs.data.dal;

import com.dxw.flfs.data.models.PigletPlan;
import com.dxw.flfs.data.models.Shed;
import com.dxw.flfs.data.models.SiteConfig;
import com.dxw.flfs.data.models.User;
import org.hibernate.Session;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by zhang on 2016-04-28.
 */
public class UnitOfWork implements AutoCloseable {

    private Session session;

    private DefaultGenericRepository<User> userRepository;
    private DefaultGenericRepository<Shed> shedRepository;
    private DefaultGenericRepository<SiteConfig> siteConfigRepository;
    private DefaultGenericRepository<PigletPlan> pigletPlanRepository;

    //private DefaultGenericRepository<User> userRepository;
    //private DefaultGenericRepository<User> userRepository;

    public UnitOfWork(Session session){
        this.session = session;
    }

    /*public void flush(){
        session.flush();
    }*/
    public DefaultGenericRepository<User> getUserRepository() {
        if( userRepository == null)
            userRepository = new DefaultGenericRepository<>(session, User.class);
        return userRepository;
    }

    public DefaultGenericRepository<SiteConfig> getSiteConfigRepository() {
        if(siteConfigRepository==null )
            siteConfigRepository = new DefaultGenericRepository<>(session, SiteConfig.class);
        return siteConfigRepository;
    }

    @Override
    public void close() throws Exception {
        if( this.session!= null)
            session.close();
    }

    public void begin(){
        if( this.session != null)
            session.beginTransaction();
    }

    public void commit(){
        if( this.session != null)
            session.getTransaction().commit();
    }

    public void rollback(){
        if( this.session != null)
            session.getTransaction().rollback();
    }


    public DefaultGenericRepository<Shed> getShedRepository() {
        if( shedRepository == null)
            shedRepository = new DefaultGenericRepository<>(session, Shed.class);
        return shedRepository;
    }

    public DefaultGenericRepository<PigletPlan> getPigletPlanRepository() {
        if( pigletPlanRepository == null)
            pigletPlanRepository = new DefaultGenericRepository<>(session, PigletPlan.class);
        return pigletPlanRepository;
    }

    public SiteConfig getSiteConfig(String siteCode){
        DefaultGenericRepository<SiteConfig> r = getSiteConfigRepository();
        Collection<SiteConfig> configs = r.findAll();

        Optional<SiteConfig> config = configs.stream()
                .filter(c -> c.getSiteCode().equals(siteCode))
                .findFirst();

        if (config.isPresent()) {
            return config.get();
        }
        return null;
    }
}
