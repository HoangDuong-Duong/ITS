/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.repository;

import com.elcom.its.management.dto.AggJobByStatus;
import com.elcom.its.management.dto.AggScheduledEvent;
import com.elcom.its.management.enums.JobStatus;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public class ReportScheduledEventCustomize {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportScheduledEventCustomize.class);

    private final SessionFactory sessionFactory;

    @Autowired
    public ReportScheduledEventCustomize(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
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

    public AggScheduledEvent getAggScheduledEvent(String userId, Date shiftStartDate, Date shiftEndDate, Date nextShiftStartDate, Date nextShiftEndDate) {
        Session session = openSession();
        try {
            String queryString = createQueryAggScheduledEvent(userId, shiftStartDate, shiftEndDate, nextShiftStartDate, nextShiftEndDate);
            Query query = session.createNativeQuery(queryString);
            List<Object[]> result = query.getResultList();
            Object[] item = result.get(0);
            return AggScheduledEvent.builder()
                    .numberOfHappenedEvent(Integer.parseInt(item[0].toString()))
                    .numberOfUpcomingEvent(Integer.parseInt(item[1].toString()))
                    .build();

        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return null;
    }

    private String createQueryAggScheduledEvent(String userId, Date shiftStartDate, Date shiftEndDate, Date nextShiftStartDate, Date nextShiftEndDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder queryString = new StringBuilder();
        queryString.append("select count(id) filter (where start_time >= '").append(dateFormat.format(shiftStartDate)).append("' and start_time <= '").append(dateFormat.format(shiftEndDate)).append("') as happened,");
        queryString.append("count(id) filter (where start_time >= '").append(dateFormat.format(nextShiftStartDate)).append("' and start_time <= '").append(dateFormat.format(nextShiftEndDate)).append("') as upcoming ");
        queryString.append("from scheduled_event ");
        queryString.append("where created_by = '").append(userId).append("' ");
        queryString.append("or users like '%").append(userId).append("%' ");;
        LOGGER.info("Query Get Agg scheduled event : {}", queryString.toString());
        return queryString.toString();
    }

    private Date getFirstDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

}
