package com.workmate.server.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin")
public class AdminController
{

    @GetMapping(path="/hello", produces = "application/json")
    public boolean helloAdmin()
    {
        return true;
    }
}
