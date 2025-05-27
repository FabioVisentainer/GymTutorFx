package aula.javafx.javafxmaven.persistencia;

import java.io.Serializable;

public class Pessoa implements Serializable {
    // definido um seriaVersionUID
    private static final long serialVersionUID = 1L;

    private String nome;
    private String cpf;

    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    // Getters e Setters (Encapsulamento)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * Método toString para facilitar a visualização dos objetos
     * @return string contendo o nome e o cpf da pessoa
     */
    @Override
    public String toString() {
        return "Pessoa: " + nome + " - CPF: " + cpf;
    }
}
