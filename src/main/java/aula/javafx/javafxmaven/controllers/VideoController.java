package aula.javafx.javafxmaven.controllers;

import aula.javafx.javafxmaven.models.Video;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class VideoController {

    private final Stage stage;
    private final File arquivo = new File("videos.dat");
    private final ObservableList<Video> listaVideos = FXCollections.observableArrayList();
    private TableView<Video> tabela;

    public VideoController(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        criarUI();
        this.stage.show();
    }

    private void criarUI() {
        stage.setTitle("Cadastro de Vídeos");

        VBox layout = new VBox();
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.setSpacing(20);

        Label titulo = new Label("Cadastro de Vídeos");
        titulo.setFont(new Font("Arial", 26));

        // Botão para adicionar novo vídeo
        Button btnAdicionar = new Button("Adicionar Vídeo");
        btnAdicionar.setFont(new Font("Arial", 18));
        btnAdicionar.setOnAction(e -> abrirFormulario());

        // Tabela para listar os vídeos
        tabela = new TableView<>();
        // Limpa colunas para evitar duplicação ou colunas extras
        tabela.getColumns().clear();
        TableColumn<Video, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));

        TableColumn<Video, String> colLink = new TableColumn<>("Link");
        colLink.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLink()));

        tabela.getColumns().addAll(colNome, colLink);
        tabela.setItems(listaVideos); // Itens da tabela
        tabela.setPrefHeight(300); // Altura da tabela

        carregarVideos(); // Carrega os vídeos do arquivo

        layout.getChildren().addAll(titulo, btnAdicionar, tabela);

        Scene cena = new Scene(layout, 800, 500);
        this.stage.setScene(cena);
    }

    // Abre o Modal com o formulário para adicionar vídeo
    private void abrirFormulario() {
        Stage modal = new Stage();
        modal.setTitle("Novo Vídeo");

        VBox formLayout = new VBox();f cf
        formLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        formLayout.setSpacing(15);

        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome do vídeo");

        TextField campoLink = new TextField();
        campoLink.setPromptText("Link do vídeo");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            String link = campoLink.getText().trim();
            if (!nome.isEmpty() && !link.isEmpty()) {
                Video video = new Video(nome, link);
                listaVideos.add(video);
                salvarVideos(); // Salva no arquivo
                modal.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!");
                alert.showAndWait();
            }
        });

        formLayout.getChildren().addAll(new Label("Nome:"), campoNome, new Label("Link:"), campoLink, btnSalvar);

        Scene formScene = new Scene(formLayout, 300, 250);
        modal.setScene(formScene);
        modal.initOwner(this.stage); // Janela pai
        modal.showAndWait();
    }

    // Serializa e salva os vídeos no arquivo
    private void salvarVideos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(listaVideos.stream().toList());
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao salvar vídeo, arquivo não encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro desconhecido ao salvar vídeo: " + e.getMessage());
        }
    }

    // Lê os vídeos salvos do arquivo
    private void carregarVideos() {
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                List<Video> videos = (List<Video>) ois.readObject();
                listaVideos.setAll(videos);
            } catch (FileNotFoundException e) {
                System.err.println("Erro ao carregar vídeo, arquivo não encontrado: " + e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro desconhecido ao carregar vídeo: " + e.getMessage());
            }
        }
    }
}