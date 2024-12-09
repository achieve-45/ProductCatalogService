package org.example.productcatalogservice_july2024.TableInheritanceExamples.TablesPerClass;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity(name="tpc_ta")
public class Ta extends User {
    private Double ratings;
}
