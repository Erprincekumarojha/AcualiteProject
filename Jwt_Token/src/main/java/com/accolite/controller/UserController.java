package com.accolite.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.entity.User;
import com.accolite.repository.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("getUser/{userid}")
	public ResponseEntity<Object> getUser(@PathVariable("userid") String userid) {
		Optional<User> user = userRepository.findByUserid(userid);
		if (user.isPresent()) {
			return new ResponseEntity<Object>(user.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>("User not found", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("addUser")
	public ResponseEntity<?> addUser(@RequestBody User userDetails) {
		ResponseEntity<Object> response = null;
		User user = new User();
		user.setUserid(userDetails.getUserid());
		user.setUseremail(userDetails.getUseremail());
		user.setUsername(userDetails.getUsername());
		user.setPassword(userDetails.getPassword());

		if (userDetails.getUserid().length() < 3) {
			response = new ResponseEntity<Object>("User not created", HttpStatus.NOT_ACCEPTABLE);
			return response;
		}
		User userSaved = userRepository.save(user);
		if (userSaved != null) {
			response = new ResponseEntity<Object>(userSaved.getUserid() + " User is created successfully",
					HttpStatus.OK);
		}
		return response;

	}

	@PutMapping("updateUser")
	public ResponseEntity<?> updateUser(@RequestBody User updateUser) {
		ResponseEntity<Object> response = null;
		String userId = updateUser.getUserid();
		if (updateUser.getUserid().length() < 3) {
			response = new ResponseEntity<Object>("User not updated", HttpStatus.NOT_MODIFIED);
			return response;
		}
		Optional<User> user = userRepository.findByUserid(userId);
		if (user.isPresent()) {
			User currentUser = user.get();
			currentUser.setUserid(updateUser.getUserid());
			currentUser.setUseremail(updateUser.getUseremail());
			currentUser.setUsername(updateUser.getUsername());
			currentUser.setPassword(updateUser.getPassword());
			User userUpdated = userRepository.save(currentUser);
			if (null != userUpdated)
				response = new ResponseEntity<Object>(userUpdated.getUserid() + "user details is updated",
						HttpStatus.OK);

		} else {
			User newUser = new User();
			newUser.setUserid(updateUser.getUserid());
			newUser.setUseremail(updateUser.getUseremail());
			newUser.setUsername(updateUser.getUsername());
			newUser.setPassword(updateUser.getPassword());
			User userSaved = userRepository.save(newUser);
			if (null != userSaved)
				response = new ResponseEntity<Object>(userSaved.getUserid() + " created", HttpStatus.CREATED);

		}
		return response;
	}

	@DeleteMapping("deleteUser/{userid}")
	public ResponseEntity<Object> deleteUser(@PathVariable("userid") String userid) {
		try {
			userRepository.deleteByUserid(userid);
			return new ResponseEntity<Object>(userid + " deleted", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(userid + "not found", HttpStatus.NOT_FOUND);
		}
	}

}
