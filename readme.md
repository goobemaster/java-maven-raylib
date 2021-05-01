In this repository you can find a fully configured project for simple, old-school game development using Java and the bindings for Raylib (https://www.raylib.com/).

![Preview](boilerplate-project/readme.jpg)

## Pre-requisites

Java 1.8
Maven 3.6

or newer

## Package and Test

On Linux simply run the "package.sh" script.
On Windows simply run the "package.bat" script (untested).

The resulting jar will be executable and include all dependencies (so called "fat-jar").

To run the built jar manually:

````
java -jar game.jar
````

## Main Class

The main class in the boilerplate project simply contains the default demo source of Raylib.

src/main/java/com/kodenkel/game/Application.java

Don't forget to update the Maven configuration (pom.xml) as well as paths to reflect your own preferred package names. **Do not use "com.kodenkel.game"!**
