//////////////////////////
// File: Server.java
// Author: R Judd, modified by M I Schwartz
// This file implements a String message-oriented server to allow clients to send
// commands to the Parking Office server.
// Note: Assignment 8 examines alternate ways of exchanging messages.
// Note: Assignment 9 examines ways of supporting paralellism.
//////////////////////////
package edu.du.ict4315.parking.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.du.ict4315.parking.service.ParkingService;
import edu.du.ict4315.parking.Address;
import edu.du.ict4315.parking.RealParkingOffice;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    static {
        System.setProperty(
                "java.util.logging.SimpleFormatter.format",
                "%1$tc %4$-7s (%2$s) %5$s %6$s%n");
    }

    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static final Gson gson = new Gson();

    static {
        logger.setLevel(Level.FINE);
    }

    private final int PORT = 7777;
    private final ParkingService service;

    public Server(ParkingService service) {
        this.service = service;
    }

    public void startServer() throws IOException {
        logger.info("Starting server: " + InetAddress.getLocalHost().getHostAddress());
        ExecutorService executor = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket client = serverSocket.accept();
                executor.submit(() -> handleClient(client));
            }
        } finally {
            executor.shutdown();
        }
    }

    private void handleClient(Socket client) {
        try (PrintWriter pw = new PrintWriter(client.getOutputStream());
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(client.getInputStream()))) {

            String output;
            try {
                String requestLine = reader.readLine();
                JsonObject json = gson.fromJson(requestLine, JsonObject.class);
                String command = json.get("command").getAsString();
                JsonObject dataJson = json.getAsJsonObject("data");

                Map<String, String> data = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : dataJson.entrySet()) {
                    data.put(entry.getKey(), entry.getValue().getAsString());
                }

                output = service.handleInput(command, data);
            } catch (RuntimeException ex) {
                ex.printStackTrace();
                output = ex.getMessage();
            }

            pw.println(output);
            pw.println("end");
            pw.flush();

        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to read from client.", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to close client socket.", e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        RealParkingOffice parkingOffice = new RealParkingOffice();
        parkingOffice.setParkingOfficeName("DU Parking Office -- Test");
        Address address = new Address.Builder()
                .withStreetAddress1("2130 S. High St.")
                .withCity("Denver")
                .withState("CO")
                .withZip("80210")
                .build();
        parkingOffice.setParkingOfficeAddress(address);
        ParkingService service = new ParkingService(parkingOffice);
        new Server(service).startServer();
    }
}
