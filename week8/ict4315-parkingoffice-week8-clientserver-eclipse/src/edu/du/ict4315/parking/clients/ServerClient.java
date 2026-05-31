/////////////////////////////
// File: ServerClient.java
// Author: Instructor
// This file contains the client's list of displayCommands it is willing to
// format and send to the server.
// The main lists the known displayCommands
// With parameters, the main will run those displayCommands
// The server must receive data matching the ParkingService interface.
/////////////////////////////
package edu.du.ict4315.parking.clients;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.du.ict4315.parking.support.PropertiesUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

public class ServerClient {

    private static final Logger logger = Logger.getLogger(ServerClient.class.getName());
    private static final Gson gson = new Gson();

    public static final String[][] COMMANDS = new String[][]{
        {"a. Register Customer", "CUSTOMER", "FirstName", "LastName", "PhoneNumber"},
        {"b. Register Vehicle", "CAR", "License", "Customer"},
        {"c. List Customers", "LISTCUST"},

    };

    private static final int PORT = 7777;
    private static final String SERVER = "localhost";

    private ServerClient() {
    }

    public static List<String> runCommand(String command, Map<String, String> data)
            throws IOException {

        InetAddress host = InetAddress.getByName(SERVER);
        ArrayList<String> response = new ArrayList<>();

        try (Socket link = new Socket(host, PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(link.getInputStream()));
             PrintWriter output = new PrintWriter(link.getOutputStream())) {

            logger.info("You are now connected to: " + host.getHostAddress());

            JsonObject json = new JsonObject();
            json.addProperty("command", command);
            JsonObject fields = new JsonObject();
            for (Entry<String, String> entry : data.entrySet()) {
                fields.addProperty(entry.getKey(), entry.getValue());
            }
            json.add("data", fields);

            output.println(gson.toJson(json));
            output.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("end")) break;
                response.add(line);
            }
        }
        return response;
    }

    public static Map<String, ClientCommand> displayCommands() {
        Map<String, ClientCommand> commands = new TreeMap<>();
        for (String[] description : COMMANDS) {
            commands.put(description[0], new ClientCommand(description[0].substring(2), description[1],
                    Arrays.asList(description).subList(2, description.length)));
        }
        return commands;
    }

    public static Map<String, ClientCommand> commands() {
        Map<String, ClientCommand> commands = new TreeMap<>();
        for (String[] description : COMMANDS) {
            commands.put(description[1], new ClientCommand(description[0].substring(2), description[1],
                    Arrays.asList(description).subList(2, description.length)));
        }
        return commands;
    }

    public static void main(String[] args) throws IOException {

        if (args.length == 0 || args[0].equals("LIST")) {
            System.out.println(
                    "Usage: $ java " + ServerClient.class.getName() + " COMMAND label1=value1 label2=value2 ...");
            for (String[] description : COMMANDS) {
                System.out.format("%s: %s ", description[0], description[1]);
                for (int i = 2; i < description.length; ++i) {
                    System.out.format("%s=value ", PropertiesUtilities.displayNameToPropertyName(description[i]));
                }
                System.out.println();
            }
            return;
        }

        ClientCommand command = commands().get(args[0]);
        if (command == null) {
            System.out.println("Unrecognised command: " + args[0]);
            System.out.print("Known commands: ");
            String comma = "";
            for (String[] description : COMMANDS) {
                System.out.print(comma + description[1]);
                comma = ", ";
            }
            System.out.println();
            return;
        }

        Map<String, String> values = new LinkedHashMap<>();
        for (String label : command.fieldNames()) {
            for (int i = 0; i < args.length; ++i) {
                if (args[i].toLowerCase().startsWith(PropertiesUtilities.displayNameToPropertyName(label))) {
                    values.put(label, args[i].split("=")[1]);
                    break;
                }
            }
        }

        System.out.println("Command: " + command + " " + String.join(", ", args));
        for (String out : ServerClient.runCommand(args[0], values)) {
            System.out.println(out);
        }
    }
}