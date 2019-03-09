package com.workmate.server.model;

import com.workmate.server.model.enums.ENUM_active;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "company")
public class Company
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    private Long id;
    @Column(name = "name", unique=true)
    @NotEmpty(message = "*Please provide your name")
    private String name;
    @Column(name = "active")
    private ENUM_active active;
}
