package com.workmate.server.controller;

import com.workmate.server.model.Company;
import com.workmate.server.repository.CompanyRepository;
import com.workmate.server.repository.RoleRepository;
import com.workmate.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
public class AdminController
{

    private final CompanyRepository companyRepository;

    @Autowired
    public AdminController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping(path="/hello", produces = "application/json")
    public boolean helloAdmin()
    {
        return true;
    }

    @RequestMapping(value = "/getCompanies", method = RequestMethod.GET)
    public List<Company> getAllCompanies()
    {
        return companyRepository.findAll();
    }

}
