package com.klishov.pow;

import java.util.Scanner;

public class KMR_Application {

    private final Thread thread;

    public KMR_Application(int proof, String previousHash, String hashToGuess) {
        this.thread = new Thread(() -> {
            KMR_Blockchain blockchain = new KMR_Blockchain(proof, previousHash, hashToGuess);
            String hash = KMR_Blockchain.kmrHash(blockchain.kmrLastBlock());
            String proofOfWork = Integer.toString(blockchain.kmrProofOfWork(proof));
            kmrOutput("Starting hash: " + hash);
            kmrOutput("Received nonce: " + proofOfWork);
            kmrOutput("Received hash: " + blockchain.kmrGetGuessHash(proof + proofOfWork));
        });
        thread.start();
    }

    public void kmrStopBlockchain() {
        thread.interrupt();
    }

    public static void main(String[] args) throws InterruptedException {
        KMR_Application app = new KMR_Application(4122001, "Klishov", "12");
        Thread.sleep(500);
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
