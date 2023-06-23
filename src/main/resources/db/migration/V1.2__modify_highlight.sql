alter table highlight drop start_x;
alter table highlight drop start_y;
alter table highlight drop end_x;
alter table highlight drop end_y;
alter table highlight drop version;

alter table highlight add column start_node varchar(255) not null comment '시작 노드 이름';
alter table highlight add column start_offset integer not null comment '시작 노드 위치';
alter table highlight add column end_node varchar(255) not null comment '끝 노드 이름';
alter table highlight add column end_offset integer not null comment '끝 노드 위치';