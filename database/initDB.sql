CREATE TABLE IF NOT EXISTS account
(
    id          BIGSERIAL       PRIMARY KEY,
    login       VARCHAR(200)    NOT NULL UNIQUE,
    password    VARCHAR(200)    NOT NULL,
    name        VARCHAR(200)    NOT NULL,
    surname     VARCHAR(200)    NOT NULL,
    patronymic  VARCHAR(200)    NOT NULL
);

CREATE TABLE IF NOT EXISTS carrier
(
    id    SERIAL        PRIMARY KEY,
    name  VARCHAR(200)  NOT NULL UNIQUE,
    phone VARCHAR(50)   NOT NULL
);

CREATE TABLE IF NOT EXISTS point
(
    id   SERIAL         PRIMARY KEY,
    name VARCHAR(200)   NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS path
(
    id                      SERIAL  PRIMARY KEY,
    departure_point_id      INTEGER NOT NULL REFERENCES point (id) ON DELETE CASCADE,
    destination_point_id    INTEGER NOT NULL REFERENCES point (id) ON DELETE CASCADE,
    carrier_id              INTEGER NOT NULL REFERENCES carrier (id) ON DELETE CASCADE,
    duration                INTERVAL NOT NULL,
    CONSTRAINT UC_path UNIQUE (departure_point_id, destination_point_id, carrier_id)
);

CREATE TABLE IF NOT EXISTS ticket
(
    id          BIGSERIAL  	PRIMARY KEY,
    path_id     INTEGER		NOT NULL REFERENCES path (id) ON DELETE CASCADE,
    place       INTEGER    	NOT NULL,
    price       MONEY       NOT NULL,
    date_time    TIMESTAMPTZ ,
    owner_id    INTEGER		REFERENCES account (id) ON DELETE SET DEFAULT,
	CONSTRAINT UC_ticket UNIQUE (path_id, place)
);
