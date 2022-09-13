package com.elcom.its.management.repository;

import com.elcom.its.management.model.Job;
import com.elcom.its.management.model.JobBackup;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface JobBackupRepository  extends PagingAndSortingRepository<JobBackup, String> {
}
