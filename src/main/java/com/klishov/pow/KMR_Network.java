package com.klishov.pow;

public record KMR_Network(int id, String name) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KMR_Network) {
            return this.id == ((KMR_Network) obj).id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
