package aula.javafx.javafxmaven.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
A interface Serializable em Java é usada para indicar que objetos de uma determinada classe podem ser convertidos em uma sequência de bytes,
 ou seja, serializados. Isso é necessário, por exemplo, para:

Salvar objetos em arquivos (persistência em disco).
Enviar objetos pela rede (como em aplicações distribuídas).
Armazenar objetos em sessões (por exemplo, em aplicações web Java).
 */

public class Video implements Serializable {
    private String nome;
    private String link;
    private String dataDeAdicao;

    public Video(String nome, String link, String dataFormatada) {
        this.nome = nome;
        this.link = link;
        this.dataDeAdicao = dataFormatada;
    }

    public String getNome() {return nome;}
    public String getLink() {return link;}
    public String getDataDeAdicao() {return dataDeAdicao;}
    public void setNome(String nome) {this.nome = nome;}
    public void setLink(String link) {this.link = link;}
    public void setDataDeAdicao(String data) {this.dataDeAdicao = data;}


}