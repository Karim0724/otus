create sequence client_seq start with 1 increment by 1;
create table client
(
    id   bigint not null primary key,
    name varchar(50)
);

create sequence address_seq start with 1 increment by 1;
create table address
(
    id bigint not null primary key,
    street varchar(50) not null,
    client_id bigint
);

create sequence phone_seq start with 1 increment by 1;
create table phone
(
    id bigint not null primary key,
    number varchar(50) not null,
    client_id bigint
);

ALTER TABLE address
    ADD CONSTRAINT fk_address
        FOREIGN KEY (client_id)
            REFERENCES client (id);

ALTER TABLE phone
    ADD CONSTRAINT fk_phone
        FOREIGN KEY (client_id)
            REFERENCES client (id);
