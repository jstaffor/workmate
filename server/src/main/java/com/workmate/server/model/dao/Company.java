package com.workmate.server.model.dao;

import com.workmate.server.model.enums.ENUM_active;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

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
    @Column(name = "createdAt")
    private Date createdAt;
    @Column(name = "updatedAt")
    private Date updatedAt;



    @PrePersist
    void createdAt()
    {
        this.createdAt = this.updatedAt = new Date();
    }
    @PreUpdate
    void updatedAt()
    {
        this.updatedAt = new Date();
    }
}
