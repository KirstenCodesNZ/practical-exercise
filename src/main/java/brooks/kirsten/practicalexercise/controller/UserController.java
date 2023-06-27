package brooks.kirsten.practicalexercise.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import brooks.kirsten.practicalexercise.model.User;
import brooks.kirsten.practicalexercise.repository.UserRepository;
import brooks.kirsten.practicalexercise.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

// Needs more validation exception handling for entity validation
@Validated
@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private UserService userService;

	// CREATE
	@PostMapping(value = "/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		try {
			userService.createUser(user);
			return new ResponseEntity<>("The user was created successfully.", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// READ
	@GetMapping(value = "/users/{email}")
	public ResponseEntity<Object> getUser(@Email @PathVariable("email") String email) {
		try {
			Optional<User> userData = userService.getUser(email);
			return userData.isPresent()
				? new ResponseEntity<>(userData.get(), HttpStatus.OK)
				: new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}

	// UPDATE
	@PutMapping(value = "/users/{email}")
	public ResponseEntity<Object> updateUser(@Email @PathVariable("email") String email, @Valid @RequestBody User user) {
		try {
			userService.updateUser(email, user);
			return new ResponseEntity<>("The user was updated successfully.", HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return e.getMessage().equals("User not found.")
				? new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	// DELETE
	@DeleteMapping(value = "/users/{email}")
	public ResponseEntity<Object> deleteUser(@Email @PathVariable("email") String email) {
		try {
			userService.deleteUser(email);
			return new ResponseEntity<>("The user was deleted successfully.", HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return e.getMessage().equals("User not found.")
				? new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
