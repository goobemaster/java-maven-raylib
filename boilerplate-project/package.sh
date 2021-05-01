#!/bin/bash

rm -f game.jar

mvn clean package

cp target/game.jar game.jar
chmod +x game.jar
java -jar game.jar
