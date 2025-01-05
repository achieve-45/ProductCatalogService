package org.example.productcatalogservice_july2024.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {
    private String query;
    private Integer pageList;
    private Integer pageNumber;
}
