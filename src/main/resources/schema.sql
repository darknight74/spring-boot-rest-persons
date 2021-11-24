create table person (
    id IDENTITY,
    name VARCHAR2 not null,
    surname VARCHAR2 not null,
    phone VARCHAR2,
    date_of_birth DATE not null,
    username VARCHAR2 not null,
    password VARCHAR2 not null
);