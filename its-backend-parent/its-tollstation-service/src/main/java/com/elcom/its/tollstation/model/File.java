/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.model;

import lombok.Data;

/**
 *
 * @author Admin
 */
@Data
public class File {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
