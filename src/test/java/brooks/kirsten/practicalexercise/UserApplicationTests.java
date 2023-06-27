package brooks.kirsten.practicalexercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import brooks.kirsten.practicalexercise.controller.UserController;
import brooks.kirsten.practicalexercise.model.User;
import brooks.kirsten.practicalexercise.repository.UserRepository;

@SpringBootTest
class UserApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserController userController;

	@AfterEach
	void teardown() {
		userRepository.deleteAll();
	}

	// Test user is successfully created
	@Test
	void testCreateNewUser() {
		User user = setupUser();
		ResponseEntity<Object> resp = userController.createUser(user);
		User userRetrieved = retrieveUser(user.getEmail());

		assertEquals(user, userRetrieved);
		assertEquals("The user was created successfully.", resp.getBody());
		assertEquals(HttpStatus.CREATED, resp.getStatusCode());
	}

	// Test user cannot be created if another user with the same email exists
	@Test
	void testCreateUserAlreadyExists() {
		// Initialise with existing user
		userController.createUser(setupUser());

		ResponseEntity<Object> resp = userController.createUser(setupUser());

		assertTrue(resp.getBody().toString().contains("User already exists with this email."));
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	// Test user cannot be created without an email (required)
	@Test
	void testCreateUserWithoutEmail() {
		User user = setupUser();
		user.setEmail(null);
		ResponseEntity<Object> resp = userController.createUser(user);

		assertTrue(resp.getBody().toString().contains("The given id must not be null"));
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	// Test a user cannot be created without a password (required)
	@Test
	void testCreateUserWithoutPassword() {
		User user = setupUser();
		user.setPassword(null);
		ResponseEntity<Object> resp = userController.createUser(user);

		assertTrue(resp.getBody().toString().contains("NULL not allowed for column \"USER_PASS\""));
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	// Test a user cannot be created with an invalid email
	// Needs more validation exception handling
	/*
		@Test
		void testCreateUserWithInvalidEmail() {
			User user = setupUser();
			user.setEmail("badEmail");

			ResponseEntity<Object> resp = userController.createUser(user);
		
			assertTrue(resp.getBody().toString().contains("must be a well-formed email address"));
			assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
		}
	*/

	// Test user cannot be null when creating
	@Test
	void testCreateUserNull() {
		ResponseEntity<Object> resp = userController.createUser(null);

		assertTrue(resp.getBody().toString().contains("\"user\" is null"));
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	// Test user is successfully retrieved
	@Test
	void testGetUser() {
		// Initialise with existing user
		User user = setupUser();
		userController.createUser(user);

		ResponseEntity<Object> resp = userController.getUser(user.getEmail());

		assertEquals(user, resp.getBody());
		assertEquals(HttpStatus.OK, resp.getStatusCode());
	}

	// Test error is returned if user doesn't exist
	@Test
	void testGetUserDoesntExist() {
		ResponseEntity<Object> resp = userController.getUser("minnie@example.com");

		assertTrue(resp.getBody().toString().contains("User not found."));
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
	}

	// Test error is returned if email is null
	@Test
	void testGetUserNull() {
		ResponseEntity<Object> resp = userController.getUser(null);

		assertTrue(resp.getBody().toString().contains("\"email\" is null"));
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	// Test user is returned regardless of email case
	@Test
	void testGetUserCaseInsensitive() {
		// Initialise with existing user
		User user = setupUser();
		userController.createUser(user);

		ResponseEntity<Object> resp = userController.getUser(user.getEmail().toUpperCase());

		assertEquals(user, resp.getBody());
		assertEquals(HttpStatus.OK, resp.getStatusCode());
	}

	// Test user is successfully updated
	@Test
	void testUpdateUser() {
		// Initialise with existing user
		User user = setupUser();
		userController.createUser(user);

		user.setPassword("NewPass");
		user.setFirstName("NewFirst");
		user.setLastName("NewLast");
		ResponseEntity<Object> resp = userController.updateUser(user.getEmail(), user);
		User userRetrieved = retrieveUser(user.getEmail());

		assertEquals(user, userRetrieved);
		assertEquals("The user was updated successfully.", resp.getBody());
		assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
	}

	// Test that a user's email cannot be updated
	@Test
	void testUpdateUserEmail() {
		// Initialise with existing user
		User user = setupUser();
		userController.createUser(user);

		String origEmail = user.getEmail();
		user.setEmail("minnie@example.com");
		ResponseEntity<Object> resp = userController.updateUser(origEmail, user);

		assertTrue(resp.getBody().toString().contains("An error occured when updating the user."));
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	// Test error is returned when user not found
	@Test
	void testUpdateUserDoesntExist() {
		User user = setupUser();

		ResponseEntity<Object> resp = userController.updateUser(user.getEmail(), user);

		assertTrue(resp.getBody().toString().contains("User not found."));
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
	}

	// Test password cannot be set to null
	@Test
	void testUpdateUserPasswordToNull() {
		// Initialise with existing user
		User user = setupUser();
		userController.createUser(user);

		user.setPassword(null);
		ResponseEntity<Object> resp = userController.updateUser(user.getEmail(), user);
	
		assertTrue(resp.getBody().toString().contains("NULL not allowed for column \"USER_PASS\""));
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	// Test error is returned if user is null
	@Test
	void testUpdateUserNull() {
		// Initialise with existing user
		User user = setupUser();
		userController.createUser(user);

		ResponseEntity<Object> resp = userController.updateUser(user.getEmail(), null);
	
		assertTrue(resp.getBody().toString().contains("\"user\" is null"));
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	// Test user is successfully deleted
	@Test
	void testDeleteUser() {
		// Initialise with existing user
		User user = setupUser();
		userController.createUser(user);

		ResponseEntity<Object> resp = userController.deleteUser(user.getEmail());
		User userRetrieved = retrieveUser(user.getEmail());

		assertNull(userRetrieved);
		assertTrue(resp.getBody().toString().contains("The user was deleted successfully."));
		assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
	}

	// Test error is returned if the user doesn't exist
	@Test
	void testDeleteUserDoesntExist() {
		ResponseEntity<Object> resp = userController.deleteUser(setupUser().getEmail());
	
		assertTrue(resp.getBody().toString().contains("User not found."));
		assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
	}

	// Test error is returned if email is null
	@Test
	void testDeleteUserNull() {
		ResponseEntity<Object> resp = userController.deleteUser(null);

		assertTrue(resp.getBody().toString().contains("The given id must not be null"));
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	// Initialise a user for testing
	private User setupUser() {
		return new User(
			"mickey@example.com",
			"Mickey123",
			"Mickey",
			"Mouse"
		);
	}

	// Get user from database
	private User retrieveUser(String email) {
		Optional<User> optUser = userRepository.findById(email);

		if (optUser.isPresent()) {
			return optUser.get();
		}
		return null;
	}

}
