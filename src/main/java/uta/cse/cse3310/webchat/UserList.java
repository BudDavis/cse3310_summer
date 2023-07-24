package uta.cse.cse3310.webchat;

import java.util.Vector;

import org.java_websocket.WebSocket;

public class UserList {

  // All users connected at this time
  private Vector<User> Users = new Vector<User>();

  public void add(User U) {
    Users.add(U);
  }

  public User get(Integer I) {
    return Users.get(I);
  }

  public Integer size() {
    return Users.size();
  }

  public void del(WebSocket conn) {
    for (Integer i = 0; i < Users.size(); i++) {
      if (Users.get(i).conn == conn) {
        Users.remove(i);
      }
    }
  }

  public void InsertNameForConn(WebSocket conn, String name) {
    // It is a vector, so we search it all
    for (Integer i = 0; i < Users.size(); i++) {
      if (Users.get(i).conn == conn) {
        Users.get(i).Name = name;
      }
    }

  }

  public User conn2user(WebSocket conn) {
    // given a conn, return the user instance
    for (Integer i = 0; i < Users.size(); i++) {
      if (conn == Users.get(i).conn) {
        return Users.get(i);
      }
    }
    return null;
  }

  public String conn2name(WebSocket conn) {
    for (Integer i = 0; i < Users.size(); i++) {
      if (conn == Users.get(i).conn) {
        return Users.get(i).Name;
      }
    }
    return "not found";
  }

  public WebSocket name2conn(String n) {
    for (Integer i = 0; i < Users.size(); i++) {
      if (n == Users.get(i).Name) {
        return Users.get(i).conn;
      }
    }
    return null;
  }
}
