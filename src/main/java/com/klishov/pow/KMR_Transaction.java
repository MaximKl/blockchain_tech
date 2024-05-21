package com.klishov.pow;

public class KMR_Transaction {

    private final int kmrAmount;
    private final String kmrRecipient;
    private final String kmrSender;

    public KMR_Transaction(int amount, String recipient, String sender) {
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
