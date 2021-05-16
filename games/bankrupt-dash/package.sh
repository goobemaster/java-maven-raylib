#!/bin/bash

rm -f kodenkel-bankrupt-dash.jar

mvn clean package

cp target/kodenkel-bankrupt-dash.jar kodenkel-bankrupt-dash.jar
chmod +x kodenkel-bankrupt-dash.jar
java -jar kodenkel-bankrupt-dash.jar
