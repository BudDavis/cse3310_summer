package uta.cse.cse3310.webchat;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

// Way more imports than are needed.
// Sloppy work....
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLContext;

import org.java_websocket.drafts.Draft;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.Handshakedata;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.server.DefaultWebSocketServerFactory;
import org.java_websocket.server.DefaultWebSocketServerFactoryTest;
import org.java_websocket.WebSocketAdapter;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.Handshakedata;

/**
 * Unit test for simple App.
 */
public class UserListTest
{



    private static class CustomWebSocketAdapter extends WebSocketAdapter {
       @Override
       public void onWebsocketMessage(WebSocket conn, String message) {
   
       }
   
       @Override
       public void onWebsocketMessage(WebSocket conn, ByteBuffer blob) {
   
       }
   
       @Override
       public void onWebsocketOpen(WebSocket conn, Handshakedata d) {
   
       }
   
       @Override
       public void onWebsocketClose(WebSocket ws, int code, String reason, boolean remote) {
   
       }
   
       @Override
       public void onWebsocketClosing(WebSocket ws, int code, String reason, boolean remote) {
   
       }
   
       @Override
       public void onWebsocketCloseInitiated(WebSocket ws, int code, String reason) {
   
       }
   
       @Override
       public void onWebsocketError(WebSocket conn, Exception ex) {
   
       }
       @Override
       public void onWriteDemand(WebSocket conn) {
   
    }

    @Override
    public InetSocketAddress getLocalSocketAddress(WebSocket conn) {
      return null;
    }

    @Override
    public InetSocketAddress getRemoteSocketAddress(WebSocket conn) {
    return null;
    }
    }

    /**
     *

UserList has the following methods:
  public void add(User U) {
  public User get(Integer I) {
  public Integer size() {
  public void del(WebSocket conn) {
  public void InsertNameForConn(WebSocket conn, String name) {
  public User conn2user(WebSocket conn) {
  public String conn2name(WebSocket conn) {
  public WebSocket name2conn(String n) {
  
     */
    @Test
    public void checkAdd()
    {
         // Add 100 users, all with the same name 
         UserList UL = new UserList();
         for (int i=0;i<100;i++) {
            User U = new User("name");
            UL.add(U);
         }
         assertTrue( UL.size() == 100 );
         
    }
    @Test
    public void shouldAnswerWithTrue()
    {
        DefaultWebSocketServerFactory webSocketServerFactory = new DefaultWebSocketServerFactory();
        CustomWebSocketAdapter webSocketAdapter = new CustomWebSocketAdapter();
        WebSocketImpl webSocketImpl = webSocketServerFactory.createWebSocket(webSocketAdapter, new Draft_6455());
        WebSocket conn = (WebSocket) webSocketImpl;
        System.out.println(conn);
        assertTrue( true );
    }
}
