package com.healthcare.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    private String description;
    private String headDoctor;
    private Integer numberOfDoctors;
    private Integer numberOfStaff;
    private String specializations;
    private String facilities;
    private String contactNumber;
    private String email;
    private String location;
    private Boolean isActive = true;
}
