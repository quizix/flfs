package com.dxw.flfs.data.dal;

import com.dxw.flfs.data.models.AppConfig;
import com.dxw.flfs.data.models.User;
import org.hibernate.Session;

/**
 * Created by zhang on 2016-04-28.
 */
public class UnitOfWork implements AutoCloseable {

    private Session session;

    private DefaultGenericRepository<User,Long> userRepository;
    private DefaultGenericRepository<AppConfig,Long> appConfigRepository;
    //private DefaultGenericRepository<User> userRepository;
    //private DefaultGenericRepository<User> userRepository;

    public UnitOfWork(Session session){
        this.session = session;
    }

    /*public void flush(){
        session.flush();
    }*/
    public DefaultGenericRepository<User, Long> getUserRepository() {
        if( userRepository == null)
            userRepository = new DefaultGenericRepository<>(session, User.class, Long.class);
        return userRepository;
    }

    public DefaultGenericRepository<AppConfig, Long> getAppConfigRepository() {
        if(appConfigRepository==null )
            appConfigRepository = new DefaultGenericRepository<>(session, AppConfig.class, Long.class);
        return appConfigRepository;
    }

    @Override
    public void close() throws Exception {
        if( this.session!= null)
            session.close();
    }


}
