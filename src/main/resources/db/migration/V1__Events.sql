CREATE TABLE events
(
    id              serial    not null primary key,
    event_uuid      text      NOT NULL unique,
    event_offset    numeric   NOT NULL,
    event_type      text      NOT NULL,
    target          text      NOT NULL,
    target_id       text      NOT NULL,
    target_revision numeric   NOT NULL,
    event_timestamp timestamp with time zone NOT NULL
)
