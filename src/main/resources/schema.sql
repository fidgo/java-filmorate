DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS friendship CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS genres_per_film CASCADE;
DROP TABLE IF EXISTS mpas CASCADE;
DROP TABLE IF EXISTS genres CASCADE;

CREATE TABLE users
(
    id       int8 auto_increment,
    name     varchar(50),
    email    varchar(200) UNIQUE NOT NULL,
    login    varchar(50) UNIQUE  NOT NULL,
    birthday date,

    CONSTRAINT users_id_pk primary key (id)
);

CREATE TABLE films
(
    id           int8 auto_increment,
    name        varchar(50),
    description  varchar(200),
    mpa_id       int,
    release_date date,
    duration     int,

    CONSTRAINT films_id_pk primary key (id)
);

CREATE TABLE friendship
(
    user_id   int8 NOT NULL,
    friend_id int8 NOT NULL,

    CONSTRAINT unq_friendship_userid_friend_id UNIQUE (user_id, friend_id)
);

CREATE TABLE likes
(
    user_id int8 NOT NULL,
    film_id int8 NOT NULL,
    CONSTRAINT unq_user_film UNIQUE (user_id, film_id)
);

CREATE TABLE genres_per_film
(
    film_id   int8 NOT NULL,
    genres_id int  NOT NULL,
    CONSTRAINT unq_genres_per_film UNIQUE (film_id, genres_id)
);

CREATE TABLE mpas
(
    id   int auto_increment,
    name varchar(5),
    CONSTRAINT unq_mpas_name UNIQUE (name),
    CONSTRAINT mpas_name_enum check name IN ('G', 'PG', 'PG-13', 'R', 'NC-17'),

    CONSTRAINT mpas_id_pk primary key (id)

);

CREATE TABLE genres
(
    id   int auto_increment,
    name varchar(14),
    CONSTRAINT unq_genres_name UNIQUE (name),
    CONSTRAINT genres_name_enum check name IN ('Комедия', 'Драма', 'Мультфильм', 'Триллер', 'Документальный', 'Боевик'),
    CONSTRAINT genres_id_pk primary key (id)

);

ALTER TABLE films
    ADD FOREIGN KEY (mpa_id) REFERENCES mpas (id);

ALTER TABLE friendship
    ADD FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE friendship
    ADD FOREIGN KEY (friend_id) REFERENCES users (id);

ALTER TABLE genres_per_film
    ADD FOREIGN KEY (film_id) REFERENCES films (id);

ALTER TABLE genres_per_film
    ADD FOREIGN KEY (genres_id) REFERENCES genres (id);

ALTER TABLE likes
    ADD FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE likes
    ADD FOREIGN KEY (film_id) REFERENCES films (id);

INSERT INTO mpas (NAME)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO genres (NAME)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');