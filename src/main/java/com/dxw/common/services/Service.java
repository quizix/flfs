/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.services;

/**
 *
 * @author pronics3
 */
public interface Service {
    String getName();
    
    void init() throws ServiceException;
    
    void destroy() throws ServiceException;
}
