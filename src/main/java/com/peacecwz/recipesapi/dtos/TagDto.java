package com.peacecwz.recipesapi.dtos;

import com.peacecwz.recipesapi.data.TagEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagDto {
    private long id;
    private String name;

    public static TagDto buildDto(TagEntity tag) {
       return TagDto.builder()
               .id(tag.getId())
               .name(tag.getName())
               .build();
    }
}
