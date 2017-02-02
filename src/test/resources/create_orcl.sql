-- CREATE USER test IDENTIFIED BY test;
-- GRANT CONNECT,RESOURCE,CREATE SESSION TO test;

CREATE TABLE parent
(
 id INTEGER NOT NULL ,
 name VARCHAR(24) NOT NULL,
 UNIQUE (id)
);

CREATE SEQUENCE parent_seq CACHE 1000;
CREATE OR REPLACE TRIGGER  parent_trg
BEFORE INSERT ON parent
FOR EACH ROW
WHEN (new.id IS NULL)
BEGIN
  SELECT parent_seq.NEXTVAL INTO :new.id FROM   dual\;
END\;
;

ALTER TRIGGER parent_trg ENABLE ;

CREATE TABLE child (
  id integer NOT NULL,
  parent_id integer NOT NULL,
  name varchar(45) NOT NULL,
  unique (id),
  CONSTRAINT fk_child FOREIGN KEY (parent_id) REFERENCES parent (id) ON DELETE CASCADE
);

CREATE SEQUENCE child_seq CACHE 1000;
CREATE OR REPLACE TRIGGER child_trg
BEFORE INSERT ON child
FOR EACH ROW
WHEN (new.id IS NULL)
BEGIN
  SELECT child_seq.NEXTVAL INTO :new.id FROM   dual\;
END\;
;

ALTER TRIGGER child_trg ENABLE ;

CREATE TABLE test_types (
  id integer NOT NULL,
  c_varchar2 varchar2(100) DEFAULT NULL,
  c_nvarchar2 nvarchar2(100) DEFAULT NULL,
  c_clob clob default null,
  c_nclob nclob default null,
  c_number number DEFAULT null,
  c_double BINARY_DOUBLE  DEFAULT null,
  c_float binary_float DEFAULT NULL,
  c_date date DEFAULT NULL,
  c_timestamp TIMESTAMP default null,
  c_timestamptz TIMESTAMP WITH TIME ZONE default null,
  c_blob blob,
  unique (id)
);

CREATE SEQUENCE test_types_seq CACHE 1000;
CREATE OR REPLACE TRIGGER test_types_trg
BEFORE INSERT ON test_types
FOR EACH ROW
WHEN (new.id IS NULL)
  BEGIN
    SELECT test_types_seq.NEXTVAL INTO :new.id FROM   dual\;
  END\;
;
