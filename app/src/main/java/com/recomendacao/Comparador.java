package com.recomendacao;

import java.util.Comparator;
import java.util.Map;

class Comparador implements Comparator<Object> {
    Map<Produto, Double> base;

    public Comparador(Map<Produto, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(Object a, Object b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}