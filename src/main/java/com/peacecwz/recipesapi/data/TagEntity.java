package com.peacecwz.recipesapi.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tags_id_seq")
    private long id;
    private String name;
}
