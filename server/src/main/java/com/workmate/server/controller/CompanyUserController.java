package com.workmate.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(path = "/company/user")
public class CompanyUserController
{
    @GetMapping(path="/hello", produces = "application/json")
    public boolean helloAdmin(Principal principal) {
        return true;
    }
}
