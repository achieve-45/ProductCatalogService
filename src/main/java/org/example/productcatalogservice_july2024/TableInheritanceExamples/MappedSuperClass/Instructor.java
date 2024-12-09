package org.example.productcatalogservice_july2024.TableInheritanceExamples.MappedSuperClass;


import jakarta.persistence.Entity;

@Entity(name="msc_instructor")
public class Instructor extends User {
    private String company;
}
