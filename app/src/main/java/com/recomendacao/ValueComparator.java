package com.recomendacao;

import java.util.Comparator;
import java.util.Map;

class ValueComparator implements Comparator<Object> {
    Map<Produto, Float> base;

    public ValueComparator(Map<Produto, Float> base) {
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