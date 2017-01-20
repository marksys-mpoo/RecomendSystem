package com.recomendacao;

class Produto {

    private String nome;
    private Supermercado supermercado;

    public Produto(String s) {
        nome = s;
    }

    public String toString() { return nome; }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Supermercado getSupermercado() {
        return supermercado;
    }
    public void setSupermercado(Supermercado supermercado) {
        this.supermercado = supermercado;
    }

}