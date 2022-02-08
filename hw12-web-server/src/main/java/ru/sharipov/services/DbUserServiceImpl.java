package ru.sharipov.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sharipov.dao.repository.DataTemplate;
import ru.sharipov.dao.sessionmanager.TransactionManager;
import ru.sharipov.model.User;

import java.util.Optional;

public class DbUserServiceImpl implements DbUserService {
    private static final Logger log = LoggerFactory.getLogger(DbUserServiceImpl.class);
    private final DataTemplate<User> userTemplate;
    private final TransactionManager transactionManager;

    public DbUserServiceImpl(DataTemplate<User> userTemplate, TransactionManager transactionManager) {
        this.userTemplate = userTemplate;
        this.transactionManager = transactionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var userOptional = userTemplate.findById(session, id);
            log.info("user: {}", userOptional);
            return userOptional;
        });
    }

    @Override
    public User save(User user) {
        return transactionManager.doInTransaction(session -> {
            if (user.getId() == null) {
                userTemplate.insert(session, user);
                log.info("created user: {}", user);
                return user;
            }
            userTemplate.update(session, user);
            log.info("updated user: {}", user);
            return user;
        });
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var userOptional = userTemplate.findByEntityField(session, "login", login);
            return userOptional.stream().findFirst();
        });
    }
}
