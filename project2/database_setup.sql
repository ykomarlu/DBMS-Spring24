-- Create the database.
DROP DATABASE cs4370_mb_platform;
CREATE DATABASE IF NOT EXISTS cs4370_mb_platform;

-- Use the created database.
USE cs4370_mb_platform;

-- Create the user table.
CREATE TABLE User (
    userId int auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    primary key (userId),
    unique (username),
    constraint userName_min_length check (char_length(trim(userName)) >= 2),
    constraint firstName_min_length check (char_length(trim(firstName)) >= 2),
    constraint lastName_min_length check (char_length(trim(lastName)) >= 2)
);



-- insert into User values ()

CREATE TABLE Post ( 
    postId int NOT NULL auto_increment,
    userId int NOT NULL, 
    postDate datetime NOT NULL default NOW(), 
    postText varchar(255) NOT NULL, 
    primary key(postId), 
    foreign key(userId) references user(userId) 
);



CREATE TABLE Heart (
    postId int NOT NULL,
    userId int NOT NULL,
    primary key (userId, postId),
    foreign key(postId) references post(postId), 
    foreign key(userId) references user(userId) 
);



CREATE TABLE Bookmark ( 
    postId int NOT NULL,
    userId int NOT NULL,
    primary key(userId, postId),
    foreign key(postId) references post(postId), 
    foreign key(userId) references user(userId) 
);


CREATE TABLE Hashtag ( 
    hashtag varchar(225) NOT NULL, 
    postId int NOT NULL, 
    primary key(hashtag,postId) ,
    foreign key(postId) references post(postId)
);



CREATE TABLE Comment (
    commentId int not null auto_increment, 
    postId int not null, 
    userId int not null,
    commentDate date not null,
    commentText varchar(225) not null,
    primary key(commentId),
    foreign key (postId) References Post(postId),
    foreign key (userId) references user(userId)
);



CREATE TABLE Follow ( 
    followerUserId int NOT NULL, 
    followeeUserId int NOT NULL, 
    primary key (followerUserId, followeeUserId), 
    foreign key(followerUserId) references user(userId), 
    foreign key(followeeUserId) references user(userId) 
);

insert into User 
values 
(1, "alex_bradshaw", "testpass", "Alex", "Bradshaw"),
(2, "jon_green", "jgreen", "Jon", "Green"),
(3, "yush_komarlu", "Inc@ntat3m", "Yushus", "Komarlu");
insert into Post 
values 
(1, 1, now(), "First Post by Alex"),
(2, 1, now(), "Second Post by Alex"),
(3, 2, now(), "First Post by Jon");

insert into Hashtag 
values 
("Draft", 2),
("Published", 3);

insert into Comment 
values 
(1, 2, 3, '2024-03-20', 'comment'),
(2, 3, 3, '2021-04-24', 'yay comment test');

insert into Bookmark 
values 
(3, 2),
(1, 1);

insert into Heart value 
(2, 1);

insert into Follow values
(1, 2),
(1, 3),
(2, 1);

-- Order of Creating Tables: User, Post, Hashtag, comment, bookmark, heart, follow