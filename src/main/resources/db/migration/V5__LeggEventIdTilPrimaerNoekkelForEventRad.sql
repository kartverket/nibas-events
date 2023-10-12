alter table eventrad drop constraint eventrad_pkey;
alter table eventrad add constraint eventrad_pkey primary key (uuid, event_fk);
