#!/bin/bash
#
# Parameter: new release version
# 
set -x

##  we do it in another directory 
cd ../..
mkdir -p flyway-test-extensions-release
cd flyway-test-extensions-release

## remove last build 
rm -rf flyway-test-extensions

## clone lastest version from git hub
git clone https://github.com/flyway/flyway-test-extensions.git

## change into mvn directory
cd flyway-test-extensions/flyway-test-extensions

## set new version
mvn versions:set -DnewVersion=$1 -f parent/pom.xml
mvn -N versions:update-child-modules  -DnewVersion=$1

## deploy and tag version
mvn deploy scm:tag -DperformRelease=true -DskipTests=true 


