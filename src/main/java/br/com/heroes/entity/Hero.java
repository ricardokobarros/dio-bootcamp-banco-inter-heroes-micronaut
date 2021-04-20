package br.com.heroes.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hero {

    private Long id;
    
    private String name;

    private String universe;

    private int films;

}
