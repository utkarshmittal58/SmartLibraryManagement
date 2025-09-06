package com.boot.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.boot.Entites.User;
import com.boot.Helper.Message;
import com.boot.dao.UserRepository;

@Controller
public class HomeController {
   
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@RequestMapping("/")
	public String Home(Model m)
	{
	    m.addAttribute("title","Home - Smart Library Management");
		return "Home";
	}
	
	@RequestMapping("/about")
	public String about(Model m)
	{
	    m.addAttribute("title","About - Smart Library Management");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signUp(Model m)
	{
	    m.addAttribute("title","Sign Up - Smart Library Management");
	    m.addAttribute("user",new User());
		return "signup";
	}
	
	@RequestMapping("/custom_login")
	public String login(Model m)
	{
	    m.addAttribute("title","Login - Smart Library Management");
		return "login";
	}
	
	//	User Registering Handler
	
	@PostMapping("/register")
	public String signUpHandler(@Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value="agree",defaultValue = "false") boolean agreement, Model m,HttpSession session)
	{
	
		try {
			
			if(!agreement)
			{
				throw new Exception("You Have Not Accepted Terms and Conditions");
			}
			
			if(result.hasErrors())
			{
				m.addAttribute("user",user);
				return "signup";
			}
			m.addAttribute("title","Sign Up - Smart Library Management");
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			
			userRepo.save(user);
			
			m.addAttribute("user",new User());
			
			session.setAttribute("message",new Message("Successfully Registered","alert-success	"));
			System.out.println(user);
			return "signup";
			
		} catch (Exception e) {
			m.addAttribute("title","Sign Up - Smart Library Management");
			e.printStackTrace();
			m.addAttribute("user",user);
			session.setAttribute("message",new Message("Something Went Wrong!!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	}
}
