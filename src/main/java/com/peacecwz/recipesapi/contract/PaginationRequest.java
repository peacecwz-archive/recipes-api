package com.peacecwz.recipesapi.contract;

import lombok.Data;

@Data
public class PaginationRequest {
    private Integer page = 1;
    private Integer size = 10;
}
