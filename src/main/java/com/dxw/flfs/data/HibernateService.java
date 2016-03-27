/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data;

import com.dxw.common.services.Service;
import org.hibernate.Session;

/**
 *
 * @author Administrator
 */
public interface HibernateService extends Service {
    public Session getSession();
}
