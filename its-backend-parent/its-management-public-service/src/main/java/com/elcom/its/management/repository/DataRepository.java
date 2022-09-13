/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Admin
 */
@Repository
public class DataRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataRepository.class);

    private SessionFactory sessionFactory;

    @Autowired
    public DataRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }

        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    public void addDailyPartition(String[] tables) {
        Session session = openSession();
        try {
            //Date for partition name
            DateFormat format = new SimpleDateFormat("yyyy_MM_dd");
            Calendar calTomorrow = Calendar.getInstance();
            calTomorrow.add(Calendar.DATE, 1);
            String tomorrow = format.format(calTomorrow.getTime());

            //Date for from to value
            Date now = new Date();
            String tomorrowValue = DateUtil.toString(DateUtil.addDay(now, 1), "yyyy-MM-dd") + " 00:00:00";
            String afterTomorrowValue = DateUtil.toString(DateUtil.addDay(now, 2), "yyyy-MM-dd") + " 00:00:00";
            LOGGER.info("tomorrowValue: {}, afterTomorrowValue: {}", tomorrowValue, afterTomorrowValue);
            //Query
            Transaction tx = session.beginTransaction();
            String partitionName = null;
            String strSql;
            Query query = null;
            for (String table : tables) {
                partitionName = table + "_" + tomorrow;
                strSql = " CREATE TABLE " + partitionName + " PARTITION OF " + table
                        + " FOR VALUES FROM (TO_TIMESTAMP('" + tomorrowValue + "', 'YYYY-MM-DD HH24:MI:SS')) "
                        + " TO (TO_TIMESTAMP('" + afterTomorrowValue + "', 'YYYY-MM-DD HH24:MI:SS')) ";
                query = session.createNativeQuery(strSql);
                query.executeUpdate();
            }
            tx.commit();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
    }

    public void addMonthlyPartition(String[] tables) {
        Session session = openSession();
        try {
            String fromPartitonValue = DateUtil.getPartitionValueOfCurrentMonth();
            String toPartitonValue = DateUtil.getPartitionValueOfNextMonth();

            Transaction tx = session.beginTransaction();
            String strSql;
            Query query;
            String partitionName = null;
            for (String table : tables) {
                partitionName = DateUtil.getPartitionNameOfNextMonth(table);
                strSql = " CREATE TABLE " + partitionName + " PARTITION OF " + table
                        + " FOR VALUES FROM (TO_TIMESTAMP('" + fromPartitonValue + "', 'YYYY-MM-DD HH24:MI:SS')) "
                        + " TO (TO_TIMESTAMP('" + toPartitonValue + "', 'YYYY-MM-DD HH24:MI:SS')) ";
                query = session.createNativeQuery(strSql);
                query.executeUpdate();
            }
            tx.commit();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
    }

    public boolean deleteTableData(String[] tables, String[] columnTables, String oldStartTime) {
        Session session = openSession();
        try {
            Transaction tx = session.beginTransaction();
            String strSql;
            Query query;
            int size = tables.length;
            String column = null;
            String table = null;
            int result = 0;
            for (int i = 0; i < size; i++) {
                column = columnTables[i];
                table = tables[i];
                strSql = " DELETE FROM " + table + " WHERE " + column + " <= (TO_TIMESTAMP('"
                        + oldStartTime + "', 'YYYY-MM-DD HH24:MI:SS')) ";
                query = session.createNativeQuery(strSql);
                result = query.executeUpdate();
            }
            tx.commit();
            return result > 0;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        } finally {
            closeSession(session);
        }
        return false;
    }

    private Session openSession() {
        Session session = this.sessionFactory.openSession();
        return session;
    }

    private void closeSession(Session session) {
        if (session.isOpen()) {
            session.disconnect();
            session.close();
        }
    }
}
