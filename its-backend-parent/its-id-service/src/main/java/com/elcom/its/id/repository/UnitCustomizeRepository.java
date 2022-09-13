/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.id.repository;

import com.elcom.its.id.model.Unit;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.elcom.its.id.model.dto.UnitDTO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class UnitCustomizeRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitCustomizeRepository.class);

    @Autowired
    public UnitCustomizeRepository(EntityManagerFactory factory) {
        super(factory);
    }

    public List<Unit> findUnitByStageIdList(List<String> stageIdList) {
        if (stageIdList == null || stageIdList.isEmpty()) {
            return null;
        }
        Session session = openSession();
        try {
            String sql = " SELECT * FROM police_unit WHERE 1 = 1 ";
            sql += " AND (";
            int i = 0;
            for (String stageId : stageIdList) {
                sql += "list_of_stage LIKE '%" + stageId + "%' ";
                if (i < stageIdList.size() - 1) {
                    sql += " OR ";
                }
                i++;
            }
            sql += ")";
            Query query = session.createNativeQuery(sql, Unit.class);
            return query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return null;
    }
}
