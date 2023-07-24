package uta.cse.cse3310.webchat;

import org.java_websocket.WebSocket;
import java.util.Vector;

public class ChatRoom {
    public String Name;
    public Vector<User> users;

    public ChatRoom() {
        users = new Vector<>();
    }

    public ChatRoom(String N) {
        Name = N;
        users = new Vector<>();
    }

    public void addUser(User U) {
        users.add(U);
    }

    public void removeUser(WebSocket U) {
        users.remove(U);
    }

    public void broadcast(String message) {
        // for (WebSocket user : users) {
        // user.send(message);
        // }
    }
}
