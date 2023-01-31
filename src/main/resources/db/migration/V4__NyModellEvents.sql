drop table events;

CREATE TABLE event
(
    id               serial    NOT NULL primary key,
    inntreffer       date      NOT NULL,
    event_timestamp  timestamp with time zone NOT NULL
);

CREATE TABLE eventrad
(
    uuid            text      NOT NULL primary key,
    event_type      text      NOT NULL,
    flate_type      text      NOT NULL,
    lokalid         text      NOT NULL,
    event_fk        integer   NOT NULL,
    CONSTRAINT event_fk
        FOREIGN KEY (event_fk)
        REFERENCES event (id)
);
