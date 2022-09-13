/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service;

import com.elcom.its.config.model.ProcessUnit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Admin
 */
public interface ProcessUnitService {

    Page<ProcessUnit> getAllByPage(Pageable pageable);

    Page<ProcessUnit> getByNamePage(String search, Pageable pageable);

    List<ProcessUnit> getAll();

    ProcessUnit create(ProcessUnit processUnit);

    ProcessUnit update(ProcessUnit processUnit);

    Optional<ProcessUnit> findById(String id);

    ProcessUnit findByName(String name);

    boolean checkByCode(String code);

    void delete(ProcessUnit processUnit);

    boolean addServerToProcessUnit(Long idProcessUnit, Long idServer);

    String toJsonSpec(ProcessUnit processUnit);

    Page<ProcessUnit> findProcessUnit(Integer puType, String search, Pageable pageable);
}
