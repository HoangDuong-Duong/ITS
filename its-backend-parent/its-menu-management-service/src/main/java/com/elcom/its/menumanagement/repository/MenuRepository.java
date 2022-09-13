/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.menumanagement.repository;

import com.elcom.its.menumanagement.model.Menu;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface MenuRepository extends CrudRepository<Menu, Integer>{
    List<Menu> findAll();
    
     List<Menu> findAllByOrderByOrderNoAsc();
    
}
