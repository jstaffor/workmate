package com.workmate.server.controller;

import com.workmate.server.model.User;
import com.workmate.server.service.SetupService;
import com.workmate.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
public class UserController
{

	@Autowired
	private UserService userService;
	@Autowired
	private SetupService setupService;


	@RequestMapping(value = "/helloAdmin", method = RequestMethod.GET)
	public boolean helloAdmin() {
		return true;
	}

	@RequestMapping(value = "/helloCompanyUser", method = RequestMethod.GET)
	public boolean helloCompanyUser() {
		return true;
	}

	@RequestMapping(value = "/helloCompanyAdmin", method = RequestMethod.GET)
	public boolean helloCompanyAdmin() {
		return true;
	}

	@RequestMapping("/login")
	public boolean login(@RequestBody User user)
	{
		return user.getEmail().equals("killesk@gmail.com") && user.getPassword().equals("m123");
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Principal user(Principal user)
	{
		return user;
	}
}
