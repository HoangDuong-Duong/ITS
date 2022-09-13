/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.menumanagement.service.impl;

import com.elcom.its.menumanagement.dto.MenuDTO;
import com.elcom.its.menumanagement.model.Menu;
import com.elcom.its.menumanagement.model.MenuResources;
import com.elcom.its.menumanagement.model.RelationResources;
import com.elcom.its.menumanagement.model.RoleMenu;
import com.elcom.its.menumanagement.repository.MenuRepository;
import com.elcom.its.menumanagement.repository.MenuResourcesRepository;
import com.elcom.its.menumanagement.repository.RelationResourcesRepository;
import com.elcom.its.menumanagement.repository.RoleMenuRepository;
import com.elcom.its.menumanagement.service.MenuManagementService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class MenuManagementServiceImpl implements MenuManagementService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private MenuResourcesRepository menuResourcesRepository;

    @Autowired
    private RelationResourcesRepository relationResourcesRepository;

    @Override
    public List<MenuDTO> findAllMenu() {
        List<Menu> listAllMenu = menuRepository.findAllByOrderByOrderNoAsc();
        List<MenuDTO> listMenuDTO = new ArrayList<>();
        for (Menu menu : listAllMenu) {
            MenuDTO menuDTO = new MenuDTO(menu);
            List<MenuResources> listMenuResources = menuResourcesRepository.findByMenuId(menu.getId());
            if (listMenuResources != null && (!listMenuResources.isEmpty())) {
                menuDTO.setResourceCode(listMenuResources.get(0).getResourcesCode());
            }
            listMenuDTO.add(menuDTO);
        }

        return listMenuDTO;
    }

    @Override
    public Menu findMenuById(int menuId) {
        try {
            Optional<Menu> optional = menuRepository.findById(menuId);
            return optional.get();
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public List<RoleMenu> findByRoleCode(String roleCode) {
        return roleMenuRepository.findByRoleCode(roleCode);
    }

    @Override
    public void deleteAllRoleMenu(List<RoleMenu> listRoleMenu) {
        roleMenuRepository.deleteAll(listRoleMenu);
    }

    @Override
    public void saveAllRoleMenu(List<RoleMenu> listRoleMenu) {
        roleMenuRepository.saveAll(listRoleMenu);
    }

    @Override
    public List<RelationResources> findAllRelationResourceses() {
        return relationResourcesRepository.findAll();
    }

}
