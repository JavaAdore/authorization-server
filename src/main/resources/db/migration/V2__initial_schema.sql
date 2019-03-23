CREATE SEQUENCE user_login_pk_seq START WITH 50;

 create table user_login (
       id int8 not null,
        authentication_channel varchar(255),
        password varchar(255),
        user_id int8,
        user_name varchar(255),
        verification_status varchar(255),
        primary key (id)
    )