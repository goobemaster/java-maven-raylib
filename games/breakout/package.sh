#!/bin/bash

rm -f kodenkel-breakout.jar

mvn clean package

cp target/kodenkel-breakout.jar kodenkel-breakout.jar
chmod +x kodenkel-breakout.jar
java -jar kodenkel-breakout.jar
