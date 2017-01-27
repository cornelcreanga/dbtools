CREATE TABLE parent
(
  id bigserial NOT NULL,
  name character varying(200),
  CONSTRAINT pk_id PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE parent
  OWNER TO test;

-- Table: child

-- DROP TABLE child;

CREATE TABLE child
(
  id bigserial NOT NULL,
  name character varying(200),
  parent_id integer,
  CONSTRAINT pk_child_id PRIMARY KEY (id),
  CONSTRAINT fk_child_parent FOREIGN KEY (parent_id)
      REFERENCES parent (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE child
  OWNER TO test;

  -- Table: test_types

-- DROP TABLE test_types;

CREATE TABLE test_types
(
  id bigserial NOT NULL,
  c_varchar character varying(100),
  c_text text,
  c_blob bytea,
  c_time time without time zone,
  c_timestamp timestamp without time zone,
  c_date date,
  c_decimal numeric(22,6),
  c_double double precision,
  c_float real,
  c_bigint bigint,
  c_int integer,
  c_smallint smallint,
  CONSTRAINT pk_test_types PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE test_types
  OWNER TO test;

