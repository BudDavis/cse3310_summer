package uta.cse.cse3310.webchat;

import org.java_websocket.WebSocket;
import java.util.Vector;

public class ChatRooms {

    Vector<ChatRoom> crs;

    public ChatRooms() {
        crs = new Vector<ChatRoom>();
    }

    public int size() {
        return crs.size();
    }

    public ChatRoom get(int i) {
        return crs.get(i);
    }

    public void add(ChatRoom C) {
        crs.add(C);
    }

    public void addUser(User U, String ChatRoomName) {
        // Add the user to the chatroom named ChatRoomName.
        // If the chatroom does not exist, it is added

        Integer found = -1;
        for (Integer i = 0; i < crs.size(); i++) {
            if (crs.get(i).Name == ChatRoomName) {
                found = i;
            }

        }
        if (found == -1) {
            ChatRoom cr = new ChatRoom(ChatRoomName);
            cr.addUser(U);
            crs.add(cr);
        } else {
            // Add this user
            crs.get(found).addUser(U);
        }
    }

}
