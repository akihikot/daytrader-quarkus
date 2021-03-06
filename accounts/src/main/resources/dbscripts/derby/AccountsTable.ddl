## DDL for the users microservice

DROP TABLE ACCOUNTPROFILEEJB;
DROP TABLE KEYGENEJB;
DROP TABLE ACCOUNTEJB;

CREATE TABLE ACCOUNTPROFILEEJB
  (ADDRESS VARCHAR(250),
   PASSWD VARCHAR(250),
   USERID VARCHAR(250) NOT NULL,
   EMAIL VARCHAR(250),
   CREDITCARD VARCHAR(250),
   FULLNAME VARCHAR(250));

ALTER TABLE ACCOUNTPROFILEEJB
  ADD CONSTRAINT PK_ACCOUNTPROFILE2 PRIMARY KEY (USERID);

CREATE TABLE KEYGENEJB
  (KEYVAL INTEGER NOT NULL,
   KEYNAME VARCHAR(250) NOT NULL);

ALTER TABLE KEYGENEJB
  ADD CONSTRAINT PK_KEYGENEJB PRIMARY KEY (KEYNAME);

## Moved open balance and balance to portfolios
##
CREATE TABLE ACCOUNTEJB
  (CREATIONDATE TIMESTAMP,
   LOGOUTCOUNT INTEGER NOT NULL,
   ACCOUNTID INTEGER NOT NULL,
   LASTLOGIN TIMESTAMP,
   LOGINCOUNT INTEGER NOT NULL,
   PROFILE_USERID VARCHAR(250));

##CREATE TABLE ACCOUNTEJB
##  (CREATIONDATE TIMESTAMP,
##   LOGOUTCOUNT INTEGER NOT NULL,
##   ACCOUNTID INTEGER NOT NULL,
##   LASTLOGIN TIMESTAMP,
##   LOGINCOUNT INTEGER NOT NULL,
##   PROFILE_USERID VARCHAR(250));

ALTER TABLE ACCOUNTEJB
  ADD CONSTRAINT PK_ACCOUNTEJB PRIMARY KEY (ACCOUNTID);

CREATE INDEX ACCOUNT_USERID ON ACCOUNTEJB(PROFILE_USERID);

