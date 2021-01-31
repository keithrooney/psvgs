create table users (
	id varchar(40) primary key, 
	username varchar(150) unique not null, 
	created_at timestamp not null, 
	updated_at timestamp not null
);