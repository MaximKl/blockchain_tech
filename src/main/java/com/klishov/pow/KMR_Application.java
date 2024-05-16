package com.klishov.pow;

import java.util.Scanner;

public class KMR_Application {

    private final Thread thread;

    public KMR_Application(int proof, String previousHash, long hashToGuess) {
        this.thread = new Thread(() -> {
            KMR_Blockchain blockchain = new KMR_Blockchain(proof, previousHash);
            String hash = KMR_Blockchain.kmrHash(blockchain.kmrLastBlock());
            kmrOutput("Received hash: " + hash);
            kmrOutput("Algorithm test: " + blockchain.kmrProofOfWork(proof, hashToGuess));
        });
        thread.start();
    }

    public void kmrStopBlockchain() {
        thread.interrupt();
    }

    public static void main(String[] args) throws InterruptedException {
        KMR_Application app = new KMR_Application(4122001, "Klishov", 12);
        Thread.sleep(1*1000);
        System.out.println("Press S to stop blockchain");
        Scanner scanner = new Scanner(System.in);

        while (!scanner.next().equalsIgnoreCase("S")) {
        }

        app.kmrStopBlockchain();
        scanner.close();
        System.exit(0);
    }

    public static void kmrOutput(Object s) {
        System.out.println("--> " + s);
    }
}
