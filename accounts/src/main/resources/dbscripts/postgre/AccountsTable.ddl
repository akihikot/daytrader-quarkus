#    Licensed to the Apache Software Foundation (ASF) under one or more
#    contributor license agreements.  See the NOTICE file distributed with
#    this work for additional information regarding copyright ownership.
#    The ASF licenses this file to You under the Apache License, Version 2.0
#    (the "License"); you may not use this file except in compliance with
#    the License.  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.

# Each SQL statement in this file should terminate with a semicolon (;)
# Lines starting with the pound character (#) are considered as comments

# If you first time initialize postgre db, the drop statements should be commented out as following.
DROP TABLE IF EXISTS accountprofileejb;
DROP TABLE IF EXISTS accountejb;
DROP TABLE IF EXISTS keygenejb;

# DDL for the Accounts microservice

CREATE TABLE accountprofileejb
  (ADDRESS VARCHAR(255),
   PASSWD VARCHAR(255),
   USERID VARCHAR(255) NOT NULL,
   EMAIL VARCHAR(255),
   CREDITCARD VARCHAR(255),
   FULLNAME VARCHAR(255));

ALTER TABLE accountprofileejb 
    ADD CONSTRAINT PK_ACCOUNTPROFILE2 PRIMARY KEY (USERID);

CREATE TABLE keygenejb
  (KEYVAL INTEGER NOT NULL,
   KEYNAME VARCHAR(255) NOT NULL);

ALTER TABLE keygenejb 
    ADD CONSTRAINT PK_KEYGENEJB PRIMARY KEY (KEYNAME);

CREATE TABLE accountejb
  (CREATIONDATE TIMESTAMP,
   LOGOUTCOUNT INTEGER NOT NULL,
   ACCOUNTID INTEGER NOT NULL,
   LASTLOGIN TIMESTAMP,
   LOGINCOUNT INTEGER NOT NULL,
   PROFILE_USERID VARCHAR(255));

ALTER TABLE accountejb 
    ADD CONSTRAINT PK_ACCOUNTEJB PRIMARY KEY (ACCOUNTID);

CREATE INDEX ACCOUNT_USERID ON accountejb(PROFILE_USERID);
