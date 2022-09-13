package com.elcom.its.config.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class Pagination {
    Integer page = 0;
    Integer size = 20;
    String sort;
}
