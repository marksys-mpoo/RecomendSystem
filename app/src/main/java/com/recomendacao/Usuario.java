package com.recomendacao;

class Usuario {
    private String nome;
    public Usuario(String s) {
        nome = s;
    }

    public int hashCode() { return nome.hashCode();}
    public String toString() { return nome; }
}
