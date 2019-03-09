package com.workmate.server.service;


import com.workmate.server.model.Company;
import com.workmate.server.model.Role;
import com.workmate.server.model.User;
import com.workmate.server.model.enums.ENUM_active;
import com.workmate.server.model.enums.ENUM_role;
import com.workmate.server.repository.CompanyRepository;
import com.workmate.server.repository.RoleRepository;
import com.workmate.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service("userService")
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       CompanyRepository companyRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Company saveCompany(String name, ENUM_active active)
    {
        Company company = new Company();
        company.setName(name);
        company.setActive(active);
        return companyRepository.save(company);
    }

    public Company findCompanyByName(String name)
    {
        return companyRepository.findByName(name);
    }

    public User saveUser(String email, String password, String name, String lastName, ENUM_active active,
                         Set<ENUM_role> enumRoles, Company company) {
        if(enumRoles == null || enumRoles.isEmpty())
        {
            return null;
        }
        if(company == null)
        {
            return null;
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setName(name);
        user.setActive(active.getValue());
        user.setLastName(lastName);Set roles = new HashSet<>();
        for(ENUM_role r : enumRoles)
        {
            roles.add(roleRepository.findByRole(r.toString()));
        }
        user.setRoles(roles);
        user.setCompany(company);
        return userRepository.save(user);
    }



}