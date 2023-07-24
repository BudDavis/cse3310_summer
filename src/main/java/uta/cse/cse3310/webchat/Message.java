package uta.cse.cse3310.webchat;

public class Message {
    // The purpose of this class is to transmit messages between the server and the
    // clients

    public Integer from; // This varialbe will let the program identify the sender by their ID

    public Integer to; // This variable will let the program identify the receiver by their ID

    public String content; // This variable will store the content of the message sent or received, which
                           // would be a text

    public void setfrom(Integer from) {
        this.from = from;
    }

    public void setto(Integer to) {
        this.to = to;
    }

    public void setcontent(String content) {
        this.content = content;
    }

    public Integer getfrom() {
        return from;
    }

    public Integer getto() {
        return to;
    }

    public String getcontent() {
        return content;
    }
}
