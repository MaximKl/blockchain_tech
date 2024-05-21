package com.klishov.pow;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class KMR_Main {

    public static void main(String[] args) throws InterruptedException {
        Map<KMR_Network, KMR_Server> networks = new HashMap<>();
        String hashToGuess = "12";
        int port = 4566;

        for (int i = 1; i < 5; i++)
            networks.put(
                    new KMR_Network(i, String.format("Network with end hash - %s and port %d", hashToGuess, port + i)),
                    new KMR_Server(port + i, 4122001, "Klishov", hashToGuess));

        Thread.sleep(500);
        System.out.println("Press S to stop whole application");
        for (KMR_Network network : networks.keySet())
            System.out.println(network.name() + " started. " + "To start or stop network press " + network.id());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.next();
            if (input.equalsIgnoreCase("S"))
                break;
            if (kmrIsNumber(input) && networks.keySet().stream().anyMatch(n -> n.id() == Integer.parseInt(input))) {
                KMR_Network network = networks.keySet()
                        .stream().filter(n -> n.id() == Integer.parseInt(input)).findFirst().get();
                KMR_Server server = networks.get(network);
                if (server.isStopped()) {
                    System.out.println("Network " + input + " working");
                    networks.put(network, (KMR_Server) server.clone());
                } else {
                    System.out.println("Network " + input + " stopped");
                    server.kmrStopBlockchain();
                }
            } else {
                System.out.println("Wrong input!");
            }
        }
        networks.forEach((key, value) -> value.kmrStopBlockchain());
        scanner.close();
        System.exit(0);
    }

    public static boolean kmrIsNumber(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
