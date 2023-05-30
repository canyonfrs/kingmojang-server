alter table memo add column deleted_at datetime(6) default null;
alter table memo add column deleted bit default false not null;

alter table comment add column deleted_at datetime(6) default null;
alter table comment add column deleted bit default false not null;

alter table reply add column deleted_at datetime(6) default null;
alter table reply add column deleted bit default false not null;
