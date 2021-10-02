package ru.sharipov;

import com.google.common.collect.Lists;

import java.util.List;

public class HelloOtus {
    public static void main(String[] args) {
        List<String> numbers = List.of("1", "2", "3");
        List<String> reversedNumbers = Lists.reverse(numbers);
        System.out.println(reversedNumbers);
    }
}
