#!/bin/bash

del game.jar

mvn clean package

copy target/game.jar game.jar
java -jar game.jar
