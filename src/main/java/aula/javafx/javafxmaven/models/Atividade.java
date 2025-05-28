package aula.javafx.javafxmaven.models;

import java.io.Serializable;

public class Atividade implements Serializable {
    private String nome;
    private String descricao;
    private String dataDeAdição;

    public Atividade() {
    }

    public Atividade(String nome, String descricao, String dataDeAdição) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataDeAdição = dataDeAdição;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataDeAdição() {
        return dataDeAdição;
    }

    public void setDataDeAdição(String dataDeAdição) {
        this.dataDeAdição = dataDeAdição;
    }
}
