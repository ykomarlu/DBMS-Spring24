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

CREATE TABLE Post ( 
    postId int NOT NULL,
    userId int NOT NULL, 
    postDate date NOT NULL default NOW(), 
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
    commentId int not null, 
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

-- Order of Creating Tables: User, Post, Hashtag, comment, bookmark, heart, follow