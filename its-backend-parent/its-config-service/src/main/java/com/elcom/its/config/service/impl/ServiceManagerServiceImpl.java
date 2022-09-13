/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service.impl;

import com.elcom.its.config.model.Services;
import com.elcom.its.config.repository.ServicesRepository;
import com.elcom.its.config.service.ServiceManagerService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ServiceManagerServiceImpl implements ServiceManagerService {

    @Autowired
    private ServicesRepository servicesRepository;

    @Override
    public Services create(Services services) {
        services = servicesRepository.save(services);
        return services;
    }

    @Override
    public Optional<Services> findById(Long id) {
        return servicesRepository.findById(id);
    }

    @Override
    public Services update(Services services) {
        services = servicesRepository.save(services);
        return services;
    }

    @Override
    public void delete(Services services) {
        servicesRepository.delete(services);
    }

    @Override
    public Page<Services> getAllByPage(Pageable pageable) {
        return servicesRepository.findAll(pageable);
    }

    @Override
    public List<Services> getAll() {
        return servicesRepository.findAll();
    }

    @Override
    public boolean addServerToServices(Long idServices, Long idServer) {
        return true;
    }
}
