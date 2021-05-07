#!/bin/bash

rm -f kodenkel-scorched.jar

mvn clean package

cp target/kodenkel-scorched.jar kodenkel-scorched.jar
chmod +x kodenkel-scorched.jar
java -jar kodenkel-scorched.jar
