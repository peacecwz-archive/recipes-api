package com.peacecwz.recipesapi.contract;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
public class PaginationResponse<T> {
    private Integer page;
    private Integer size;
    private Integer total;
    private T data;
}
