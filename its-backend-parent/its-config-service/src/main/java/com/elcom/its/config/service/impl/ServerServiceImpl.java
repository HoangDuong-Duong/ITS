/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service.impl;

import com.elcom.its.config.model.Servers;
import com.elcom.its.config.model.dto.ServerDTO;
import com.elcom.its.config.repository.ServerCustomizeRepository;
import com.elcom.its.config.repository.ServerRepository;
import com.elcom.its.config.service.ServerService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ServerServiceImpl implements ServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerServiceImpl.class);

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerCustomizeRepository serverCustomizeRepository;

    @Override
    public Servers save(Servers server) {
        return serverRepository.save(server);
    }

    @Override
    public boolean updateServer(Servers server) {
        return serverCustomizeRepository.updateServer(server);
    }

    @Override
    public List<ServerDTO> getServerList() {
        List<ServerDTO> listServerDTO = null;
        try {
            listServerDTO = serverCustomizeRepository.findAllServer();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
        return listServerDTO;
    }

    @Override
    public Servers findById(Long id) {
        Servers s = null;
        try {
            s = serverCustomizeRepository.findById(id);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
        return s;
    }

    @Override
    public void remove(Servers servers) {
        try {
            serverRepository.delete(servers);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public Servers findByName(String name) {
        try {
            return serverCustomizeRepository.findByName(name);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Servers findByIp(String ip) {
        try {
            return serverCustomizeRepository.findByIp(ip);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
        return null;   
    }

}
