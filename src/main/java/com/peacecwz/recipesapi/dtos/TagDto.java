package com.peacecwz.recipesapi.dtos;

import com.peacecwz.recipesapi.data.TagEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
