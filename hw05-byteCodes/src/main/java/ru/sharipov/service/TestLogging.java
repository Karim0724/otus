package ru.sharipov.service;

public interface TestLogging {
    void calculation(int a, int b);
    void calculation(int a, int b, int c);
    void calculation(int b, String d);
    void calculation(String b, int d);
    void calculation(long a);
    void calculation(int a);
    void calculation();
}
