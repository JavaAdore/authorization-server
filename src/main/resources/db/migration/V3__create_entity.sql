CREATE SEQUENCE entity_pk_seq START WITH 50;

 create table entity
 (
       id int8 not null,
       referenced_other_entity_id int8 not null,
       referenced_other_entity_schema varchar(255) not null,
       referenced_other_entity_table_name varchar(255) not null,
       referenced_other_entity_type varchar(255) not null,
       primary key (id)
);
    
    
    
   
	