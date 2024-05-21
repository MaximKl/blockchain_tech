package com.klishov.pow;

import java.util.List;

public class KMR_Block {
    private final int kmrIndex;
    private final String kmrPreviousHash;
    private final int kmrProof;
    private final long kmrTimestamp;
    private final List<KMR_Transaction> kmrTransactions;

    public KMR_Block(int index, String previousHash, int proof, List<KMR_Transaction> transactions) {
        this.kmrIndex = index;
        this.kmrTransactions = transactions;
        this.kmrProof = proof;
        this.kmrPreviousHash = previousHash;
        this.kmrTimestamp = System.currentTimeMillis();
    }

    public int getKmrIndex() {
        return kmrIndex;
    }

    public long getKmrTimestamp() {
        return kmrTimestamp;
    }

    public List<KMR_Transaction> getKmrTransactions() {
        return kmrTransactions;
    }

    public int getKmrProof() {
        return kmrProof;
    }

    public String getKmrPreviousHash() {
        return kmrPreviousHash;
    }
}