package uta.cse.cse3310.webchat;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class App extends WebSocketClient {
    private String id;

    public App(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public App(URI serverURI) {
        super(serverURI);
    }

    public App(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // convert the bytes to a string
        // handshake response
        System.out.println("Opened connection");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received: " + message);
        saveToLog("Received: " + message);

        // check if the contains the "client_id":
        if (message.contains("client_id")) {
            // Create a Gson instance
            Gson gson = new Gson();

            // Deserialize the JSON string into a Java object
            JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
            // Extract the client_id value
            id = jsonObject.get("client_id").getAsString();
            System.out.println("Client ID: " + id);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: "
                + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public static void main(String[] args) throws URISyntaxException {
        int port = 8081;
        App c = new App(new URI("ws://localhost:" + port));
        c.connect();
    }

    public static void saveToLog(String s) {
        try {
            // Read the existing content of the file, if any
            List<JsonElement> logs = new ArrayList<>();
            String filePath = "log.txt";
            if (Files.exists(Paths.get(filePath))) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                if (!content.isEmpty()) {
                    JsonArray jsonArray = new Gson().fromJson(content, JsonArray.class);
                    for (JsonElement jsonElement : jsonArray) {
                        logs.add(jsonElement);
                    }
                }
            }

            // Create a new JSON object for the current log entry
            JsonObject jsonLog = new JsonObject();
            jsonLog.addProperty("message", s);

            LocalDateTime currentTime = LocalDateTime.now();

            // Define the desired date-time format using DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Format the LocalDateTime object to a string
            String timestamp = currentTime.format(formatter);

            // Print the timestamp string

            jsonLog.addProperty("timestamp", timestamp);

            // Add the new log entry to the existing list of logs
            logs.add(jsonLog);

            // Convert the list of logs to a JSON array
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonArray jsonArray = new JsonArray();
            for (JsonElement log : logs) {
                jsonArray.add(log);
            }

            // Write the JSON array to the file
            try (FileWriter fw = new FileWriter(filePath)) {
                fw.write(gson.toJson(jsonArray));
            } catch (IOException e) {
                System.out.println("Error writing to log file");
            }
        } catch (Exception e) {
            System.out.println("Error creating JSON object");
        }
    }
}