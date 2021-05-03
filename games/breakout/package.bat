#!/bin/bash

del kodenkel-breakout.jar

mvn clean package

copy target/kodenkel-breakout.jar kodenkel-breakout.jar
java -jar kodenkel-breakout.jar
