package uta.cse.cse3310.webchat;

import java.util.Collections;
import java.util.Vector;

// Contains the message that is sent to the client !
// This naming could be better.... it only makes sense on the
// javascript side
public class RecvChatMessage {
    public Vector<String> Users;
    public Vector<String> Chatrooms;
    public String Text;

    public RecvChatMessage() {
        Users = new Vector<String>();
        Chatrooms = new Vector<String>();
    }
}
