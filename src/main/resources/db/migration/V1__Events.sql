CREATE TABLE event
(
    uuid             text      NOT NULL primary key ,
    eventnummer      serial    NOT NULL unique,
    inntreffer       date      NOT NULL,
    event_timestamp  timestamp with time zone NOT NULL
);

CREATE TABLE eventrad
(
    uuid            text      NOT NULL primary key,
    event_type      text      NOT NULL,
    objekt_type     text      NOT NULL,
    lokalid         text      NOT NULL,
    event        text      NOT NULL,
    CONSTRAINT event_fk
        FOREIGN KEY (event)
        REFERENCES event (uuid)
);
