## Example Game: Pong

See the project in the games/pong sub-directory. This is a **very rudimentary** Pong clone.

Use the up/down keys to move the paddle. See the Application class to switch sides, or set which players are controlled by who, e.g.:

````
Pad playerHuman = new Pad(Player.HUMAN, Side.LEFT, WINDOW_WIDTH, WINDOW_HEIGHT);
Pad playerComputer = new Pad(Player.COMPUTER, Side.RIGHT, WINDOW_WIDTH, WINDOW_HEIGHT);
````

![Preview](games/pong/readme.jpg)

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
java -jar game.jar
````
