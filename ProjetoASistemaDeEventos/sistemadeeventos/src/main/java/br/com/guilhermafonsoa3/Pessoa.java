package br.com.guilhermafonsoa3;

import java.io.Serializable;

public abstract class Pessoa implements Serializable {
    protected Long id;
    protected String nome;

    public Pessoa() {
        // construtor vazio necessário para deserialização
    }

    public Pessoa(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public abstract String getDescricao();
    public abstract String getContato();

    // Getters e Setters (se necessário)
}
