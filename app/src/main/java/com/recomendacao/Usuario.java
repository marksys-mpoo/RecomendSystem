package com.recomendacao;

class Usuario {
    private String nome;
    private RecomendacaoProduto recomendacaoProduto;
    private RecomendacaoSupermercado recomendacaoSupermercado;


    public Usuario(String s) {
        nome = s;
    }

    public int hashCode() { return nome.hashCode();}
    public String toString() { return nome; }

    public RecomendacaoProduto getRecomendacaoProduto() {
        return recomendacaoProduto;
    }
    public void setRecomendacaoProduto(RecomendacaoProduto recomendacaoProduto) {
        this.recomendacaoProduto = recomendacaoProduto;
    }

    public RecomendacaoSupermercado getRecomendacaoSupermercado() {
        return recomendacaoSupermercado;
    }
    public void setRecomendacaoSupermercado(RecomendacaoSupermercado recomendacaoSupermercado) {
        this.recomendacaoSupermercado = recomendacaoSupermercado;
    }
}
