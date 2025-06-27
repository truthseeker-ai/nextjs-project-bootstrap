package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "hospitals")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
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
    private Boolean isActive = true;
}
