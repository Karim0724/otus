package ru.sharipov;


import java.util.*;

public class CustomerService {
    private final TreeMap<Customer, String> storage = new TreeMap<>(getCustomerComparator());

    public Map.Entry<Customer, String> getSmallest() {
        return copyEntry(storage.entrySet().iterator().next());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var higherEntry = storage.higherEntry(customer);
        return storage.higherEntry(customer) == null ? null : copyEntry(higherEntry);
    }

    private Map.Entry<Customer, String> copyEntry(Map.Entry<Customer, String> entry) {
        return Map.entry(new Customer(entry.getKey()), entry.getValue());
    }

    public void add(Customer customer, String data) {
        storage.put(new Customer(customer), data);
    }

    private Comparator<Customer> getCustomerComparator() {
        return Comparator.comparing(Customer::getScores);
    }
}
