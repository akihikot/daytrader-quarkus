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
DROP TABLE IF EXISTS holdingejb;
DROP TABLE IF EXISTS keygenejb;
DROP TABLE IF EXISTS orderejb;
DROP TABLE IF EXISTS accountejb;

CREATE TABLE holdingejb
  (PURCHASEPRICE DECIMAL(14, 2),
   HOLDINGID INTEGER NOT NULL,
   QUANTITY DOUBLE PRECISION NOT NULL,
   PURCHASEDATE TIMESTAMP,
   ACCOUNT_ACCOUNTID INTEGER,
   QUOTE_SYMBOL VARCHAR(255));

ALTER TABLE holdingejb 
    ADD CONSTRAINT PK_HOLDINGEJB PRIMARY KEY (HOLDINGID);

CREATE TABLE keygenejb
  (KEYVAL INTEGER NOT NULL,
   KEYNAME VARCHAR(255) NOT NULL);

ALTER TABLE keygenejb 
    ADD CONSTRAINT PK_KEYGENEJB PRIMARY KEY (KEYNAME);

CREATE TABLE orderejb
  (ORDERFEE DECIMAL(14, 2),
   COMPLETIONDATE TIMESTAMP,
   ORDERTYPE VARCHAR(255),
   ORDERSTATUS VARCHAR(255),
   PRICE DECIMAL(14, 2),
   QUANTITY DOUBLE PRECISION NOT NULL,
   OPENDATE TIMESTAMP,
   ORDERID INTEGER NOT NULL,
   ACCOUNT_ACCOUNTID INTEGER,
   QUOTE_SYMBOL VARCHAR(255),
   HOLDING_HOLDINGID INTEGER);

ALTER TABLE orderejb 
    ADD CONSTRAINT PK_ORDEREJB PRIMARY KEY (ORDERID);

CREATE TABLE accountejb
  (ACCOUNTID INTEGER NOT NULL,
   PROFILE_USERID VARCHAR(255) NOT NULL,
   OPENBALANCE DECIMAL(14, 2) NOT NULL,
   BALANCE DECIMAL(14, 2) NOT NULL);

ALTER TABLE accountejb 
    ADD CONSTRAINT PK_ACCOUNTEJB PRIMARY KEY (ACCOUNTID);

CREATE INDEX ACCOUNT_USERID ON accountejb(PROFILE_USERID);
CREATE INDEX HOLDING_ACCOUNTID ON holdingejb(ACCOUNT_ACCOUNTID);
CREATE INDEX ORDER_ACCOUNTID ON orderejb(ACCOUNT_ACCOUNTID);
CREATE INDEX ORDER_HOLDINGID ON orderejb(HOLDING_HOLDINGID);
CREATE INDEX CLOSED_ORDERS ON orderejb(ACCOUNT_ACCOUNTID,ORDERSTATUS);
