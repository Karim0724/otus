package ru.sharipov.dao.repository;

import org.springframework.data.repository.CrudRepository;
import ru.sharipov.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
