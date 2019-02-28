package com.workmate.server.repository;

import com.workmate.server.model.Company;
import com.workmate.server.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("companyRepository")
public interface CompanyRepository extends JpaRepository<Company, Integer>
{
    Company findByName(String name);
}