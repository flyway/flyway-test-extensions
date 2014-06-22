#!/bin/bash
set -x

STORE_PWD=$PWD
## 
cd ..

## change into mvn directory
cd flyway-test-extensions

## set new version
mvn versions:set -DnewVersion=$1 -f parent/pom.xml
mvn -N versions:update-child-modules  -DnewVersion=$1

## set new versions for sample parts
cd flyway-test-samples
mvn versions:set -DnewVersion=$1 -f flyway-test-samples-parent/pom.xml
mvn -N versions:update-child-modules  -DnewVersion=$1

# go back 
cd $STORE_PWD



