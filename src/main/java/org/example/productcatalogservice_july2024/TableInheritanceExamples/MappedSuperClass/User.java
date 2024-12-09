package org.example.productcatalogservice_july2024.TableInheritanceExamples.MappedSuperClass;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class User {
    @Id
    private Long id;

    private String name;

}
