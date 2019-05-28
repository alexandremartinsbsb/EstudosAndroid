package com.max.contatos;

public class Contato {

    private String contatoId;
    private String nome;
    private String numero;


    public String getContatoId() {
        return contatoId;
    }

    public void setContatoId(String contatoId) {
        this.contatoId = contatoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroCobrar() {
        String aux = this.numero.replaceAll("\\D+", "");

        if (aux.length() > 9) {
            return "90" + this.numero;
        } else {
            return "9090" + this.numero;
        }
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
