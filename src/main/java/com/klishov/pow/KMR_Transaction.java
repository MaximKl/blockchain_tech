package com.klishov.pow;

public class KMR_Transaction {

    private final String kmrSender;
    private final String kmrRecipient;
    private final int kmrAmount;

    public KMR_Transaction(String sender, String recipient, int amount) {
        this.kmrSender = sender;
        this.kmrRecipient = recipient;
        this.kmrAmount = amount;
    }

    public String getKmrSender() {
        return kmrSender;
    }

    public String getKmrRecipient() {
        return kmrRecipient;
    }

    public int getKmrAmount() {
        return kmrAmount;
    }
}
