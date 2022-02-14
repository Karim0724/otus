create sequence user_seq start with 1 increment by 1;
create table users
(
    id   bigint not null primary key,
    name varchar(50) not null,
    login varchar(100) unique,
    password varchar(250) not null
);
