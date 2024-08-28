
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    user_name VARCHAR(255),
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    release_date DATE,
    duration INT,
    mpa_id  INT,
    FOREIGN KEY (mpa_id) REFERENCES mpa(mpa_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INT,
    genre_id INT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friends (
    user_id INT,
    friend_id INT,
    status VARCHAR(50),
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id INT,
    user_id INT,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

INSERT INTO mpa (mpa_id, name) VALUES (1, 'G');
INSERT INTO mpa (mpa_id, name) VALUES (2, 'PG');
INSERT INTO mpa (mpa_id, name) VALUES (3, 'PG-13');
INSERT INTO mpa (mpa_id, name) VALUES (4, 'R');
INSERT INTO mpa (mpa_id, name) VALUES (5, 'NC-17');


INSERT INTO genres (genre_id, name) VALUES (1, 'Комедия');
INSERT INTO genres (genre_id, name) VALUES (2, 'Драма');
INSERT INTO genres (genre_id, name) VALUES (3, 'Мультфильм');
INSERT INTO genres (genre_id, name) VALUES (4, 'Триллер');
INSERT INTO genres (genre_id, name) VALUES (5, 'Документальный');
INSERT INTO genres (genre_id, name) VALUES (6, 'Боевик');









