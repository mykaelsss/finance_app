package com.javastack.spring.finance.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javastack.spring.finance.models.LoginUser;
import com.javastack.spring.finance.models.User;
import com.javastack.spring.finance.services.UserService;

@RestController
@CrossOrigin(origins="http://127.0.0.1:5173/")
public class UserRestController {
	
	private final UserService userServ;
	
	public UserRestController(UserService userServ) {
		this.userServ = userServ;
	}
	
	@GetMapping("/api")
	public List<User> allUsers(){
		return userServ.allUsers();
	}
	
	@GetMapping("/api/{id}")
	public User oneUser(@PathVariable("id") Long id) {
		User user = userServ.findById(id);
		return user;
	}
	
	@PostMapping("/api/register")
	public User registerUser(@Valid @RequestBody User user,BindingResult result ) {
		if(result.hasErrors()) {
			List<ObjectError> errors = result.getAllErrors();
		}
		return userServ.register(user,result);
	}
	
	@PostMapping("/api/login")
	public User loginUser(@Valid LoginUser user,BindingResult result) {
		User userLogin = userServ.login(user, result);
		System.out.println(user.getEmail());
		if(result.hasErrors()) {
			return null;
		}
		return userLogin;
	}
}
