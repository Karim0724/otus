package ru.sharipov.services;

import ru.sharipov.model.User;

import java.util.Optional;

public interface DbUserService {
    Optional<User> findById(long id);
    User save(User user);
    Optional<User> findByLogin(String login);
}
