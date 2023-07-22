package com.javastack.spring.finance.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.javastack.spring.finance.models.LoginUser;
import com.javastack.spring.finance.models.User;
import com.javastack.spring.finance.services.UserService;

@Controller
public class UserController {

	private final UserService userServ;
	
	public UserController(UserService userServ) {
		this.userServ = userServ;
	}
	
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("user", new User());
		return "/user/register.jsp";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session ) {
		userServ.register(user, result);
		if(result.hasErrors()) {
			return "/user/register.jsp";
		}

			session.setAttribute("userId", user.getId());
			return "redirect:/dashboard";
	}
	
	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("user", new LoginUser());
		return "/user/login.jsp";
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("user") LoginUser user, BindingResult result, HttpSession session) {
       User loginUser = userServ.login(user, result);
        
       if(result.hasErrors()) {
           return "/user/login.jsp";
       }
       
       session.setAttribute("userId", loginUser.getId());
       System.out.println(loginUser.getId());
       return "redirect:/dashboard";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}
