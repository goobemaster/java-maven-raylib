## Example Game: Scorched

This is a **very rudimentary** Scorched Earth clone.

Your objective is eliminating the opposing tank. The first tank to blow up the other tank is the winner.

Use the arrow keys to set the angle of the turret, and the speed of the missile.

![Preview](readme.jpg)

## Pre-requisites

- Java 1.8
- Maven 3.6

or newer

Help on my blog: <a href="https://www.kodenkel.com/how-to/java-jdk-maven-installation-windows" target="_blank">How To Install the Java JDK and Maven on Windows</a>

Debian/Ubuntu

````
sudo apt-get install default-jdk maven
````

## Package and Test

On Linux simply run the "package.sh" script.

On Windows simply run the "package.bat" script (untested).

The resulting jar will be executable and include all dependencies (so called "fat-jar").

To run the built jar manually:

````
java -jar kodenkel-scorched.jar
````
