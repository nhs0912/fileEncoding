drop table if exists item cascade;
drop table if exists member cascade;
drop table if exists upload_items cascade;
drop table if exists upload_order cascade;
create table item
(
    item_id              bigint generated by default as identity,
    member_id            bigint,
    original_name        varchar(255),
    original_path        varchar(255),
    original_extend_type varchar(255),
    save_name            varchar(255),
    save_path            varchar(255),
    save_extend_type     varchar(255),
    status               varchar(255) check (status in ('ACTIVE', 'DELETE')),
    create_at            timestamp(6),
    modified_at          timestamp(6),
    primary key (item_id)
);

create table member
(
    member_id   bigint generated by default as identity,
    name        varchar(255),
    employee_id varchar(255),
    password    varchar(255),
    email       varchar(255),
    status      varchar(255) check (status in ('ACTIVE', 'TERMINATED')),
    create_at   timestamp(6),
    modified_at timestamp(6),
    primary key (member_id)
);

create table upload_items
(
    create_at       timestamp(6),
    item_id         bigint,
    modified_at     timestamp(6),
    upload_items_id bigint generated by default as identity,
    upload_order_id bigint,
    primary key (upload_items_id)
);

create table upload_order
(
    create_at       timestamp(6),
    member_id       bigint,
    modified_at     timestamp(6),
    upload_order_id bigint generated by default as identity,
    primary key (upload_order_id)
);

alter table if exists upload_items
    add constraint FKqqh7vc5nj6rbymux7scgy144c
    foreign key (item_id)
    references item;

alter table if exists upload_items
    add constraint FK5ukq8jxtwxfb9xvil4t12qtvl
    foreign key (upload_order_id)
    references upload_order;

alter table if exists upload_order
    add constraint FKsw8tcpq6thvjejyfqm97uxg31
    foreign key (member_id)
    references member;
