/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model;

import com.elcom.its.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Admin
 */
public class VdsSpec {

    public static Specification<Vds> findVds(String siteId, String cameraId, String processUnitId,
            String keyword) {
        if (keyword != null) {
            keyword = "%" + keyword.toUpperCase().trim().replace("_", "\\_").replace("%", "\\%") + "%";
        }
        final String finalKeyword = keyword;

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //siteId
            if (!StringUtil.isNullOrEmpty(siteId)) {
                predicates.add(criteriaBuilder.equal(root.get("siteId"), siteId));
            }

            //cameraId
            if (!StringUtil.isNullOrEmpty(cameraId)) {
                predicates.add(criteriaBuilder.equal(root.get("cameraId"), cameraId));
            }

            //processUnitId
            if (!StringUtil.isNullOrEmpty(processUnitId)) {
                predicates.add(criteriaBuilder.equal(root.get("processUnitId"), processUnitId));
            }

            //keyword
            if (!StringUtil.isNullOrEmpty(finalKeyword)) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("vdsName")), finalKeyword),
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("vdsKey")), finalKeyword)
                    )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
    
    public static Specification<Vds> findVds(List<String> siteIdList, String cameraId, String processUnitId,
            String keyword) {
        if (keyword != null) {
            keyword = "%" + keyword.toUpperCase().trim().replace("_", "\\_").replace("%", "\\%") + "%";
        }
        final String finalKeyword = keyword;

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //siteIdList
            if (siteIdList != null && !siteIdList.isEmpty()) {
                predicates.add(root.get("siteId").in(siteIdList));
            }

            //cameraId
            if (!StringUtil.isNullOrEmpty(cameraId)) {
                predicates.add(criteriaBuilder.equal(root.get("cameraId"), cameraId));
            }

            //processUnitId
            if (!StringUtil.isNullOrEmpty(processUnitId)) {
                predicates.add(criteriaBuilder.equal(root.get("processUnitId"), processUnitId));
            }

            //keyword
            if (!StringUtil.isNullOrEmpty(finalKeyword)) {
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("vdsName")), finalKeyword),
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("vdsKey")), finalKeyword)
                    )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
