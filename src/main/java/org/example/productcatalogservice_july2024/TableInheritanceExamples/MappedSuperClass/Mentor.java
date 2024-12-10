package org.example.productcatalogservice_july2024.TableInheritanceExamples.MappedSuperClass;

import jakarta.persistence.Entity;

@Entity(name="msc_mentor")
public class Mentor extends User {
    private Integer numberOfHours;



}

