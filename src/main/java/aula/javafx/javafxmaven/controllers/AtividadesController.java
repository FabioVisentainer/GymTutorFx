package aula.javafx.javafxmaven.controllers;

import aula.javafx.javafxmaven.models.Atividade;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AtividadesController {

    private final Stage stage;
    private final BorderPane rootLayout;
    private final File arquivo = new File("atividades.dat");
    private final ObservableList<Atividade> listaAtividades = FXCollections.observableArrayList();
    private TableView<Atividade> tabela;

    public AtividadesController(Stage stage, BorderPane rootLayout) {
        this.stage = stage;
        this.rootLayout = rootLayout;
    }

    /* ==============================
       Interface do Fragmento de Atividades
       ============================== */
    public void showAtividadesFragment() {
        VBox atividadesContent = criarUI();
        rootLayout.setCenter(atividadesContent);
    }

    private VBox criarUI() {
        VBox layout = new VBox(15); // Adjusted spacing for better layout
        layout.setStyle("-fx-padding: 20; -fx-alignment: top-center; -fx-background-color: #e6f7ff;"); // Align top-center

        /* ---------- Topo com Título e Descritivo ---------- */
        Label titulo = new Label("Cadastro de Atividades");
        titulo.setFont(new Font("Arial", 26));
        titulo.setStyle("-fx-text-fill: #0056b3; -fx-font-weight: bold;"); // Bold title

        Label descritivo = new Label("Cadastre aqui as atividades que o aluno poderá realizar durante os treinos. Essas atividades são essenciais para a criação das fichas de treino personalizadas.");
        descritivo.setFont(new Font("Arial", 12));
        descritivo.setStyle("-fx-text-fill: #333333;");
        descritivo.setWrapText(true); // Allow text to wrap
        descritivo.setMaxWidth(600); // Limit width for readability

        VBox headerArea = new VBox(5, titulo, descritivo); // Small spacing between title and descriptive text
        headerArea.setAlignment(Pos.TOP_CENTER); // Center the header elements

        /* ---------- Botão Adicionar ---------- */
        Button btnAdicionar = new Button("Adicionar atividades");
        btnAdicionar.setFont(new Font("Arial", 16)); // Slightly smaller font
        btnAdicionar.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 8 15;");
        btnAdicionar.setOnAction(e -> abrirFormulario());

        // HBox to position the button to the left
        HBox buttonContainer = new HBox(btnAdicionar);
        buttonContainer.setAlignment(Pos.CENTER_LEFT); // Align button to the left
        buttonContainer.setPadding(new javafx.geometry.Insets(10, 0, 10, 0)); // Padding around the button

        /* ---------- Tabela ---------- */
        tabela = new TableView<>();
        tabela.setPrefHeight(300); // Initial height
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setTableMenuButtonVisible(false);

        // Make the table grow vertically
        VBox.setVgrow(tabela, Priority.ALWAYS); // This makes the table take up available vertical space

        TableColumn<Atividade, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNome()));

        TableColumn<Atividade, String> colDesc = new TableColumn<>("Descrição");
        colDesc.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescricao()));

        TableColumn<Atividade, String> colData = new TableColumn<>("Data de adição");
        colData.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDataDeAdicao()));

        /* ---- Coluna Ações ---- */
        TableColumn<Atividade, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar   = new Button("Editar");
            private final Button btnDeletar  = new Button("Deletar");
            private final Button btnVideos   = new Button("Cadastrar Vídeo");
            private final HBox   hBox        = new HBox(5, btnEditar, btnDeletar, btnVideos); // Reduced spacing

            {
                btnEditar.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-font-size: 10px;");
                btnDeletar.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 10px;");
                btnVideos.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-size: 10px;");

                /* ======== Editar ======== */
                btnEditar.setOnAction(e -> {
                    Atividade a = getTableView().getItems().get(getIndex());
                    abrirFormularioEdicao(a);
                });

                /* ======= Deletar ======= */
                btnDeletar.setOnAction(e -> {
                    Atividade a = getTableView().getItems().get(getIndex());
                    Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                            "Deseja realmente deletar?", ButtonType.YES, ButtonType.NO);
                    c.showAndWait().ifPresent(r -> {
                        if (r == ButtonType.YES) {
                            listaAtividades.remove(a);
                            try {
                                salvar();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                mostrarErro("Erro ao salvar exclusão.", ex);
                            }
                        }
                    });
                });

                /* ======= Vídeos ======== */
                btnVideos.setOnAction(e -> {
                    Atividade a = getTableView().getItems().get(getIndex());
                    new VideoController(stage, rootLayout, a, AtividadesController.this).showVideosFragment();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });

        tabela.getColumns().addAll(colNome, colDesc, colData, colAcoes);
        tabela.setItems(listaAtividades);

        carregar();

        layout.getChildren().addAll(headerArea, buttonContainer, tabela);
        return layout;
    }

    /* ============ Formulário Novo ============ */
    private void abrirFormulario() {
        Stage modal = new Stage();
        modal.setTitle("Nova Atividade"); // Título mais amigável
        modal.initOwner(stage);
        modal.setResizable(false);

        VBox form = new VBox(20); // Aumenta o espaçamento entre os elementos do formulário
        form.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 10;"); // Fundo branco, borda suave, cantos arredondados

        Label titleLabel = new Label("Adicionar Nova Atividade");
        titleLabel.setFont(new Font("Arial", 22)); // Fonte maior para o título do modal
        titleLabel.setStyle("-fx-text-fill: #0056b3; -fx-font-weight: bold;");

        // Campos de texto com estilo aprimorado
        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome da Atividade (obrigatório)");
        campoNome.setMaxWidth(300);
        campoNome.setStyle("-fx-control-inner-background: #f0f0f0; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #a0a0a0; -fx-padding: 10;");

        TextField campoDesc = new TextField();
        campoDesc.setPromptText("Descrição da Atividade (opcional)"); // Descrição mais flexível
        campoDesc.setMaxWidth(300);
        campoDesc.setStyle("-fx-control-inner-background: #f0f0f0; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #a0a0a0; -fx-padding: 10;");

        // Botão Salvar com estilo consistente e foco
        Button btnSalvar = new Button("Salvar Atividade"); // Texto mais descritivo
        btnSalvar.setStyle(
                "-fx-background-color: #28a745; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " + // Padding aumentado
                        "-fx-background-radius: 5; " +
                        "-fx-border-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);" // Sombra suave
        );
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            String desc = campoDesc.getText().trim(); // A descrição não é mais obrigatória aqui

            if (!nome.isEmpty()) { // Apenas o nome é obrigatório agora
                String data = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                listaAtividades.add(new Atividade(nome, desc, data));

                try {
                    salvar();
                    modal.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarErro("Erro ao salvar atividade.", ex);
                }
            } else {
                mostrarAviso("O campo 'Nome da Atividade' é obrigatório!");
            }
        });

        // Botão Cancelar para fechar o modal
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle(
                "-fx-background-color: #dc3545; " + // Cor vermelha para cancelar
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );
        btnCancelar.setOnAction(e -> modal.close());

        HBox buttonBox = new HBox(15, btnSalvar, btnCancelar); // Espaçamento entre os botões
        buttonBox.setAlignment(Pos.CENTER);

        // Adiciona um espaçador para empurrar os botões para baixo e centralizar o conteúdo
        VBox contentBox = new VBox(10,
                new Label("Nome da Atividade:"), campoNome,
                new Label("Descrição:"), campoDesc
        );
        contentBox.setAlignment(Pos.CENTER_LEFT); // Alinha os labels e campos à esquerda

        form.getChildren().addAll(
                titleLabel,
                new Separator(), // Linha separadora para o título
                contentBox,
                new Region(), // Espaçador
                buttonBox
        );
        VBox.setVgrow(new Region(), Priority.ALWAYS); // Faz o espaçador crescer

        modal.setScene(new Scene(form, 480, 400)); // Aumenta o tamanho do modal para acomodar o novo layout
        modal.showAndWait();
    }

    /* ============ Formulário Edição ============ */
    private void abrirFormularioEdicao(Atividade a) {
        Stage modal = new Stage();
        modal.setTitle("Editar Atividade"); // Título mais amigável
        modal.initOwner(stage);
        modal.setResizable(false);

        VBox form = new VBox(20); // Aumenta o espaçamento
        form.setStyle("-fx-padding: 30; -fx-alignment: center; -fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 10;"); // Fundo branco, borda suave, cantos arredondados

        Label titleLabel = new Label("Editar Atividade Existente");
        titleLabel.setFont(new Font("Arial", 22));
        titleLabel.setStyle("-fx-text-fill: #0056b3; -fx-font-weight: bold;");

        TextField campoNome = new TextField(a.getNome());
        campoNome.setMaxWidth(300);
        campoNome.setStyle("-fx-control-inner-background: #f0f0f0; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #a0a0a0; -fx-padding: 10;");

        TextField campoDesc = new TextField(a.getDescricao());
        campoDesc.setMaxWidth(300);
        campoDesc.setStyle("-fx-control-inner-background: #f0f0f0; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #a0a0a0; -fx-padding: 10;");

        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.setStyle(
                "-fx-background-color: #ffc107; " + // Amarelo para edição
                        "-fx-text-fill: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 5; " +
                        "-fx-border-radius: 5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            String desc = campoDesc.getText().trim();

            if (!nome.isEmpty()) {
                a.setNome(nome);
                a.setDescricao(desc);
                tabela.refresh();

                try {
                    salvar();
                    modal.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarErro("Erro ao salvar alterações.", ex);
                }
            } else {
                mostrarAviso("O campo 'Nome da Atividade' é obrigatório!");
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
                new Label("Nome da Atividade:"), campoNome,
                new Label("Descrição:"), campoDesc
        );
        contentBox.setAlignment(Pos.CENTER_LEFT);

        form.getChildren().addAll(
                titleLabel,
                new Separator(),
                contentBox,
                new Region(),
                buttonBox
        );
        VBox.setVgrow(new Region(), Priority.ALWAYS);

        modal.setScene(new Scene(form, 480, 400));
        modal.showAndWait();
    }

    /* ============ Persistência ============ */
    public void salvar() {
        try {
            try (FileChannel fc = FileChannel.open(arquivo.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
                fc.truncate(0);
            }
            salvarDados();
        } catch (NoSuchFileException | FileNotFoundException e) {
            System.out.println("Arquivo não encontrado ou diretório inválido. Tentando criar arquivo...");
            try {
                salvarDados();
                System.out.println("Arquivo criado e dados salvos.");
            } catch (IOException ex) {
                mostrarErro("Erro ao salvar os dados após tentar criar o arquivo.", ex);
            }
        } catch (IOException e) {
            mostrarErro("Erro ao acessar o arquivo.", e);
        }
    }

    private void salvarDados() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(listaAtividades.stream().toList());
        }
    }

    public void carregar() {
        if (!arquivo.exists() || arquivo.length() == 0) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            List<Atividade> atividades = (List<Atividade>) ois.readObject();
            listaAtividades.setAll(atividades);
        } catch (IOException | ClassNotFoundException ex) {
            mostrarErro("Erro ao carregar atividades.", ex);
            listaAtividades.clear();
        }
    }

    /* ============ Utilitários ============ */
    private void mostrarAviso(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }

    private void mostrarErro(String titulo, Exception ex) {
        Alert a = new Alert(Alert.AlertType.ERROR, titulo + "\n" + ex.getMessage());
        a.setHeaderText("Erro");
        a.showAndWait();
    }
}