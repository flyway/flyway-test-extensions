#!/bin/bash
#
# Parameter: new release version
# 
set -x

# set GPG TTY
export GPG_TTY=$(tty)

##  we do it in another directory 
cd ../..
mkdir -p flyway-test-extensions-release
cd flyway-test-extensions-release

## remove last build 
rm -rf flyway-test-extensions

## clone lastest version from git hub
git clone git@flyway.github.com:flyway/flyway-test-extensions.git

## change into mvn directory
cd flyway-test-extensions/flyway-test-extensions

## set new version
mvn versions:set -DnewVersion=$1 -f parent/pom.xml
mvn -N versions:update-child-modules  -DnewVersion=$1

## set new versions for sample parts
cd flyway-test-samples
mvn versions:set -DnewVersion=$1 -f flyway-test-samples-parent/pom.xml
mvn -N versions:update-child-modules  -DnewVersion=$1

# go back to build directory
cd ..
echo "*******************************************************"
echo "For github deploy a personal access token is necessary!"
echo "*******************************************************"
echo " "

## deploy and tag version
### during release one of the test will always fail
mvn deploy scm:tag -DperformRelease=true  -DskipTests=true


