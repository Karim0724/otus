create table users
(
    id       bigserial    not null primary key,
    name     varchar(50)  not null,
    login    varchar(100) unique,
    password varchar(250) not null
);