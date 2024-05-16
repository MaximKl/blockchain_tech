package com.klishov.pow;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class KMR_Blockchain {

    private final List<KMR_Block> kmrChain;
    private final List<KMR_Transaction> kmrCurrentTransactions;

    public KMR_Blockchain(int proof, String previousHash) {
        this.kmrCurrentTransactions = new ArrayList<>();
        this.kmrChain = new ArrayList<>();
        kmrNewBlock(proof, previousHash);
    }

    public KMR_Block kmrNewBlock(int proof, String previousHash) {
        KMR_Block newBlock = new KMR_Block(this.kmrChain.size(), new ArrayList<>(this.kmrCurrentTransactions), proof, previousHash);
        this.kmrCurrentTransactions.clear();
        this.kmrChain.add(newBlock);
        return newBlock;
    }

    public int kmrNewTransaction(String sender, String recipient, int amount) {
        this.kmrCurrentTransactions.add(new KMR_Transaction(sender, recipient, amount));
        return this.kmrChain.size();
    }

    public static String kmrHash(KMR_Block block) {
        String hashingInput = new StringBuilder()
                .append(block.getKmrIndex())
                .append(block.getKmrTimestamp())
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

    public int kmrProofOfWork(int lastProofOfWork, long hashToGuess) {
        int proof = 0;
        while (!kmrIsProofValid(lastProofOfWork, proof, Long.toString(hashToGuess))) {
            proof++;
        }
        return proof;
    }

    public boolean kmrIsProofValid(int lastProof, int proof, String hashToGuess) {
        String guessString = Integer.toString(lastProof) + Integer.toString(proof);
        String guessHash = Hashing.sha256().hashString(guessString, StandardCharsets.UTF_8).toString();
        return guessHash.endsWith(hashToGuess);
    }
}

