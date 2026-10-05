# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Phase 2: Server Design Sequence Diagram

[Chess Server Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtG5Dgbl0e7L4vN4fRftkGFfMl4e3C+nxPO+SyvgC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatLPqMJpEuGFoctyvIGoKwowKK4qutKSaXjBCpKiqmGdn+abITy2a5pg3GdsWaYARW46tl806zs2ElfiJnbZD2MD9oOvRPiOowLpOfTSY2skTn0S6cKu3h+IEXgoOge4Hr4zDHukmSYEpF5FNQ17SAAorunn1J5zQtAY6gJGg3R6XO35stxVy6UGMnzoZEFAh20FOvKMDwfYdkBnF+loBhcpYQSOEsuUHAoNwmSxv64XoCRTJuuR5SRMMEA0DAtVoKGpGNcxblpbBKkDjAsRyO0GW2b6I0+G6jrOsmkElqpI3yGA42ZXZ02zUJfUiX2w2jWtrIbVNOazQpfUuWA+1OCtY3HZNzBndKpjLqZ66BJCtq7tCMAAOKjqyDmns557MKl15-b5AX2KOYW5RFF2-otomxXWeXjJJSWpmymFwdCAOjKoOXo3OBUwRqJWkjA5JgNVJMznl9VmhGhSWjAlExkGNFhJ13UNUxqXgjA1XxoVlM9aVGUE4DsLM2RkYchzPK2sAyr-aODqMeau2FWSgNzYmC3JTxMtE-xCB5ijwlXqjsNE5jFSNAcF2nFdN2PvbaiO87ZgmZ4ZkbtgPhQNg3DwLqmQa6MKSOWeOTgyxJTlDeDQw3DwQI+gQ5ewAcqOLu21FKMxZ1mPVlMedAVjgnW0L6WenqhMoLCcCRygzeoRi5MDRLAvU7T9OdfLvVs0rnMi9z2hCrzWdddrEb14NovaIbRUyFT7owI3mTN7CXshgvTUiza0coGLFN9dFEdervo4W1bJs2+5dujgAktI5dtq7hTu0Nan9C9h-L+Rk-YrgDh9AIlgKrwWSDAAAUhAHkZ9Ag6AQKABsoME641tinKolI7wtC9vDUm2dehh2ANAqAcAIDwSgLMIB0hC4v2LibUuc8QEgXQVQmhdDOGV3fp-RKtcn5L3KAAKyQWgPeiCeSdxQGibua8+4szwnTIMDN4ojyYmPcoE8V7yBnh1Oe-MWbPwGuUAxwBlHYUlgPCke9GHaJ1ro5WvJm48xpoI0xCsxFnwvr3Wx-ct5+C0HfUY+9NbaGcazdk5RKTYDCYYDxq9OJXxLv-O6R0toshETjXWL8bpZPGs9dQkVLpgyKYdEpM0XpvQgeZAIXhKFdi9LAYA2Aw6EHiIkWOIMro4MKZULyPk-IBWMOU5GbDt7cDwDASEijYh5KgknYWIBZlwh7s6Yqdit7rLaXLHxo84nplau1NWCA6IdFUAwwGsxOoaDScbVMVTVo1O2nXCGaZlrVNZKUx5RcKkJ1efdHJ5owFAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
