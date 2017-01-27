BEGIN
  EXECUTE IMMEDIATE 'DROP TRIGGER test_types_trg'\;
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -4080 THEN
    RAISE\;
  END IF\;
END\;
;
BEGIN
  EXECUTE IMMEDIATE 'DROP TRIGGER parent_trg'\;
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -4080 THEN
    RAISE\;
  END IF\;
END\;
;
BEGIN
  EXECUTE IMMEDIATE 'DROP TRIGGER child_trg'\;
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -4080 THEN
    RAISE\;
  END IF\;
END\;
;

BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE test_types_seq'\;
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -2289 THEN
    RAISE\;
  END IF\;
END\;
;

BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE parent_seq'\;
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -2289 THEN
    RAISE\;
  END IF\;
END\;
;
BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE child_seq'\;
  EXCEPTION
  WHEN OTHERS THEN
  IF SQLCODE != -2289 THEN
    RAISE\;
  END IF\;
END\;
;



BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE test_types'\;
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE\;
      END IF\;
END\;
;
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE child'\;
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE\;
      END IF\;
END\;
;
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE parent'\;
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE\;
      END IF\;
END\;
;
