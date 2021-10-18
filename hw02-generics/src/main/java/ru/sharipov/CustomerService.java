package ru.sharipov;


import java.util.*;

public class CustomerService {
    private final Map<Customer, String> storage = new TreeMap<>(getCustomerComparator());

    public Map.Entry<Customer, String> getSmallest() {
        return copyEntry(storage.entrySet().iterator().next());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Set<Map.Entry<Customer, String>> entrySet = storage.entrySet();
        for (Map.Entry<Customer, String> next : entrySet) {
            if (getCustomerComparator().compare(next.getKey(), customer) > 0) {
                return copyEntry(next);
            }
        }
        return null;
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
