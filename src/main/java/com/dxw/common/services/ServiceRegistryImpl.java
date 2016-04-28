/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author pronics3
 */
public class ServiceRegistryImpl implements ServiceRegistry{

    private final Map<String, Service> services = new ConcurrentHashMap<>();
    
    private static ServiceRegistry registry;
    public static ServiceRegistry getInstance(){
        if( registry ==null){
            registry = new ServiceRegistryImpl();
        }
        return registry;
    }
    @Override
    public void register(Service service) throws ServiceException{
        if( service ==null)
            throw new IllegalArgumentException("service cannot be null");
        
        if( services.containsKey(service.getName()))
            throw new ServiceException( String.format("service with name %s already exists", service.getName()));
        
        services.put( service.getName(), service);
    }

    @Override
    public void unregister(Service service) throws ServiceException{
        if( service ==null)
            throw new IllegalArgumentException("service cannot be null");
        
        if( services.containsKey(service.getName()))
            throw new ServiceException( String.format("service with name %s not exists", service.getName()));
        
        services.remove(service.getName());
    }

    @Override
    public Service getService(String name) {
        return services.get(name);
    }
    
}
