package com.workmate.server.service;

import com.workmate.server.model.Company;
import com.workmate.server.model.enums.ENUM_active;
import com.workmate.server.model.enums.ENUM_role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;

@Service("setupService")
public class SetupService
{
    public static final String commonPassword = "123";
    public static final String adminCompanyName = "admin";
    public static final String adminUserEmail = "admin@gmail.com";
    public static final String companyName = "TomMcgoo";
    public static final String companyAdminEmail = "companyadmin@gmail.com";
    public static final String companyUserEmail = "companyuser@gmail.com";

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init()
    {
        setUpUsersAndCompanies();
    }

    private void setUpUsersAndCompanies()
    {
        if(userService.findCompanyByName(adminCompanyName) == null)
        {
            Company adminCompany = userService.saveCompany(adminCompanyName, ENUM_active.ACTIVE);
            userService.saveUser(adminUserEmail, commonPassword, "name", "name",
                    ENUM_active.ACTIVE, new HashSet<>(Arrays.asList(ENUM_role.ADMIN)), adminCompany);

        }

        if(userService.findCompanyByName(companyName) == null)
        {
            Company normalCompany = userService.saveCompany(companyName, ENUM_active.ACTIVE);
            userService.saveUser(companyAdminEmail, commonPassword, "name", "name", ENUM_active.ACTIVE,
                    new HashSet<>(Arrays.asList(ENUM_role.COMPANY_ADMIN)), normalCompany);
            userService.saveUser(companyUserEmail, commonPassword, "name", "name", ENUM_active.ACTIVE,
                    new HashSet<>(Arrays.asList(ENUM_role.COMPANY_USER)), normalCompany);
        }
    }
}
