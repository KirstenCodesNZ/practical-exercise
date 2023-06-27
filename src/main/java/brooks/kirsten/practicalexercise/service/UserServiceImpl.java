package brooks.kirsten.practicalexercise.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import brooks.kirsten.practicalexercise.model.User;
import brooks.kirsten.practicalexercise.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User createUser(User user) throws Exception {
		if (userRepository.existsById(user.getEmail())) {
			throw new Exception("User already exists with this email.");
		}

		return userRepository.save(new User(user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName()));
	}

	@Override
	public Optional<User> getUser(String email) {
		return userRepository.findById(email.toLowerCase());
	}

	@Override
	public User updateUser(String email, User user) throws Exception {
		if (!user.getEmail().toLowerCase().equals(email.toLowerCase())) {
			throw new Exception("An error occured when updating the user.");
		}

		if (userRepository.existsById(email.toLowerCase())) {
			return userRepository.save(user);
		}

		throw new Exception("User not found.");
	}

	@Override
	public void deleteUser(String email) throws Exception {
		if (userRepository.existsById(email)) {
			userRepository.deleteById(email);
		} else {
			throw new Exception("User not found.");
		}
	}
	
}
