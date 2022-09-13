package com.elcom.its.management.repository;

import com.elcom.its.management.model.Job;
import com.elcom.its.utils.StringUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class JobCustomize {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobCustomize.class);

    private final SessionFactory sessionFactory;

    @Autowired
    public JobCustomize(EntityManagerFactory factory) {
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


    public List<Job> getMyJob( Date startDate, Date endDate,Date expireStart, Date expireEnd , List<String> jobType, Integer priority, Integer status, List<String> siteIds, String groupId, String uuid) {
        Session session = openSession();
        try {
            StringBuilder stringBuilder = new StringBuilder("");
            //List
            stringBuilder.append("select * "
                    + " FROM job j WHERE 1 = 1 ");

            stringBuilder.append(" and ( (group_id = '");
            stringBuilder.append(groupId);
            stringBuilder.append("' AND( user_ids is null or user_ids ='') ) OR (user_ids LIKE ");
            String uuids = "'%" +uuid + "%'";
            stringBuilder.append(uuids);
            stringBuilder.append("))");
            if (startDate != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                stringBuilder.append(" AND start_time >='").append(df.format(startDate)).append("' ");
                stringBuilder.append(" AND start_time <='").append(df.format(endDate)).append("' ");
            }

            if (expireStart != null && expireEnd != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                stringBuilder.append(" AND(  (end_time IS NOT NULL and end_time <='").append(df.format(expireEnd)).append("'");
                stringBuilder.append(" AND end_time >='").append(df.format(expireStart)).append("' )");
                stringBuilder.append(" OR end_time IS NULL )");
            }

            if (status!=-1) {
                stringBuilder.append(" AND status = ").append( status);
            }

            if (priority!=-1) {
                stringBuilder.append(" AND priority = ").append( priority);
            }
            String jobTmp="";
            if (jobType!=null && !jobType.isEmpty()) {
                String listType = "";
                for (String tmp : jobType) {
                    listType += "'" + tmp + "',";
                }
                if (listType.length() > 0) {
                    jobTmp = listType.substring(0, listType.length() - 1);
                }
                stringBuilder.append(" AND job_type IN (").append(jobTmp).append(") ");
            }

            String siteTmp = "";
            if(siteIds!=null && !siteIds.isEmpty()) {
                for (String tmp : siteIds) {
                    siteTmp += "'" + tmp + "',";
                }
                if (siteTmp.length() > 0) {
                    siteTmp = siteTmp.substring(0, siteTmp.length() - 1);
                    stringBuilder.append(" AND( start_site_id IN (").append(siteTmp).append(") ");
                    stringBuilder.append(" OR end_site_id IN (").append(siteTmp).append(") ) ");
                }
            }
            stringBuilder.append(" ORDER BY j.priority DESC,j.status asc ,j.start_time asc ");
//            stringBuilder.append(" AND( source_id IN (").append(sourceIdList).append(") ");
            // Nếu ko truyền sortBy thì mặc định sort theo startTime DESC
            List<Job> result = session.createNativeQuery(stringBuilder.toString(), Job.class).getResultList();

            return result;
        } catch (Exception ex) {
            LOGGER.error("findAll().ex: " + ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return null;
    }

    public List<Job> getJobForReport( Date startDate, Date endDate) {
        Session session = openSession();
        try {
            StringBuilder stringBuilder = new StringBuilder("");
            //List
            stringBuilder.append("select * "
                    + " FROM job j WHERE 1 = 1 ");
            if (startDate != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                stringBuilder.append(" AND start_time >='").append(df.format(startDate)).append("' ");
                stringBuilder.append(" AND start_time <='").append(df.format(endDate)).append("' ");
            }
            stringBuilder.append(" ORDER BY j.priority DESC,j.status asc ,j.start_time asc ");
            List<Job> result = session.createNativeQuery(stringBuilder.toString(), Job.class).getResultList();

            return result;
        } catch (Exception ex) {
            LOGGER.error("findAll().ex: " + ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return null;
    }

}
