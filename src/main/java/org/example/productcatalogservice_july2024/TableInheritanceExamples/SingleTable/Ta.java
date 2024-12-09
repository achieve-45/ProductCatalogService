package org.example.productcatalogservice_july2024.TableInheritanceExamples.SingleTable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name="st_ta")
@DiscriminatorValue("1")
public class Ta extends User {
    private Double ratings;
}
