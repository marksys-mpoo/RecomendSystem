package com.recomendacao;

public class Combine implements Comparable<Combine> {

    public Produto value;
    public Float key;

    public Combine(Produto value, Float key) {
        this.value = value;
        this.key = key;
    }

    @Override
    public int compareTo(Combine arg0) {
        // TODO Auto-generated method stub
        return this.key > arg0.key ? 1 : this.key < arg0.key ? -1
                : 0;
    }

    public String toString() {
        return this.value + " " + this.key;
    }
}