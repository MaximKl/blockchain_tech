package com.klishov.pow;

import com.google.common.hash.Hashing;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class KMR_Application {

    private final Thread thread;

    public KMR_Application(int proof, String previousHash) {
        this.thread = new Thread(() -> {
            KMR_Blockchain blockchain = new KMR_Blockchain(proof, previousHash);
            String hash = KMR_Blockchain.kmrHash(blockchain.kmrLastBlock());
            kmrOutput("Received hash: " + hash);
            kmrOutput("Algorithm test: " + kmrProofOfWork(proof));
        });
        thread.start();
    }

    public void kmrStopBlockchain() {
        thread.interrupt();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        KMR_Application app = new KMR_Application(4122001, "Klishov");
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

    public static int kmrProofOfWork(int lastProofOfWork) {
        int proof = 0;
        while (!kmrIsProofValid(lastProofOfWork, proof)) {
            proof++;
        }
        return proof;
    }

    public static boolean kmrIsProofValid(int lastProof, int proof) {
        String guessString = Integer.toString(lastProof) + Integer.toString(proof);
        String guessHash = Hashing.sha256().hashString(guessString, StandardCharsets.UTF_8).toString();
        return guessHash.endsWith("12");
    }
}
