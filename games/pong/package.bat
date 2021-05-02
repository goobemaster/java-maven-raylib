#!/bin/bash

del kodenkel-pong.jar

mvn clean package

copy target/kodenkel-pong.jar kodenkel-pong.jar
java -jar kodenkel-pong.jar
