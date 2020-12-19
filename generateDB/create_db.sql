create database if not exists usersdb;
use usersdb;

create table Users
(
	Id int primary key auto_increment not null,
    FullName nvarchar(50) not null,
    Email nvarchar(20) unique not null,
    Nickname nvarchar(50) not null,
    Password nvarchar(20) not null,
    Role nvarchar(30) not null
);

create table Messages
(
	Id int primary key auto_increment not null,
    Message nvarchar(100) not null,
    Role nvarchar(30) not null
)