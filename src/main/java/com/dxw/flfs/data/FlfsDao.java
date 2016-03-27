/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.flfs.data;

import com.dxw.common.models.Shed;
import com.dxw.common.models.Sty;
import java.util.List;

public interface FlfsDao extends AutoCloseable{
    
    public Shed getShedByCode(String code);
    
    public List getSheds();
    
    public Sty getStyByCode(String code);
    
    
}
