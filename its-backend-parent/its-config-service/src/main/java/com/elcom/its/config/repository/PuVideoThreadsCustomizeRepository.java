/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.repository;

import com.elcom.its.config.model.PuVideoThreads;
import com.elcom.its.utils.StringUtil;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class PuVideoThreadsCustomizeRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PuVideoThreadsCustomizeRepository.class);

    public PuVideoThreadsCustomizeRepository(EntityManagerFactory factory) {
        super(factory);
    }

    public List<PuVideoThreads> findListPuVideoThreadsByIdProcessUnit(String IdProcessUnit) {
        List<PuVideoThreads> puVideoThreadses = null;
        Session session = openSession();
        try {
            Query query = session.createNativeQuery("SELECT * FROM pu_video_threads WHERE process_unit_id = ?", PuVideoThreads.class);
            query.setParameter(1, IdProcessUnit);
            puVideoThreadses = query.getResultList();
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return puVideoThreadses;
    }
}
