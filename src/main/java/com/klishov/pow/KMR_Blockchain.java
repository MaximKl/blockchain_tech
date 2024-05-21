package com.klishov.pow;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KMR_Blockchain {

    private final String kmrHashToGuess;
    private List<KMR_Block> kmrChain;
    private final List<KMR_Transaction> kmrCurrentTransactions;
    private final Set<String> kmrNodes;

    public KMR_Blockchain(int proof, String previousHash, String hashToGuess) {
        this.kmrCurrentTransactions = new ArrayList<>();
        this.kmrChain = new ArrayList<>();
        this.kmrHashToGuess = hashToGuess;
        this.kmrNodes = new HashSet<>();
        kmrNewBlock(proof, previousHash);
    }

    public List<KMR_Block> getKmrChain() {
        return kmrChain;
    }

    public Set<String> getKmrNodes() {
        return kmrNodes;
    }

    public KMR_Block kmrNewBlock(int proof, String previousHash) {
        KMR_Block newBlock = new KMR_Block(this.kmrChain.size() + 1, previousHash, proof,
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

    public String kmrGetGuessHash(String guessString) {
        return Hashing.sha256().hashString(guessString, StandardCharsets.UTF_8).toString();
    }

    public void kmrRegisterNode(String netloc) {
        kmrNodes.add(netloc);
    }

    public boolean kmrValidChain(List<KMR_Block> chain) throws AccessException {
        for (int i = 1; i < chain.size(); i++) {
            KMR_Block lastBlock = chain.get(i - 1);
            KMR_Block currentBlock = chain.get(i);
            if (!currentBlock.getKmrPreviousHash().equals(kmrHash(lastBlock)) ||
                    !kmrIsProofValid(lastBlock.getKmrProof(), currentBlock.getKmrProof()))
                return false;
        }
        return true;
    }

    public boolean resolveConflicts() throws Exception {
        Gson gson = new Gson();
        int maxLen = this.kmrChain.size();
        List<KMR_Block> newChain = this.kmrChain;
        for (String host : this.kmrNodes) {
            URL url;
            url = new URL(host + "/chain");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int status = con.getResponseCode();
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                KMR_ChainResponse response = gson.fromJson(content.toString(), KMR_ChainResponse.class);
                if (response.getKmrLength() > maxLen && kmrValidChain(response.getKmrChain())) {
                    maxLen = response.getKmrLength();
                    newChain = response.getKmrChain();
                }
            }
        }
        if (newChain != this.kmrChain) {
            this.kmrChain = newChain;
            return true;
        }
        return false;
    }

}

