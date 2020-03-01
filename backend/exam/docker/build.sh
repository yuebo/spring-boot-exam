#!/usr/bin/env bash
rm -f *.jar
mvn clean install -f ../pom.xml -P prod -Dmaven.test.skip=true
cp ../target/exam.jar app.jar
docker build . -t mooncakexyb/exam:0.3