create table shortcut (
    id serial primary key not null,
    url_link varchar not null,
	link_code varchar unique,
    id_site int not null references site(id)
);