/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service;

import com.elcom.its.config.model.Services;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Admin
 */
public interface ServiceManagerService {

    Page<Services> getAllByPage(Pageable pageable);

    List<Services> getAll();

    Services create(Services services);

    Services update(Services services);

    Optional<Services> findById(Long id);

    boolean addServerToServices(Long idServices, Long idServer);

    void delete(Services services);
}
