package uta.cse.cse3310.webchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Vector;

import org.java_websocket.drafts.Draft;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WebChat extends WebSocketServer {

    // All users connected at this time
    // Vector<User> Users = new Vector<User>();
    UserList Users = new UserList();

    // All chatrooms that exist at this time
    Vector<ChatRoom> ActiveRooms = new Vector<ChatRoom>();

    public WebChat(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public WebChat(InetSocketAddress address) {
        super(address);
    }

    public WebChat(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        // conn.send("Welcome to the server!");

        String W = (conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
        System.out.println(W); // Prints the statement to stdout to indicate a new connection has opened

        // This is the first time we have seen this client.
        // So, we need to make an instance of a User class
        User U = new User(conn);

        // And add it to the list of users
        Users.add(U);

        // And we need to add it to the 'lobby', because it has to be somewhere.
        // (at this point, maybe a vector was not the ideal data structure...)
        Integer found = -1;
        for (Integer i = 0; i < ActiveRooms.size(); i++) {
            if (ActiveRooms.get(i).Name == "lobby") {
                found = i;
            }
            System.out.print(ActiveRooms.get(i).Name + " ");
        }
        // Add this new user to the lobby
        ActiveRooms.get(found).addUser(U);

        // Next, we send the information that is needed by the client web page.
        // At this time we don't know the users name or anything, but we can tell
        // the user all the other users and all the chatrooms that are available.
        RecvChatMessage InitMessage = new RecvChatMessage();
        for (Integer i = 0; i < Users.size(); i++) {
            InitMessage.Users.add(Users.get(i).Name);
        }
        for (Integer i = 0; i < ActiveRooms.size(); i++) {
            InitMessage.Chatrooms.add(ActiveRooms.get(i).Name);
        }

        // Turn it into json
        Gson gson = new Gson();
        String jsonString = gson.toJson(InitMessage);
        System.out.println("Sending " + jsonString);

        conn.send(jsonString);
        // This is where it should be logged.
        App.saveToLog(jsonString);

        // ChatRoom.addUser(conn);
        // Id++;

        // Gson gson = new Gson();
        // String json = gson.toJson("Client ID: " + Id);
        // conn.send(json); // sends the client's ID to the client browser
        // System.out.println(json); // prints the client's ID to stdout

        // String wson = gson.toJson(W);
        // broadcast(wson); // sends the message to the entire chatroom
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " has left the room!");
        System.out.println(conn + " has left the room!");

        // TODO: Remove a user when the connection is closed
        Users.del(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // Allows onMessage to receive contents from the Message class to print to
        // stdout and to transmit the message to the chat room
        System.out.println(conn + ": " + message);

        // Here is the place to log the messages coming in
        App.saveToLog(message);

        // Process the message
        //
        // This is a brute force solution. There are many better ways to do it!
        //
        if (message.contains("\"Type\":\"Login\"")) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            LoginMessage U = gson.fromJson(message, LoginMessage.class);
            System.out.println("Got a login message");
            // for now, all we have to do is put it in the list of users.
            Users.InsertNameForConn(conn, U.Name);
        } else if (message.contains("\"Type\":\"Chatroom\"")) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            SelectChatMessage U = gson.fromJson(message, SelectChatMessage.class);
            System.out.println("Got a chatroom message");

            // A user can be in 1 and only 1 chatroom. So, before we start, let's
            // delete the user from the chatroom it is currently in.
            String theUserName = Users.conn2name(conn);

            for (Integer i = 0; i < ActiveRooms.size(); i++) {
                for (Integer j = 0; j < ActiveRooms.get(i).users.size(); j++) {
                    if (theUserName.equals(ActiveRooms.get(i).users.get(j).Name)) {
                        ActiveRooms.get(i).users.remove(ActiveRooms.get(i).users.get(j));
                        break;
                    }
                }
            }

            Integer found = -1;
            for (Integer i = 0; i < ActiveRooms.size(); i++) {
                if (ActiveRooms.get(i).Name.equals(U.Name)) {
                    found = i;
                }
            }
            // it does not exist, so make it.
            if (found == -1) {
                ActiveRooms.add(new ChatRoom(U.Name));
                System.out.println("create a chatroom named " + U.Name + " completed");
            }

            // lets look again, in case we added to it.
            found = -1;
            for (Integer i = 0; i < ActiveRooms.size(); i++) {
                if (ActiveRooms.get(i).Name.equals(U.Name)) {
                    found = i;
                }
            }
            // found is the chatroom where we need to be.
            // so, let's go put the User in that chatroom
            // Need to find the User instance, so we can add it to the chatroom
            // all we have is a 'conn' object when this routine is called
            // Add them now
            ActiveRooms.get(found).users.add(Users.conn2user(conn));
        } else if (message.contains("\"Type\":\"Text\"")) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            SendChatMessage U = gson.fromJson(message, SendChatMessage.class);

            // To send the message, we need to find the chatroom we are in.
            // And then send the message to all the other users in the same
            // chatroom.

            // This function is called async when messages arrive. All we have
            // is a 'conn'. So the first thing is to relate the 'conn' to a
            // 'user name'
            User CurrentUser = Users.conn2user(conn);
            //
            // Is this "conn" (meaning the user associated with it, in this chatroom
            for (Integer i = 0; i < ActiveRooms.size(); i++) {
                Integer InThisChatroom = 0;
                ChatRoom CR = ActiveRooms.get(i);
                for (Integer j = 0; j < CR.users.size(); j++) {
                    if (CurrentUser.Name.equals(CR.users.get(j).Name)) {
                        InThisChatroom = 1;
                    }
                }
                if (InThisChatroom > 0) {
                    for (Integer j = 0; j < CR.users.size(); j++) {
                        System.out.println("\t\tsend the message for chatroom " + CR.Name + " from user "
                                + CurrentUser.Name + " to user " + CR.users.get(j).Name);
                        SendChatMessage S = new SendChatMessage();
                        S.Text = U.Text;
                        S.From = CurrentUser.Name;
                        Gson gs = new Gson();
                        String jsonString = gs.toJson(S);
                        Users.name2conn(CR.users.get(j).Name).send(jsonString);
                    }
                }
            }

        }
        // Next, we send the information that is needed by the client web page.
        // At this time we don't know the users name or anything, but we can tell
        // the user all the other users and all the chatrooms that are available.
        RecvChatMessage InitMessage = new RecvChatMessage();
        for (Integer i = 0; i < Users.size(); i++) {
            InitMessage.Users.add(Users.get(i).Name);
        }
        for (Integer i = 0; i < ActiveRooms.size(); i++) {
            InitMessage.Chatrooms.add(ActiveRooms.get(i).Name);
        }

        // Turn it into json
        Gson gson = new Gson();
        String jsonString = gson.toJson(InitMessage);
        System.out.println("Sending " + jsonString);

        broadcast(jsonString);
        // GsonBuilder builder = new GsonBuilder();
        // Gson gson = builder.create();
        // Message U = gson.fromJson(message, Message.class);
        // System.out.println(U);

        // String json;
        // json = gson.toJson(U);

        // System.out.println(json);
        // broadcast(json);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        broadcast(message.array());
        System.out.println(conn + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
        }
    }

    @Override
    public void onStart() {

        // There must be at least 1 chatroom to start out with,
        // let's call it the 'lobby'...
        ChatRoom Lobby = new ChatRoom("lobby");
        ActiveRooms.add(Lobby);

        System.out.println("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    public static void main(String[] args) throws IOException {

        int hport = 8080;
        HttpServer H = new HttpServer(hport, "./html");
        H.start();
        System.out.println("HTTP Server started on port " + hport);

        int wport = 8081;
        WebChat w = new WebChat(wport);
        w.start();
        System.out.println("WebChat server started on port: " + w.getPort());
    }

}
