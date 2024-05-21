package com.klishov.pow;

import java.util.List;

public class KMR_ChainResponse{
    private final List<KMR_Block> kmrChain;
    private final int kmrLength;

    public KMR_ChainResponse(List<KMR_Block> chain, int length) {
        this.kmrChain = chain;
        this.kmrLength = length;
    }

    public List<KMR_Block> getKmrChain() {
        return kmrChain;
    }

    public int getKmrLength() {
        return kmrLength;
    }
}
