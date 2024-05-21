package com.klishov.pow;

import com.google.gson.Gson;
import spark.Service;

import java.util.*;

public class KMR_Server implements Cloneable {

    private Service kmrService;
    private Thread kmrThread;
    public int kmrPort;
    private int kmrProof;
    private String kmrPreviousHash;
    private String kmrHashToGuess;
    private final KMR_Blockchain kmrBlockchain;

    public KMR_Server(int port, int proof, String previousHash, String hashToGuess) {
        this.kmrBlockchain = new KMR_Blockchain(proof, previousHash, hashToGuess);
        kmrInit(port, proof, previousHash, hashToGuess);
        kmrThread.start();
    }

    private KMR_Server(int port, int proof, String previousHash, String hashToGuess, KMR_Blockchain kmrBlockchain) {
        this.kmrBlockchain = kmrBlockchain;
        kmrInit(port, proof, previousHash, hashToGuess);
        kmrThread.start();
    }

    private void kmrInit(int port, int proof, String previousHash, String hashToGuess) {
        this.kmrPort = port;
        this.kmrProof = proof;
        this.kmrPreviousHash = previousHash;
        this.kmrHashToGuess = hashToGuess;
        this.kmrThread = kmrCreateThread();
    }

    private Thread kmrCreateThread() {
        return new Thread(() -> {
            this.kmrService = Service.ignite().port(kmrPort);
            Gson gson = new Gson();
            kmrService.get(KMR_PATHES.MINE, (req, res) -> {
                kmrOutput("Request to GET: " + req.url());
                KMR_Block lastBlock = kmrBlockchain.kmrLastBlock();
                int lastProof = lastBlock.getKmrProof();
                int proofOfWork = kmrBlockchain.kmrProofOfWork(lastProof);
                kmrOutput("Received nonce: " + proofOfWork);
                kmrOutput("Received hash: " + kmrBlockchain.kmrGetGuessHash(lastProof + "" + proofOfWork));
                kmrBlockchain.kmrNewTransaction("0", UUID.randomUUID().toString().replace("-", ""), 1);
                String lastHash = KMR_Blockchain.kmrHash(lastBlock);
                KMR_Block newBlock = kmrBlockchain.kmrNewBlock(proofOfWork, lastHash);
                String json = gson.toJson(newBlock);
                kmrOutput("200. Sent: " + json + "\n");
                res.status(200);
                return json;
            });

            kmrService.post(KMR_PATHES.NEW_TRANSACTION, (req, res) -> {
                String answer = "";
                kmrOutput("Request to POST: " + req.url());
                try {
                    kmrOutput("Received: " + req.body());
                    Map transaction = gson.fromJson(req.body(), Map.class);
                    int index = kmrBlockchain.kmrNewTransaction(
                            transaction.get("sender").toString(),
                            transaction.get("recipient").toString(),
                            ((Double) transaction.get("amount")).intValue());
                    answer = "201. Transaction will be added to Block " + index;
                    res.status(201);
                } catch (Exception e) {
                    answer = "400. Invalid JSON.";
                    res.status(400);
                } finally {
                    kmrOutput("Answer: " + answer + "\n");
                    return answer;

                }
            });

            kmrService.get(KMR_PATHES.CHAIN, (req, res) -> {
                kmrOutput("Request to GET: " + req.url());
                List<KMR_Block> chain = kmrBlockchain.getKmrChain();
                res.status(200);
                String json = gson.toJson(new KMR_ChainResponse(chain, chain.size()));
                kmrOutput("200. Sent: " + json + "\n");
                return json;
            });
        });
    }

    public void kmrStopBlockchain() {
        kmrService.stop();
        kmrThread.interrupt();
    }

    public boolean isStopped() {
        return kmrThread.isInterrupted();
    }

    public static void kmrOutput(Object s) {
        System.out.println("--> " + s);
    }

    @Override
    public Object clone() {
        return new KMR_Server(kmrPort, kmrProof, kmrPreviousHash, kmrHashToGuess, kmrBlockchain);
    }
}
