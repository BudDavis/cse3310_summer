package uta.cse.cse3310.webchat;

import java.util.Vector;

import org.java_websocket.WebSocket;

public class User {
    String Name;
    // a conn is needed to be able to know where to send the messages
    WebSocket conn;

    public User() {

    }

    public User(String N) {
        Name = N;
        conn = null;
    }

    public User(WebSocket C) {
        conn = C;
        Name = "";
    }

    public User(WebSocket C, String N) {
        conn = C;
        Name = N;
    }

}
