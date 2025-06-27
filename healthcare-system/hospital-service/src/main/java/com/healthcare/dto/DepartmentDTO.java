package com.healthcare.dto;

import lombok.Data;

@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private Long hospitalId;
    private String description;
    private String headDoctor;
    private Integer numberOfDoctors;
    private Integer numberOfStaff;
    private String specializations;
    private String facilities;
    private String contactNumber;
    private String email;
    private String location;
    private Boolean isActive;
    
    // Additional fields for nested data
    private HospitalDTO hospital;
}
