package com.doll.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeReturnDto  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jwt;

    private Long userId;

    private String role;



}
