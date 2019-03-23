CREATE SEQUENCE user_verification_pk_seq START WITH 50;

 create table user_verification(
       id int8 not null,
        token varchar(255),
        code varchar(9),
        user_login_id int8,
        verification_channel varchar(255),
        verification_reason varchar(255),
        token_expiry_date timestamp,
        last_token_sent_date timestamp,
        vertification_date timestamp,
        has_consumed int4,

        primary key (id)
    );
    
    
alter table user_verification 
       add constraint usr_emil_vrfctin_usr_lgn_id_fk 
       foreign key (user_login_id) 
       references user_login;
       
       
alter table user_login add is_permanently_locked int4;
      
  