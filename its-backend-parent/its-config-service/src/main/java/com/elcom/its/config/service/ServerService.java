/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service;

import com.elcom.its.config.model.Servers;
import com.elcom.its.config.model.dto.ServerDTO;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ServerService {

    public Servers save(Servers server);

    public boolean updateServer(Servers server);

    public List<ServerDTO> getServerList();

    public Servers findById(Long id);
    
    public Servers findByIp(String ip);
    
    public Servers findByName(String name);

    public void remove(Servers servers);
    
}
