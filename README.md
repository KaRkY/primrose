# Primrose

Primrose is an example app for demonstrating use of few technologies.

## Database

Primrose uses Postgresql for its database. All creation scripts and scripts for cleanup are located in [primrose-database](primrose-database). 
For creation scripts to work you must have few extensions installed: btree_gist and pgcrypto.

I have them installed in extensions schema:
CREATE EXTENSION btree_gist WITH SCHEMA extensions;

## Test data
In [test-data](primrose-test-data) is located [data.json](primrose-test-data/data.json) with some example data for testing. If you run backend server you can execute: 
'''bash
npm install
npm run post
'''
and all the data will be transformed and posted to http://localhost:9080/import

## Backend

## Frontend
