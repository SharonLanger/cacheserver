package com.example.cacheserver.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "NUMBERS")
@Data
@Accessors(chain = true)
public class NumberObj {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    long id;

    @Column(name = "value")
    long value;

}
