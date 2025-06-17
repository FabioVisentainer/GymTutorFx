package aula.javafx.javafxmaven.controllers;

import aula.javafx.javafxmaven.models.Atividade;
import aula.javafx.javafxmaven.models.Video;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List; // Import List if not already

public class VideoController {

    private final Stage stage;
    private final BorderPane rootLayout;
    private final Atividade atividade;
    private ObservableList<Video> listaVideos;
    private TableView<Video> tabela;
    private final AtividadesController atividadesController;

    public VideoController(Stage stage,
                           BorderPane rootLayout,
                           Atividade atividade,
                           AtividadesController atividadesController) {
        this.stage = stage;
        this.rootLayout = rootLayout;
        this.atividade = atividade;
        this.atividadesController = atividadesController;
    }

    /* ==============================
       Interface do Fragmento de Vídeos
       ============================== */
    public void showVideosFragment() {
        VBox videoContent = criarUI();
        if (atividade != null) {
            listaVideos = FXCollections.observableArrayList(atividade.getVideos());
            tabela.setItems(listaVideos);
        }
        rootLayout.setCenter(videoContent);
    }

    private VBox criarUI() {
        VBox layout = new VBox(15); // Espaçamento ajustado para melhor layout
        layout.setStyle("-fx-padding: 20; -fx-alignment: top-center; -fx-background-color: #f0f8ff;");

        /* ---------- Topo com Título e Descritivo ---------- */
        // Ajuste no título: "Cadastro de Vídeos - (Nome da atividade)"
        Label titulo = new Label("Cadastro de Vídeos - Atividade: " + (atividade != null ? atividade.getNome() : ""));
        titulo.setFont(new Font("Arial", 26));
        titulo.setStyle("-fx-text-fill: #0056b3; -fx-font-weight: bold;"); // Mantém negrito e cor

        // Nova descrição para o fragmento de vídeos
        Label descritivo = new Label("Nesta seção, você pode adicionar, editar e remover vídeos para a atividade \"" + (atividade != null ? atividade.getNome() : "") + "\". Os vídeos ajudam a demonstrar a execução correta da atividade.");
        descritivo.setFont(new Font("Arial", 12));
        descritivo.setStyle("-fx-text-fill: #333333;");
        descritivo.setWrapText(true);
        descritivo.setMaxWidth(600);

        // HBox para o botão Voltar (agora no topo esquerdo)
        Button btnVoltar = new Button("Voltar para Atividades");
        btnVoltar.setFont(new Font("Arial", 12));
        btnVoltar.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-insets: 0; -fx-background-radius: 5; -fx-border-width: 0; -fx-border-color: transparent; -fx-border-radius: 5;");
        btnVoltar.setOnAction(e -> atividadesController.showAtividadesFragment());

        HBox topoBotaoVoltar = new HBox(btnVoltar);
        topoBotaoVoltar.setAlignment(Pos.CENTER_LEFT);
        topoBotaoVoltar.setPadding(new javafx.geometry.Insets(0, 0, 10, 0)); // Padding abaixo do botão

        VBox headerArea = new VBox(5, titulo, descritivo); // Espaçamento entre título e descritivo
        headerArea.setAlignment(Pos.TOP_CENTER); // Centraliza os elementos do cabeçalho

        /* ---------- Botão Novo ---------- */
        Button btnAdicionar = new Button("Adicionar Vídeo");
        btnAdicionar.setFont(new Font("Arial", 18));
        btnAdicionar.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 8 15;");
        btnAdicionar.setOnAction(e -> abrirFormulario());

        HBox buttonContainer = new HBox(btnAdicionar);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        buttonContainer.setPadding(new javafx.geometry.Insets(10, 0, 10, 0)); // Padding ao redor do botão
        HBox.setHgrow(buttonContainer, Priority.ALWAYS); // Garante que o HBox ocupe toda a largura disponível

        /* ---------- Tabela ---------- */
        tabela = new TableView<>();
        tabela.setPrefHeight(300);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setTableMenuButtonVisible(false);
        VBox.setVgrow(tabela, Priority.ALWAYS); // A tabela ocupa o espaço vertical disponível

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
            private final HBox hBox = new HBox(5, btnEditar, btnDeletar); // Reduzido o espaçamento entre botões

            {
                // Estilização para botões de ação (adicionado insets/border para consistência)
                String actionBtnStyle = "-fx-font-size: 10px; -fx-padding: 5 10; -fx-background-insets: 0; -fx-background-radius: 3; -fx-border-width: 0; -fx-border-color: transparent; -fx-border-radius: 3;";

                btnEditar.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;" + actionBtnStyle);
                btnDeletar.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;" + actionBtnStyle);

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
        layout.getChildren().addAll(topoBotaoVoltar, headerArea, buttonContainer, tabela);

        return layout;
    }

    /* ============ Formulário Novo ============ */
    private void abrirFormulario() {
        Stage modal = new Stage();
        modal.setTitle("Novo Vídeo para " + (atividade != null ? atividade.getNome() : ""));
        modal.initOwner(stage);
        modal.setResizable(false);

        VBox formLayout = new VBox(20); // Aumenta o espaçamento
        formLayout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 10;");

        Label titleLabel = new Label("Adicionar Novo Vídeo");
        titleLabel.setFont(new Font("Arial", 22));
        titleLabel.setStyle("-fx-text-fill: #0056b3; -fx-font-weight: bold;");

        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome do vídeo (obrigatório)"); // Prompt mais claro
        campoNome.setMaxWidth(300);
        campoNome.setStyle("-fx-control-inner-background: #f0f0f0; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #a0a0a0; -fx-padding: 10;");

        TextField campoLink = new TextField();
        campoLink.setPromptText("Link do vídeo (URL completa e válida)"); // Prompt mais claro
        campoLink.setMaxWidth(300);
        campoLink.setStyle("-fx-control-inner-background: #f0f0f0; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #a0a0a0; -fx-padding: 10;");

        Button btnSalvar = new Button("Salvar Vídeo"); // Texto mais descritivo
        btnSalvar.setStyle(
                "-fx-background-color: #28a745; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );
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
                mostrarAviso("Por favor, preencha o nome do vídeo e insira uma URL válida para o link.");
            }
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle(
                "-fx-background-color: #dc3545; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );
        btnCancelar.setOnAction(e -> modal.close());

        HBox buttonBox = new HBox(15, btnSalvar, btnCancelar);
        buttonBox.setAlignment(Pos.CENTER);

        VBox contentBox = new VBox(10,
                new Label("Nome do Vídeo:"), campoNome,
                new Label("Link do Vídeo:"), campoLink
        );
        contentBox.setAlignment(Pos.CENTER_LEFT);

        formLayout.getChildren().addAll(
                titleLabel,
                new Separator(),
                contentBox,
                new Region(),
                buttonBox
        );
        VBox.setVgrow(new Region(), Priority.ALWAYS);

        modal.setScene(new Scene(formLayout, 480, 400));
        modal.showAndWait();
    }

    /* ============ Formulário Edição ============ */
    private void abrirFormularioEdicao(Video video) {
        Stage modal = new Stage();
        modal.setTitle("Editar Vídeo de " + (atividade != null ? atividade.getNome() : ""));
        modal.initOwner(stage);
        modal.setResizable(false);

        VBox formLayout = new VBox(20); // Aumenta o espaçamento
        formLayout.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 10;");

        Label titleLabel = new Label("Editar Vídeo Existente");
        titleLabel.setFont(new Font("Arial", 22));
        titleLabel.setStyle("-fx-text-fill: #0056b3; -fx-font-weight: bold;");

        TextField campoNome = new TextField(video.getNome());
        campoNome.setMaxWidth(300);
        campoNome.setStyle("-fx-control-inner-background: #f0f0f0; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #a0a0a0; -fx-padding: 10;");

        TextField campoLink = new TextField(video.getLink());
        campoLink.setMaxWidth(300);
        campoLink.setStyle("-fx-control-inner-background: #f0f0f0; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #a0a0a0; -fx-padding: 10;");

        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.setStyle(
                "-fx-background-color: #ffc107; " +
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );
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
                mostrarAviso("Por favor, preencha o nome do vídeo e insira uma URL válida para o link.");
            }
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle(
                "-fx-background-color: #dc3545; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );
        btnCancelar.setOnAction(e -> modal.close());

        HBox buttonBox = new HBox(15, btnSalvar, btnCancelar);
        buttonBox.setAlignment(Pos.CENTER);

        VBox contentBox = new VBox(10,
                new Label("Nome do Vídeo:"), campoNome,
                new Label("Link do Vídeo:"), campoLink
        );
        contentBox.setAlignment(Pos.CENTER_LEFT);

        formLayout.getChildren().addAll(
                titleLabel,
                new Separator(),
                contentBox,
                new Region(),
                buttonBox
        );
        VBox.setVgrow(new Region(), Priority.ALWAYS);

        modal.setScene(new Scene(formLayout, 480, 400));
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