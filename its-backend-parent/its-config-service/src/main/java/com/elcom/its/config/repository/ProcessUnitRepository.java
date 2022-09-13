/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.repository;

import com.elcom.its.config.model.ProcessUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface ProcessUnitRepository extends JpaRepository<ProcessUnit, String> {

    boolean existsByCode(String code);

    @Query("SELECT p FROM ProcessUnit p LEFT JOIN Servers s ON p.servers = s.id WHERE ( UPPER(p.name) LIKE %:search% or UPPER(s.name) LIKE %:search% )")
    Page<ProcessUnit> search(@Param("search") String search, Pageable pageable);
    
    Page<ProcessUnit> findAll(Specification<ProcessUnit> spec, Pageable pageable);

    ProcessUnit findByName(String name);
}
