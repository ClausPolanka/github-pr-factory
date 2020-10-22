#!/bin/sh

mvn clean package

java -jar ./target/github-pr-factory-1.3.0-SNAPSHOT-jar-with-dependencies.jar $*
