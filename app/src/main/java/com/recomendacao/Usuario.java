package com.recomendacao;

import java.util.List;

class Usuario {
    private String nome;
    private List<RecomendacaoSupermercado> recomendacaoSupermercadoList;
    private List<RecomendacaoProduto> recomendacaoProdutoList;

    public Usuario(String s) {
        nome = s;
    }
    public int hashCode() { return nome.hashCode();}
    public String toString() { return nome; }

    public List<RecomendacaoProduto> getRecomendacaoProdutoList() {
        return recomendacaoProdutoList;
    }
    public void setRecomendacaoProdutoList(List<RecomendacaoProduto> recomendacaoProdutoList) {
        this.recomendacaoProdutoList = recomendacaoProdutoList;
    }

    public List<RecomendacaoSupermercado> getRecomendacaoSupermercadoList() {
        return recomendacaoSupermercadoList;
    }
    public void setRecomendacaoSupermercadoList(List<RecomendacaoSupermercado> recomendacaoSupermercadoList) {
        this.recomendacaoSupermercadoList = recomendacaoSupermercadoList;
    }







}
