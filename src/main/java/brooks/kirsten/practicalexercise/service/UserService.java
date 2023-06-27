package brooks.kirsten.practicalexercise.service;

import java.util.Optional;

import brooks.kirsten.practicalexercise.model.User;

public interface UserService {
	
	User createUser(User user) throws Exception;

	Optional<User> getUser(String email);

	User updateUser(String email, User user) throws Exception;

	void deleteUser(String email) throws Exception;

}
