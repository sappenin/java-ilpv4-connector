-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: db/changelog-master.xml
-- Ran at: 6/28/19 4:03 PM
-- Against: fuelling@jdbc:postgresql://localhost:5432/circle_test
-- Liquibase version: 3.6.3
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE databasechangeloglock (ID INTEGER NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITHOUT TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT DATABASECHANGELOGLOCK_PKEY PRIMARY KEY (ID));

-- Initialize Database Lock Table
DELETE FROM databasechangeloglock;

INSERT INTO databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
UPDATE databasechangeloglock SET LOCKED = TRUE, LOCKEDBY = 'fuelling-lm.lan (192.168.86.48)', LOCKGRANTED = '2019-06-28 16:03:34.17' WHERE ID = 1 AND LOCKED = FALSE;

-- Create Database Change Log Table
CREATE TABLE databasechangelog (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED TIMESTAMP WITHOUT TIME ZONE NOT NULL, ORDEREXECUTED INTEGER NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONTEXTS VARCHAR(255), LABELS VARCHAR(255), DEPLOYMENT_ID VARCHAR(10));

-- Changeset db/changelogs/base/changelog.xml::create initial tables::dfuelling
CREATE TABLE ACCOUNT_SETTINGS (ID BIGSERIAL NOT NULL, NATURAL_ID VARCHAR(32) NOT NULL, CREATED_DTTM TIMESTAMP WITHOUT TIME ZONE NOT NULL, MODIFIED_DTTM TIMESTAMP WITHOUT TIME ZONE NOT NULL, VERSION SMALLINT NOT NULL, DESCRIPTION VARCHAR(255), INTERNAL BOOLEAN DEFAULT FALSE NOT NULL, CONNECTION_INITIATOR BOOLEAN DEFAULT FALSE NOT NULL, ILP_ADDR_SEGMENT VARCHAR(512), ACCOUNT_RELATIONSHIP SMALLINT NOT NULL, LINK_TYPE VARCHAR(255) NOT NULL, ASSET_CODE VARCHAR(12) NOT NULL, ASSET_SCALE SMALLINT NOT NULL, SEND_ROUTES BOOLEAN DEFAULT FALSE NOT NULL, RECEIVE_ROUTES BOOLEAN DEFAULT FALSE NOT NULL, MAX_PACKET_AMT BIGINT, MIN_BALANCE BIGINT, MAX_BALANCE BIGINT, SETTLE_THRESHOLD BIGINT, SETTLE_TO BIGINT, MAX_PACKETS_PER_SEC BIGINT, CUSTOM_SETTINGS VARCHAR(8196), CONSTRAINT ACCOUNT_SETTINGS_PKEY PRIMARY KEY (ID), UNIQUE (NATURAL_ID));

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('create initial tables', 'dfuelling', 'db/changelogs/base/changelog.xml', NOW(), 1, '8:7baaad1332696ffa37289ae0ce8e9e92', 'createTable tableName=ACCOUNT_SETTINGS', '', 'EXECUTED', NULL, NULL, '3.6.3', '1759414227');

-- Changeset db/changelogs/base/changelog.xml::hibernate_sequence::dfuelling
CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1;

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('hibernate_sequence', 'dfuelling', 'db/changelogs/base/changelog.xml', NOW(), 2, '8:66d339bfb0b07ca6736080b2bc5a37fd', 'createSequence sequenceName=hibernate_sequence', '', 'EXECUTED', NULL, NULL, '3.6.3', '1759414227');

-- Release Database Lock
UPDATE databasechangeloglock SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

