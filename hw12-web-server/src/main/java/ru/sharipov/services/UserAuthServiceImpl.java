package ru.sharipov.services;

public class UserAuthServiceImpl implements UserAuthService {

    private final DbUserService dbUserService;

    public UserAuthServiceImpl(DbUserService dbUserService) {
        this.dbUserService = dbUserService;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return dbUserService.findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
