package com.javastack.spring.finance.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.javastack.spring.finance.models.LoginUser;
import com.javastack.spring.finance.models.User;
import com.javastack.spring.finance.repositories.UserRepo;

@Service
public class UserService {

	private final UserRepo userRepo;

	public UserService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	public User register(User newUser, BindingResult result) {

		Optional<User> potentialUser = userRepo.findByEmail(newUser.getEmail());

    	// Reject if email is taken (present in database)
    	if(potentialUser.isPresent()) {
    		result.rejectValue("email", "Matches", "An account with that email already exists!");
    	}

        // Reject if password doesn't match confirmation
    	if(!newUser.getPassword().equals(newUser.getConfirm())) {
    		result.rejectValue("confirm", "Matches", "The Confirm Password must match Password!");
    	}

    	// Return null if result has errors
    	if(result.hasErrors()) {
    		return null;
    	}

        // Hash and set password, save user to database
    	String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
    	newUser.setPassword(hashed);
    	return userRepo.save(newUser);

	}

	 public User login(LoginUser newLogin, BindingResult result) {
	        // TO-DO: Additional validations!
	    	if(result.hasErrors()) {
	    		return null;
	    	}
	    	User existingUser = getUser(newLogin.getEmail());
	    	if(existingUser == null) {
	    		result.rejectValue("email", "Unique", "Invalid Login");
	    		return null;
	    	}
	    	if(!BCrypt.checkpw(newLogin.getPassword(), existingUser.getPassword())) {
	    		result.rejectValue("password", "Matches", "Invalid Login");
	    		return null;
	    	}

	    	    // Exit the method and go back to the controller
	    	    // to handle the response

	        return existingUser;
	    }

	 public User create(User user) {
		 return userRepo.save(user);
	 }
	 
	public List<User> allUsers(){
		return userRepo.findAll();
	}

	public User updateUser(User user) {
		return userRepo.save(user);
	}

	public User findById(Long id) {
		Optional<User> optionalUser = userRepo.findById(id);
		if(optionalUser.isPresent()) {
			return optionalUser.get();
		}else {
			return null;
		}
	}
    		public User getUser(String email) {
			Optional<User> potentialUser = userRepo.findByEmail(email);
			return potentialUser.isPresent() ? potentialUser.get() : null;
		}

		public User getUser(Long id) {
			Optional<User> potentialUser = userRepo.findById(id);
			return potentialUser.isPresent() ? potentialUser.get() : null;
		}
}
