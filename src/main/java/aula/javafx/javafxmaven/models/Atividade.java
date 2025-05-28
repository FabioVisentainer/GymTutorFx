package aula.javafx.javafxmaven.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Atividade implements Serializable {
    private String nome;
    private String descricao;
    private String dataDeAdicao;

    public Atividade() {
    }

    public Atividade(String nome, String descricao, String dataDeAdicao) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataDeAdicao = dataDeAdicao;
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
    public String getDataDeAdicao() {
        return dataDeAdicao;
    }
    public void setDataDeAdicao(String dataDeAdicao) {
        this.dataDeAdicao = dataDeAdicao;
    }

    // Lista de videos pertencentes a atividade
    private List<Video> videos = new ArrayList<>();
    public List<Video> getVideos() {return videos;}
    public void setVideos(List<Video> videos) {this.videos = videos;}
    public void adicionarVideo(Video video) {this.videos.add(video);}
}
