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
public class ChatRoomsTest {

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
    * The following methods are in ChatRooms
    * public void addUser(User U, String ChatRoomName) {
    * 
    */
   @Test
   public void addUser() {
      // Add a user to a chatroom

      ChatRooms CR = new ChatRooms();

      // First, we need a conn, of type WebSocket
     
      DefaultWebSocketServerFactory webSocketServerFactory = new DefaultWebSocketServerFactory();
      CustomWebSocketAdapter webSocketAdapter = new CustomWebSocketAdapter();
      WebSocketImpl webSocketImpl = webSocketServerFactory.createWebSocket(webSocketAdapter, new Draft_6455());
      WebSocket conn = (WebSocket) webSocketImpl;

      // make a User with no name
      User U = new User(conn);

      // Add it to a chatroom
      CR.addUser(U,"lobby");

      assertTrue(CR.size()==1);  // only one called lobby
   }

   @Test
   public void shouldAnswerWithTrue() {
      DefaultWebSocketServerFactory webSocketServerFactory = new DefaultWebSocketServerFactory();
      CustomWebSocketAdapter webSocketAdapter = new CustomWebSocketAdapter();
      WebSocketImpl webSocketImpl = webSocketServerFactory.createWebSocket(webSocketAdapter, new Draft_6455());
      WebSocket conn = (WebSocket) webSocketImpl;
      System.out.println(conn);
      assertTrue(true);
   }
}
