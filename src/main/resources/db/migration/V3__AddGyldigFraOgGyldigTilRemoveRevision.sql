alter table events add column gyldigfra timestamp with time zone default '2022.12.01' NOT NULL;
alter table events add column gyldigtil timestamp with time zone;
alter table events drop column target_revision;

