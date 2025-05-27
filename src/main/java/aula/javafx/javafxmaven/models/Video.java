package aula.javafx.javafxmaven.models;

import java.io.Serializable;

public class Video implements Serializable {
    private String nome;
    private String link;

    public Video(String nome, String link) {
        this.nome = nome;
        this.link = link;
    }

    public String getNome() {
        return nome;
    }

    public String getLink() {
        return link;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLink(String link) {
        this.link = link;
    }
}