#!/bin/bash

del kodenkel-bankrupt-dash.jar

mvn clean package

copy target/kodenkel-bankrupt-dash.jar kodenkel-bankrupt-dash.jar
java -jar kodenkel-bankrupt-dash.jar
