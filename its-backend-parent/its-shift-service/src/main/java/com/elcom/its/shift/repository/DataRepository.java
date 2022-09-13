package com.elcom.its.shift.repository;

import com.elcom.its.utils.DateUtil;
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

@Repository
public class DataRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataRepository.class);
    private SessionFactory sessionFactory;

    @Autowired
    public DataRepository(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        } else {
            this.sessionFactory = (SessionFactory)factory.unwrap(SessionFactory.class);
        }
    }

    public void addDailyPartition(String[] tables) {
        Session session = this.openSession();

        try {
            DateFormat format = new SimpleDateFormat("yyyy_MM_dd");
            Calendar calTomorrow = Calendar.getInstance();
            calTomorrow.add(5, 1);
            String tomorrow = format.format(calTomorrow.getTime());
            Date now = new Date();
            Date var10000 = DateUtil.addDay(now, 1);
            String tomorrowValue = DateUtil.toString(var10000, "yyyy-MM-dd") + " 00:00:00";
            var10000 = DateUtil.addDay(now, 2);
            String afterTomorrowValue = DateUtil.toString(var10000, "yyyy-MM-dd") + " 00:00:00";
            LOGGER.info("tomorrowValue: {}, afterTomorrowValue: {}", tomorrowValue, afterTomorrowValue);
            Transaction tx = session.beginTransaction();
            String partitionName = null;
            Query query = null;
            String[] var13 = tables;
            int var14 = tables.length;

            for(int var15 = 0; var15 < var14; ++var15) {
                String table = var13[var15];
                partitionName = table + "_" + tomorrow;
                String strSql = " CREATE TABLE " + partitionName + " PARTITION OF " + table + " FOR VALUES FROM (TO_TIMESTAMP('" + tomorrowValue + "', 'YYYY-MM-DD HH24:MI:SS'))  TO (TO_TIMESTAMP('" + afterTomorrowValue + "', 'YYYY-MM-DD HH24:MI:SS')) ";
                query = session.createNativeQuery(strSql);
                query.executeUpdate();
            }

            tx.commit();
        } catch (Exception var20) {
            LOGGER.error(var20.toString());
            var20.printStackTrace();
        } finally {
            this.closeSession(session);
        }

    }

    public void addMonthlyPartition(String[] tables) {
        Session session = this.openSession();

        try {
            String fromPartitonValue =getPartitionValueOfCurrentMonth();
            String toPartitonValue = getPartitionValueOfNextMonth();
            Transaction tx = session.beginTransaction();
            String partitionName = null;
            String[] var9 = tables;
            int var10 = tables.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                String table = var9[var11];
                partitionName = tables+"_"+getPartitionValueOfCurrentMonth();
                String strSql = " CREATE TABLE " + partitionName + " PARTITION OF " + table + " FOR VALUES FROM (TO_TIMESTAMP('" + fromPartitonValue + "', 'YYYY-MM-DD HH24:MI:SS'))  TO (TO_TIMESTAMP('" + toPartitonValue + "', 'YYYY-MM-DD HH24:MI:SS')) ";
                Query query = session.createNativeQuery(strSql);
                query.executeUpdate();
            }

            tx.commit();
        } catch (Exception var16) {
            LOGGER.error(var16.toString());
        } finally {
            this.closeSession(session);
        }

    }

    private String getPartitionValueOfCurrentMonth(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-01 00:00:00");
        return simpleDateFormat.format(date);
    }
    private String getPartitionValueOfNextMonth(){
        Date date = new Date();
        Integer month = date.getMonth()+2;
        String monthCurr = String.valueOf(month);
        if(month < 10){
            monthCurr = "0" + String.valueOf(month);
        }
        return (date.getYear()+1900) +"-"+monthCurr+"-01 00:00:00";
    }


    public boolean deleteTableData(String[] tables, String[] columnTables, String oldStartTime) {
        Session session = this.openSession();

        try {
            Transaction tx = session.beginTransaction();
            int size = tables.length;
            String column = null;
            String table = null;
            int result = 0;

            for(int i = 0; i < size; ++i) {
                column = columnTables[i];
                table = tables[i];
                String strSql = " DELETE FROM " + table + " WHERE " + column + " <= (TO_TIMESTAMP('" + oldStartTime + "', 'YYYY-MM-DD HH24:MI:SS')) ";
                Query query = session.createNativeQuery(strSql);
                result = query.executeUpdate();
            }

            tx.commit();
            boolean var18 = result > 0;
            return var18;
        } catch (Exception var16) {
            LOGGER.error(var16.toString());
        } finally {
            this.closeSession(session);
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
