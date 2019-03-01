package com.workmate.server.controller;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "/admin")
public class AdminController
{

    @GetMapping(path="/hello", produces = "application/json")
    public boolean helloAdmin() {
        return true;
    }
}
