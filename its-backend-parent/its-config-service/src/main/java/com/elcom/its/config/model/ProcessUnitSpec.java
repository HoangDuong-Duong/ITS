/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.model;

import com.elcom.its.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Admin
 */
public class ProcessUnitSpec {

    public static Specification<ProcessUnit> findProcessUnit(Integer puType, String keyword) {
        if (keyword != null) {
            keyword = "%" + keyword.toUpperCase().trim().replace("_", "\\_").replace("%", "\\%") + "%";
        }
        final String finalKeyword = keyword;

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //puType
            if (puType != null) {
                predicates.add(criteriaBuilder.equal(root.get("puType"), puType));
            }

            //keyword
            if (!StringUtil.isNullOrEmpty(finalKeyword)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + finalKeyword + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
