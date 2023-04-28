create table site (
    id serial primary key not null,
    url_address varchar unique,
	login varchar(2000) unique,
    password varchar(2000) not null
);