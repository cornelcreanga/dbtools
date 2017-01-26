CREATE TABLE parent
(
 id INTEGER NOT NULL ,
 name VARCHAR(24) NOT NULL,
 UNIQUE (id)
);

CREATE SEQUENCE parent_seq;
CREATE OR REPLACE TRIGGER  parent_trg
BEFORE INSERT ON parent
FOR EACH ROW
WHEN (new.id IS NULL)
BEGIN
  SELECT parent_seq.NEXTVAL INTO :new.id FROM   dual;;
END;;
;

ALTER TRIGGER parent_trg ENABLE ;

CREATE TABLE child (
  id integer NOT NULL,
  parent_id integer NOT NULL,
  name varchar(45) NOT NULL,
  unique (id),
  CONSTRAINT fk_child FOREIGN KEY (parent_id) REFERENCES parent (id) ON DELETE CASCADE
);

CREATE SEQUENCE child_seq;
CREATE OR REPLACE TRIGGER child_trg
BEFORE INSERT ON child
FOR EACH ROW
WHEN (new.id IS NULL)
BEGIN
  SELECT child_seq.NEXTVAL INTO :new.id FROM   dual;;
END;;
;

ALTER TRIGGER child_trg ENABLE ;
