CREATE TABLE test_types (
  id uuid PRIMARY KEY,
  c_ascii text,
  c_bigint bigint,
  c_blob blob,
  c_boolean boolean,
  c_date date,
  c_decimal decimal,
  c_double double,
  c_float float,
  c_inet inet,
  c_int int,
  c_list list<text>,
  c_map map<text,text>,
  c_set set<text>,
  c_smallint smallint,
  c_text text,
  c_time time,
  c_timestamp timestamp,
  c_timeuuid timeuuid,
  c_tinyint tinyint,
  c_varchar varchar,
  c_varint varint
);