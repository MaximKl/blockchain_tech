package com.klishov.pow;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class KMR_Blockchain {

    private final String kmrHashToGuess;
    private final List<KMR_Block> kmrChain;
    private final List<KMR_Transaction> kmrCurrentTransactions;

    public KMR_Blockchain(int proof, String previousHash, String hashToGuess) {
        this.kmrCurrentTransactions = new ArrayList<>();
        this.kmrChain = new ArrayList<>();
        this.kmrHashToGuess = hashToGuess;
        kmrNewBlock(proof, previousHash);
    }

    public KMR_Block kmrNewBlock(int proof, String previousHash) {
        KMR_Block newBlock = new KMR_Block(this.kmrChain.size()+1, previousHash,proof,
                new ArrayList<>(this.kmrCurrentTransactions));
        this.kmrCurrentTransactions.clear();
        this.kmrChain.add(newBlock);
        return newBlock;
    }

    public int kmrNewTransaction(String sender, String recipient, int amount) {
        this.kmrCurrentTransactions.add(new KMR_Transaction(amount, recipient, sender));
        return this.kmrChain.size();
    }

    public static String kmrHash(KMR_Block block) {
        String hashingInput = new StringBuilder()
                .append(block.getKmrIndex())
                .append(block.getKmrTimestamp())
                .append(block.getKmrProof())
                .append(block.getKmrPreviousHash())
                .toString();
        return Hashing.sha256().hashString(hashingInput, StandardCharsets.UTF_8).toString();
    }

    public KMR_Block kmrLastBlock() {
        if (!this.kmrChain.isEmpty()) {
            return this.kmrChain.get(this.kmrChain.size() - 1);
        }
        return null;
    }

    public int kmrProofOfWork(int lastProofOfWork) {
        int proof = 0;
        while (!kmrIsProofValid(lastProofOfWork, proof)) {
            proof++;
        }
        return proof;
    }

    public boolean kmrIsProofValid(int lastProof, int proof) {
        String guessString = Integer.toString(lastProof) + Integer.toString(proof);
        return kmrGetGuessHash(guessString).endsWith(kmrHashToGuess);
    }

    public String kmrGetGuessHash(String guessString){
        return Hashing.sha256().hashString(guessString, StandardCharsets.UTF_8).toString();
    }
}
