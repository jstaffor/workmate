package com.workmate.server.controller;

import com.workmate.server.model.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
public class UserController {

	@RequestMapping("/login")
	public boolean login(@RequestBody User user) {
		return user.getEmail().equals("killesk@gmail.com") && user.getPassword().equals("m123");
	}


//	@RequestMapping("/user")
//	public Principal user(HttpServletRequest request) {
//		String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
//		return () -> new String(Base64.getDecoder().decode(authToken)).split(":")[0];
//	}

//	@RequestMapping(value = "/user/{name}", method = RequestMethod.GET)
//	@ResponseBody
//	public boolean user(@PathVariable("name") String name){//}, HttpServletRequest request) {
////		String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
//		return true;
//	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Principal user(Principal user) {
		return user;
	}
}
