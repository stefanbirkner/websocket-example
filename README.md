# Jetty WebSocket Server Example

This repository contains a tiny WebSocket server built with Jetty 11. It's
purpose is to show how less you need for a minimal WebSocket server. It's build
with Java 11.

## Usage

First you have to download the Jetty libraries. You need to have wget installed
for running the script

    ./scripts/download_libraries.sh

If you don't have wget you can manually download the dependencies listed in
`src/libraries.txt`.

Now you can start the WebSocketServer with

    java -cp "lib/*" src/WebSocketServer.java
