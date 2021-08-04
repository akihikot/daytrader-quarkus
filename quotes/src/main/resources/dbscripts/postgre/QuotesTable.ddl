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

DROP TABLE IF EXISTS quoteejb;
DROP TABLE IF EXISTS keygenejb;

CREATE TABLE quoteejb
  (LOW DECIMAL(14, 2),
   OPEN1 DECIMAL(14, 2),
   VOLUME DOUBLE PRECISION NOT NULL,
   PRICE DECIMAL(14, 2),
   HIGH DECIMAL(14, 2),
   COMPANYNAME VARCHAR(255),
   SYMBOL VARCHAR(255) NOT NULL,
   CHANGE1 DOUBLE PRECISION NOT NULL);

ALTER TABLE quoteejb 
    ADD CONSTRAINT PK_QUOTEEJB PRIMARY KEY (SYMBOL);

CREATE TABLE keygenejb
  (KEYVAL INTEGER NOT NULL,
   KEYNAME VARCHAR(255) NOT NULL);

ALTER TABLE keygenejb 
    ADD CONSTRAINT PK_KEYGENEJB PRIMARY KEY (KEYNAME);
