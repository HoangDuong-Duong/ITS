/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.enums;

/**
 *
 * @author Admin
 */
public enum Priority {
    LOW_IMPORTANCE(0, "Độ quan trọng thấp"),
    AVERAGE_IMPORTANCE(1, "Độ quan trọng trung bình"),
    HIGH_IMPORTANCE(2, "Độ quan trọng cao");
  

    private int code;
    private String description;

    public int code() {
        return code;
    }
    public String description() {
        return description;
    }

    Priority(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Priority of(String type){
        switch (type) {
            case "LOW_IMPORTANCE":
                return Priority.LOW_IMPORTANCE;
            case "AVERAGE_IMPORTANCE":
                return Priority.AVERAGE_IMPORTANCE;
            case "HIGH_IMPORTANCE":
                return Priority.HIGH_IMPORTANCE;
            default:
                return Priority.AVERAGE_IMPORTANCE;
        }
    }

}
