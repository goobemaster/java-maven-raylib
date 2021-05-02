#!/bin/bash

rm -f kodenkel-pong.jar

mvn clean package

cp target/kodenkel-pong.jar kodenkel-pong.jar
chmod +x kodenkel-pong.jar
java -jar kodenkel-pong.jar
