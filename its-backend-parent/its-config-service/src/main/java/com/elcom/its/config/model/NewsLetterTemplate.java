package com.elcom.its.config.model;

import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewsLetterTemplate {
    private String id;

    private String name;

    private String typeBoard;

    private String content;

    private String createdBy;

    private Date createDate;
}
