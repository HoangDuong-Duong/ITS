package com.elcom.its.management.repository;

import com.elcom.its.management.dto.AggJobByStatus;
import com.elcom.its.management.dto.MapperJobStatusReport;
import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.service.impl.ITSCoreEventServiceImpl;
import com.elcom.its.utils.StringUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Query;

@Repository
public class ReportJobCustomize {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportJobCustomize.class);
    
    private final SessionFactory sessionFactory;
    
    @Autowired
    public ReportJobCustomize(EntityManagerFactory factory) {
        if (factory.unwrap(SessionFactory.class) == null) {
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }
    
    @Autowired
    private ITSCoreEventServiceImpl eventService;
    
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
    
    public List<MapperJobStatusReport> reportStatusByGroup(Date startDate, Date endDate, List<String> groupIds) {
        Session session = openSession();
        try {
            StringBuilder stringBuilder = new StringBuilder("");
            //List
            stringBuilder.append("SELECT j.group_id ,count(*) as total, status from job j where 1=1");
            if (groupIds != null && !groupIds.isEmpty()) {
                String listGroup = "";
                for (String tmp : groupIds) {
                    listGroup += "'" + tmp + "',";
                }
                if (listGroup.length() > 0) {
                    listGroup = listGroup.substring(0, listGroup.length() - 1);
                }
                stringBuilder.append(" AND group_id IN (").append(listGroup).append(") ");
            }
            if (startDate != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                stringBuilder.append(" AND (( start_time >='").append(df.format(startDate)).append("' ");
                stringBuilder.append(" AND start_time <='").append(df.format(endDate)).append("' ");
                stringBuilder.append(" ) OR (end_time IS NOT NULL and end_time <='").append(df.format(endDate)).append("'");
                stringBuilder.append(" and end_time >='").append(df.format(startDate)).append("'))");
//                stringBuilder.append(" AND end_time >='").append(df.format(startDate)).append("' )");
//                stringBuilder.append(") OR end_time IS NULL )");
            }
            
            stringBuilder.append(" group BY j.group_id ,status");
//            stringBuilder.append(" AND( source_id IN (").append(sourceIdList).append(") ");
            // Nếu ko truyền sortBy thì mặc định sort theo startTime DESC
            List<Object[]> result = session.createNativeQuery(stringBuilder.toString()).getResultList();
            List<MapperJobStatusReport> vos = null;
            vos = result.parallelStream().map(item -> {
                MapperJobStatusReport mapperEventByObjectTypeReport = new MapperJobStatusReport();
                mapperEventByObjectTypeReport.setObject(item[0] != null ? item[0].toString() : "");
                mapperEventByObjectTypeReport.setTotal(item[1] != null ? Integer.parseInt(item[1].toString()) : 0);
                mapperEventByObjectTypeReport.setStatus(item[2] != null ? Integer.parseInt(item[2].toString()) : 0);
                return mapperEventByObjectTypeReport;
            }).collect(Collectors.toList());
            
            return vos;
        } catch (Exception ex) {
            LOGGER.error("findAll().ex: " + ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public List<MapperJobStatusReport> reportJobGroup(Date startDate, Date endDate) {
        Session session = openSession();
        try {
            StringBuilder stringBuilder = new StringBuilder("");
            //List
            stringBuilder.append("SELECT j.job_type ,count(*) as total, status from job j where 1=1 ");
            if (startDate != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                stringBuilder.append(" AND (( start_time >='").append(df.format(startDate)).append("' ");
                stringBuilder.append(" AND start_time <='").append(df.format(endDate)).append("' ");
                stringBuilder.append(" ) OR (end_time IS NOT NULL and end_time <='").append(df.format(endDate)).append("'");
                stringBuilder.append(" AND end_time >='").append(df.format(startDate)).append("' ))");
//                stringBuilder.append(") OR end_time IS NULL )");
            }
            
            stringBuilder.append(" group BY j.job_type ,status");
//            stringBuilder.append(" AND( source_id IN (").append(sourceIdList).append(") ");
            // Nếu ko truyền sortBy thì mặc định sort theo startTime DESC
            List<Object[]> result = session.createNativeQuery(stringBuilder.toString()).getResultList();
            List<MapperJobStatusReport> vos = null;
            vos = result.parallelStream().map(item -> {
                MapperJobStatusReport mapperEventByObjectTypeReport = new MapperJobStatusReport();
                mapperEventByObjectTypeReport.setObject(item[0] != null ? item[0].toString() : "");
                mapperEventByObjectTypeReport.setTotal(item[1] != null ? Integer.parseInt(item[1].toString()) : 0);
                mapperEventByObjectTypeReport.setStatus(item[2] != null ? Integer.parseInt(item[2].toString()) : 0);
                return mapperEventByObjectTypeReport;
            }).collect(Collectors.toList());
            
            return vos;
        } catch (Exception ex) {
            LOGGER.error("findAll().ex: " + ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    public List<Job> getMyJob(Date startDate, Date endDate, String status, List<String> groupIds) {
        Session session = openSession();
        try {
            StringBuilder stringBuilder = new StringBuilder("");
            //List
            stringBuilder.append("select * "
                    + " FROM job j WHERE 1 = 1 ");
            
            if (startDate != null) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                stringBuilder.append(" AND (( start_time >='").append(df.format(startDate)).append("' ");
                stringBuilder.append(" AND start_time <='").append(df.format(endDate)).append("' ");
                stringBuilder.append(" ) OR (end_time IS NOT NULL and end_time <='").append(df.format(endDate)).append("'");
                stringBuilder.append(" AND end_time >='").append(df.format(startDate)).append("' ))");
            }
            
            if (groupIds != null && !groupIds.isEmpty()) {
                String listGroup = "";
                for (String tmp : groupIds) {
                    listGroup += "'" + tmp + "',";
                }
                if (listGroup.length() > 0) {
                    listGroup = listGroup.substring(0, listGroup.length() - 1);
                }
                stringBuilder.append(" AND group_id IN (").append(listGroup).append(") ");
            }
            
            if (!StringUtil.isNullOrEmpty(status)) {
                stringBuilder.append(" AND status in (").append(status).append(")");
            }
            
            stringBuilder.append(" ORDER BY j.start_time asc ");
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
    
    public List<AggJobByStatus> getAggJobByStatus(String groupId, Date startDate, Date endDate) {
        Session session = openSession();
        try {
            String queryString = createQueryGetAggJobByStatus(groupId, startDate, endDate);
            long start = System.currentTimeMillis();
            Query query = session.createNativeQuery(queryString);
            List<Object[]> result = query.getResultList();
            List<AggJobByStatus> vos = result.stream().map(item -> {
                AggJobByStatus aggJobByStatus = new AggJobByStatus();
                aggJobByStatus.setJobStatus(JobStatus.of(Integer.parseInt(item[0].toString())));
                aggJobByStatus.setInTime(Integer.parseInt(item[1].toString()));
                aggJobByStatus.setTotal(Integer.parseInt(item[2].toString()));
                return aggJobByStatus;
            }).collect(Collectors.toList());
            long end = System.currentTimeMillis();
            LOGGER.info("Query from Database getVehicleTrafficFlowReport: {}ms", end - start);
            return vos;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            ex.printStackTrace();
        } finally {
            closeSession(session);
        }
        return null;
    }
    
    private String createQueryGetAggJobByStatus(String groupId, Date startDate, Date endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder queryString = new StringBuilder();
        queryString.append("select status,");
        queryString.append("count(id) filter (where start_time >= '").append(dateFormat.format(startDate)).append("' and start_time <= '").append(dateFormat.format(endDate)).append("') as in_time,");
        queryString.append("count(id) filter (where start_time >= '").append(dateFormat.format(getFirstDateOfMonth(endDate))).append("' and start_time <= '").append(dateFormat.format(endDate)).append("') as total ");
        queryString.append("from job ");
        queryString.append("where group_id = '").append(groupId).append("' ");
        queryString.append("GROUP BY status order by status ASC");
        LOGGER.info("Query Get Agg job By Status : {}", queryString.toString());
        return queryString.toString();
    }
    
    private Date getFirstDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    
}
