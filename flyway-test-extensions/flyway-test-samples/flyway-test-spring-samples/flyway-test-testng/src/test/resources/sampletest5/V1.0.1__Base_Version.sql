--
-- Copyright (C) 2011-2023 the original author or authors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--         http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

/*
 * Base test for h2 tests
*/



CREATE TABLE CUSTOMER (
    cus_id NUMBER(38,0)  CONSTRAINT cus_ID_NN NOT NULL,
    cus_name VARCHAR(50)  CONSTRAINT cus_NAME_NN NOT NULL
);

CREATE INDEX CUS_CUS_ID_I ON CUSTOMER(CUS_ID) ;
ALTER TABLE CUSTOMER ADD CONSTRAINT CUS_ID_PK PRiMARY KEY (CUS_ID);
ALTER TABLE CUSTOMER ADD CONSTRAINT CUS_NAME_UK UNIQUE (CUS_NAME);

CREATE SEQUENCE CUS_SEQ START WITH 1000 INCREMENT by 10;

