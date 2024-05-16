package com.klishov.pow;

import java.util.List;

public class KMR_Block {
    private final int kmrIndex;
    private final long kmrTimestamp;
    private final List<KMR_Transaction> kmrTransactions;
    private final int kmrProof;
    private final String kmrPreviousHash;

    public KMR_Block(int index, List<KMR_Transaction> transactions, int proof, String previousHash) {
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
