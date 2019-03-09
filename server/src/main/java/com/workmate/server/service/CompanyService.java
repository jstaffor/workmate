package com.workmate.server.service;


import com.workmate.server.model.Company;
import com.workmate.server.repository.CompanyRepository;
import com.workmate.server.repository.RoleRepository;
import com.workmate.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("companyService")
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }


    public List<Company> getAllCompanies()
    {
        return companyRepository.findAll();
    }

    public boolean isCompanyExist(Company company) {
        Company companyFromDatabase = companyRepository.findByName(company.getName());
        if(companyFromDatabase != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Company save(Company company)
    {
        return companyRepository.save(company);
    }

    public Company findById(Long id)
    {
        return companyRepository.findById(id).get();
    }

    public void deleteCompanyById(Long id)
    {
        companyRepository.deleteById(id);
    }


}