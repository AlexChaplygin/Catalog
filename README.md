# Catalog
Test task. Catalog of users with sectors

Tasks:
 
1. "Sectors" selectbox:
1.1. Add all the entries from the "Sectors" selectbox to database
1.2. Compose the "Sectors" selectbox using data from database
 
2. Perform the following activities after the "Save" button has been pressed: 
2.1. Validate all input data (all fields are mandatory)
2.2. Store all input data to database (Name, Sectors, Agree to terms)
2.3. Refill the form using stored data 
2.4. Allow the user to edit his/her own data during the session

---

![alt text](https://monosnap.com/image/AP1UMd4gAXwq5cg6Pzac35CuFxcBHq.png)

### TECHNOLOGIES AND TOOLS

| # | Title |
|---| ----- |
|1| Java |
|2| Spring Boot |
|3| Spring Data |
|4| JWT |
|5| AngularJS |
|6| PostgreSQL |


### DATABASE DUMP


>CREATE TABLE users
(
  id integer NOT NULL DEFAULT nextval('user_id_seq'::regclass),
  name character varying(255),
  agree boolean,
  CONSTRAINT users_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users
  OWNER TO postgres;
  
>CREATE TABLE sectors
(
  id integer NOT NULL DEFAULT nextval('sector_id_seq'::regclass),
  parent_id integer,
  name character varying(250) NOT NULL,
  CONSTRAINT sectors_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sectors
  OWNER TO postgres;

>CREATE TABLE user_sector
(
  user_id integer NOT NULL,
  sector_id integer NOT NULL,
  CONSTRAINT sector_fk FOREIGN KEY (sector_id)
      REFERENCES sectors (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_fk FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE user_sector
  OWNER TO postgres;

>CREATE SEQUENCE sector_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 7
  CACHE 1;
ALTER TABLE sector_id_seq
  OWNER TO postgres;

>CREATE SEQUENCE user_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 4
  CACHE 1;
ALTER TABLE user_id_seq
  OWNER TO postgres;
