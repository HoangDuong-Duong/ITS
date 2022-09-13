/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.elcom.its.management.repository;

import com.elcom.its.management.enums.ScheduledEventStatus;
import com.elcom.its.management.model.ScheduledEvent;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Acer
 */
@Repository
public interface ScheduledEventRepository extends PagingAndSortingRepository<ScheduledEvent, String> {

    @Override
    List<ScheduledEvent> findAll();

    @Query("select se from ScheduledEvent se where ( se.title like :keyword or se.description like :keyword or se.siteName like :keyword or se.eventType like :keyword) and se.startTime between :startTime and :endTime ")
    List<ScheduledEvent> findByStartTimeBetween(Date startTime, Date endTime, String keyword);

    @Query("select se from ScheduledEvent se where ( se.title like :keyword or se.description like :keyword or se.siteName like :keyword or se.eventType like :keyword)"
            + "and ((se.groupId = :groupId and se.users is null) or (se.users like :userId) or (se.createdBy like :userId)) and se.startTime between :startTime and :endTime ")
    List<ScheduledEvent> getListScheduledEventForUser(Date startTime, Date endTime, String groupId, String userId, String keyword);

    ScheduledEvent findByIdAndStartTime(String id, Date startTime);

    List<ScheduledEvent> findByStatusInAndStartTimeBetween(List<ScheduledEventStatus> listStatus, Date startTime, Date endTime);
}
