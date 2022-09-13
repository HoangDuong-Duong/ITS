/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.repository;

import com.elcom.its.config.model.Servers;
import com.elcom.its.config.model.dto.ServerDTO;
import com.elcom.its.utils.StringUtil;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class ServerCustomizeRepository extends BaseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerCustomizeRepository.class);

    public ServerCustomizeRepository(EntityManagerFactory factory) {
        super(factory);
    }

    public boolean updateServer(Servers server) {
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String update = " ";
            if (!StringUtil.isNullOrEmpty(server.getName())) {
                update += "name = :name ";
            }
            if (!StringUtil.isNullOrEmpty(server.getIpAddress())) {
                update += ", ip_address = :ipAddress ";
            }
            if (!StringUtil.isNullOrEmpty(server.getModifiedBy())) {
                update += ", modified_by = :modifiedBy";
            }
            if (server.getModifiedDate() != null) {
                update += ", modified_date = :modifiedDate ";
            }
            String sql = "Update servers SET " + update + " WHERE id = :id ";
            Query query = session.createNativeQuery(sql);
            if (!StringUtil.isNullOrEmpty(server.getName())) {
                query.setParameter("name", server.getName());
            }
            if (!StringUtil.isNullOrEmpty(server.getIpAddress())) {
                query.setParameter("ipAddress", server.getIpAddress());
            }
            if (!StringUtil.isNullOrEmpty(server.getModifiedBy())) {
                query.setParameter("modifiedBy", server.getModifiedBy());
            }
            if (server.getModifiedDate() != null) {
                query.setParameter("modifiedDate", server.getModifiedDate());
            }
            query.setParameter("id", server.getId());
            int result = query.executeUpdate();
            tx.commit();
            return result > 0;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return false;
    }

    public List<ServerDTO> findAllServer() {
        Session session = openSession();
        try {
            String sql = " SELECT s.id AS id, s.name AS name, s.gpu As gpu, s.ip_address AS ipAddress "
                    + " FROM servers s ";
            return session.createNativeQuery(sql)
                    .addScalar("id", LongType.INSTANCE)
                    .addScalar("name", StringType.INSTANCE)
                    .addScalar("ipAddress", StringType.INSTANCE)
                    .addScalar("gpu", IntegerType.INSTANCE)
                    .setResultTransformer(Transformers.aliasToBean(ServerDTO.class))
                    .getResultList();
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return null;
    }

    public Servers findById(Long id) {
        Session session = openSession();
        Object result = null;
        try {
            Query query = session.createNativeQuery("SELECT * FROM servers s WHERE s.id = ?", Servers.class);
            query.setParameter(1, id);
            result = query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return result != null ? (Servers) result : null;
    }

    public Servers findByName(String name) {
        Session session = openSession();
        Object result = null;
        try {
            Query query = session.createNativeQuery("SELECT * FROM servers s WHERE s.name = ?", Servers.class);
            query.setParameter(1, name);
            result = query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return result != null ? (Servers) result : null;
    }

    public Servers findByIp(String ip) {
        Session session = openSession();
        Object result = null;
        try {
            Query query = session.createNativeQuery("SELECT * FROM servers s WHERE s.ip_address = ?", Servers.class);
            query.setParameter(1, ip);
            result = query.getSingleResult();
        } catch (Exception ex) {
            LOGGER.error(StringUtil.printException(ex));
        } finally {
            closeSession(session);
        }
        return result != null ? (Servers) result : null;
    }
}
