package com.healthcare.dto;

import lombok.Data;

@Data
public class HospitalDTO {
    private Long id;
    private String name;
    private String address;
    private String contactNumber;
    private String email;
    private String website;
    private String description;
    private String licenseNumber;
    private Integer numberOfDepartments;
    private Integer numberOfDoctors;
    private Integer numberOfStaff;
    private String facilities;
    private String specializations;
    private String emergencyContact;
    private String operatingHours;
    private Boolean isActive;
}
