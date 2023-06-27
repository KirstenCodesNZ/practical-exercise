package brooks.kirsten.practicalexercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import brooks.kirsten.practicalexercise.model.User;

public interface UserRepository extends JpaRepository<User, String> {
	
}
