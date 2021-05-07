#!/bin/bash

del kodenkel-scorched.jar

mvn clean package

copy target/kodenkel-scorched.jar kodenkel-scorched.jar
java -jar kodenkel-scorched.jar
