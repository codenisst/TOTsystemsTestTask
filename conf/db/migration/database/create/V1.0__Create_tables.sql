CREATE TABLE security
(
    id            INTEGER,
    secid         TEXT PRIMARY KEY,
    regnumber     TEXT,
    name          TEXT,
    emitent_title TEXT
);

CREATE TABLE history
(
    secid     TEXT PRIMARY KEY,
    tradedate TEXT,
    numtrades INTEGER,
    open      DOUBLE,
    close     DOUBLE,
    FOREIGN KEY (secid) REFERENCES security (secid) ON DELETE CASCADE ON UPDATE CASCADE
);