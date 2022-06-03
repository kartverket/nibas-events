CREATE TABLE events
(
    id        serial NOT NULL primary key,
    event_type text   NOT NULL,
    target    text   NOT NULL,
    target_id  text   NOT NULL
)
