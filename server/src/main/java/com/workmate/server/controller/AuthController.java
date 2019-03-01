package com.workmate.server.controller;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AuthController
{
	@CrossOrigin()
	@RequestMapping(value = "/auth/user", method = RequestMethod.GET)
	public Principal user(Principal user)
	{
		return user;
	}
}
