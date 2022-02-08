package ru.sharipov.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
