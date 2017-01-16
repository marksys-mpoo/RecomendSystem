package com.recomendacao;

class Produto {

    private String nome;

    //private Recomendacao recomendacao;

    public Produto(String s) {
        nome = s;
    }

    public int hashCode() { return nome.hashCode();}
    public String toString() { return nome; }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    /*
    public Recomendacao getRecomendacao() {
        return recomendacao;
    }

    public void setRecomendacao(Recomendacao recomendacao) {
        this.recomendacao = recomendacao;
    } */
}