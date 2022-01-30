package ru.sharipov.crm.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq_generator")
    @SequenceGenerator(name = "client_seq_generator", sequenceName = "client_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();

    public Client() {
    }

    public Client(String name) {
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        setAddress(address);
        phones.forEach(this::addPhone);
    }

    @Override
    public Client clone() {
        var phones = this.phones.stream().map(Phone::copy).collect(Collectors.toList());
        Address address = null;
        if (this.address != null) {
            address = this.address.copy();
        }
        return new Client(this.id, this.name, address, phones);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address == null) {
            if (this.address != null) {
                this.address.setClient(null);
            }
        } else {
            address.setClient(this);
        }
        this.address = address;
    }

    public void addPhone(Phone phone) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone is null");
        }
        if (phones == null) {
            phones = new ArrayList<>();
        }
        phones.add(phone);
        phone.setClient(this);
    }

    public List<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
