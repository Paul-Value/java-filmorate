CREATE TABLE IF NOT EXISTS MPA_RATING (
    ID BIGINT NOT NULL PRIMARY KEY,
    NAME VARCHAR(5) NOT NULL
);

CREATE TABLE IF NOT EXISTS USERS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    EMAIL VARCHAR(255) NOT NULL,
    LOGIN VARCHAR(255) NOT NULL,
    NAME VARCHAR(255),
    BIRTHDAY DATE
);

CREATE TABLE IF NOT EXISTS GENRES (
    ID BIGINT NOT NULL PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILMS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(200),
    RELEASE_DATE DATE,
    DURATION INT CHECK (DURATION > 0),
    MPA_RATING_ID BIGINT,
    FOREIGN KEY (MPA_RATING_ID) REFERENCES MPA_RATING(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS USER_FILM_LIKES (
    USER_ID BIGINT,
    FILM_ID BIGINT,
    PRIMARY KEY (USER_ID, FILM_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(ID) ON DELETE CASCADE,
    FOREIGN KEY (FILM_ID) REFERENCES FILMS(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS USER_FRIENDS (
    USER_ID BIGINT,
    FRIEND_ID BIGINT,
    STATUS VARCHAR(255) NOT NULL DEFAULT 'unconfirmed',
    PRIMARY KEY (USER_ID, FRIEND_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(ID) ON DELETE CASCADE,
    FOREIGN KEY (FRIEND_ID) REFERENCES USERS(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FILM_GENRE (
    FILM_ID BIGINT,
    GENRE_ID BIGINT,
    PRIMARY KEY (FILM_ID, GENRE_ID),
    FOREIGN KEY (FILM_ID) REFERENCES FILMS(ID) ON DELETE CASCADE,
    FOREIGN KEY (GENRE_ID) REFERENCES GENRES(ID) ON DELETE CASCADE
);