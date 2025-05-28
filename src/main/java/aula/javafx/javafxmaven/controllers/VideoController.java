package aula.javafx.javafxmaven.controllers;

import aula.javafx.javafxmaven.models.Atividade;
import aula.javafx.javafxmaven.models.Video;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VideoController {

    private final Stage stage;
    private final Atividade atividade;
    private ObservableList<Video> listaVideos;
    private TableView<Video> tabela;
    private final AtividadesController atividadesController;

    public VideoController(Stage stage,
                           Atividade atividade,
                           AtividadesController atividadesController) {
        this.stage = stage;
        this.atividade = atividade;
        this.atividadesController = atividadesController;
    }

    /* ==============================
       Interface da Janela Principal
       ============================== */
    public void mostrar() {
        criarUI();
        if (atividade != null) {
            listaVideos = FXCollections.observableArrayList(atividade.getVideos());
            tabela.setItems(listaVideos);
        }
        stage.show();
    }

    private void criarUI() {
        stage.setTitle("Cadastro de Vídeos");

        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        /* ---------- Topo ---------- */
        Label titulo = new Label("Cadastro de Vídeos");
        titulo.setFont(new Font("Arial", 26));

        Button btnVoltar = new Button("Voltar para Atividades");
        btnVoltar.setFont(new Font("Arial", 9));
        btnVoltar.setOnAction(e -> atividadesController.mostrar());

        HBox topo = new HBox(btnVoltar);
        topo.setStyle("-fx-alignment: top-left;");
        topo.setSpacing(10);
        layout.getChildren().add(topo);

        /* ---------- Botão Novo ---------- */
        Button btnAdicionar = new Button("Adicionar Vídeo");
        btnAdicionar.setFont(new Font("Arial", 18));
        btnAdicionar.setOnAction(e -> abrirFormulario());
        layout.getChildren().addAll(titulo, btnAdicionar);

        /* ---------- Tabela ---------- */
        tabela = new TableView<>();
        tabela.setPrefHeight(300);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setTableMenuButtonVisible(false);

        TableColumn<Video, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));

        TableColumn<Video, String> colLink = new TableColumn<>("Link");
        colLink.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getLink()));

        TableColumn<Video, String> colData = new TableColumn<>("Data de adição");
        colData.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDataDeAdicao()));

        /* ---- Coluna Ações ---- */
        TableColumn<Video, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnDeletar = new Button("Deletar");
            private final HBox hBox = new HBox(10, btnEditar, btnDeletar);

            {
                /* ======== Editar ======== */
                btnEditar.setOnAction(e -> {
                    Video video = getTableView().getItems().get(getIndex());
                    abrirFormularioEdicao(video);
                });

                /* ======= Deletar ======== */
                btnDeletar.setOnAction(e -> {
                    Video video = getTableView().getItems().get(getIndex());
                    Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                            "Deseja realmente deletar?", ButtonType.YES, ButtonType.NO);
                    confirmacao.showAndWait().ifPresent(resposta -> {
                        if (resposta == ButtonType.YES) {
                            atividade.getVideos().remove(video);
                            listaVideos.remove(video);

                            try {
                                atividadesController.salvar();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                mostrarErro("Erro ao salvar exclusão de vídeo.", ex);
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });

        tabela.getColumns().addAll(colNome, colLink, colData, colAcoes);
        layout.getChildren().add(tabela);

        stage.setScene(new Scene(layout, 800, 500));
    }

    /* ============ Formulário Novo ============ */
    private void abrirFormulario() {
        Stage modal = new Stage();
        modal.setTitle("Novo Vídeo");

        VBox formLayout = new VBox(25);
        formLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome do vídeo");
        campoNome.setMaxWidth(300);

        TextField campoLink = new TextField();
        campoLink.setPromptText("Link do vídeo");
        campoLink.setMaxWidth(300);

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            String link = campoLink.getText().trim();

            if (!nome.isEmpty() && !link.isEmpty() && linkValido(link)) {
                String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                Video video = new Video(nome, link, data);

                listaVideos.add(video);
                atividade.adicionarVideo(video);

                try {
                    atividadesController.salvar();
                    modal.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarErro("Erro ao salvar vídeo.", ex);
                }
            } else {
                mostrarAviso("Preencha todos os campos corretamente!\nO link deve ser uma URL válida.");
            }
        });

        formLayout.getChildren().addAll(
                new Label("Nome:"), campoNome,
                new Label("Link:"), campoLink,
                btnSalvar
        );

        modal.setScene(new Scene(formLayout, 300, 250));
        modal.initOwner(stage);
        modal.showAndWait();
    }

    /* ============ Formulário Edição ============ */
    private void abrirFormularioEdicao(Video video) {
        Stage modal = new Stage();
        modal.setTitle("Editar Vídeo");

        VBox formLayout = new VBox(25);
        formLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField campoNome = new TextField(video.getNome());
        campoNome.setMaxWidth(300);

        TextField campoLink = new TextField(video.getLink());
        campoLink.setMaxWidth(300);

        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.setOnAction(e -> {
            String novoNome = campoNome.getText().trim();
            String novoLink = campoLink.getText().trim();

            if (!novoNome.isEmpty() && !novoLink.isEmpty() && linkValido(novoLink)) {
                video.setNome(novoNome);
                video.setLink(novoLink);
                tabela.refresh();

                try {
                    atividadesController.salvar();
                    modal.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarErro("Erro ao salvar alterações.", ex);
                }
            } else {
                mostrarAviso("Preencha todos os campos corretamente!\nO link deve ser uma URL válida.");
            }
        });

        formLayout.getChildren().addAll(
                new Label("Nome:"), campoNome,
                new Label("Link:"), campoLink,
                btnSalvar
        );

        modal.setScene(new Scene(formLayout, 450, 300));
        modal.initOwner(stage);
        modal.showAndWait();
    }

    /* ============ Utilitários ============ */
    private boolean linkValido(String link) {
        try {
            new java.net.URL(link);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void mostrarAviso(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }

    private void mostrarErro(String titulo, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR,
                titulo + "\n" + ex.getMessage());
        alert.setHeaderText("Erro");
        alert.showAndWait();
    }
}