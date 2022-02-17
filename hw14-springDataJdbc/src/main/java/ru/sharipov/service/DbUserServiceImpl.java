package ru.sharipov.service;

import org.springframework.stereotype.Service;
import ru.sharipov.dao.repository.UserRepository;
import ru.sharipov.dao.sessionmanager.TransactionManager;
import ru.sharipov.model.User;

import java.util.Optional;

@Service
public class DbUserServiceImpl implements DbUserService {
    private final UserRepository userRepository;
    private final TransactionManager transactionManager;

    public DbUserServiceImpl(UserRepository userRepository, TransactionManager transactionManager) {
        this.userRepository = userRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        return transactionManager.doInReadOnlyTransaction(() -> userRepository.findById(id));
    }

    @Override
    public User save(User user) {
        return transactionManager.doInTransaction(() -> userRepository.save(user));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInReadOnlyTransaction(() -> userRepository.findByLogin(login));
    }
}
