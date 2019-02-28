package com.workmate.server.service;

import com.workmate.server.model.Company;
import com.workmate.server.model.User;
import com.workmate.server.model.enums.ENUM_active;
import com.workmate.server.model.enums.ENUM_role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service("setupService")
public class SetupService
{
    @Autowired
    private UserService userService;

    public void setUpUsersAndCompanies() {
        Company adminCompany = userService.saveCompany("admin", ENUM_active.ACTIVE);
        Company normalCompany = userService.saveCompany("TomMcgoo", ENUM_active.ACTIVE);
        userService.saveUser("killesk@gmail.com", "m123", "Paul", "Stafford",
                ENUM_active.ACTIVE, new HashSet<>(Arrays.asList(ENUM_role.ADMIN)), adminCompany);
        userService.saveUser("companyuser@gmail.com", "m123", "John", "Stafford",
                ENUM_active.ACTIVE, new HashSet<>(Arrays.asList(ENUM_role.COMPANY_USER)), normalCompany);
        userService.saveUser("companyuser@gmail.com", "m123", "Chris", "Stafford",
                ENUM_active.ACTIVE, new HashSet<>(Arrays.asList(ENUM_role.COMPANY_ADMIN)), normalCompany);
    }
}
