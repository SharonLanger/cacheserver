package com.example.cacheserver.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "NUMBERS")
@Data
@Accessors(chain = true)
public class Numbers {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    Integer id;

    @Column(name = "value")
    Integer value;

}
