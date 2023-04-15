create table if not exists Users(
    id identity,
    username varchar(30) unique not null ,
    password varchar(255) not null ,
    roles bigint
    );

create table if not exists Roles(
    id identity,
    name varchar(30)

);

alter table users add foreign key (roles) references Roles(id);
