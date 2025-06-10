package aula.javafx.javafxmaven.controllers;

import aula.javafx.javafxmaven.models.Atividade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private final File arquivo = new File("atividades.dat");
    private final ObservableList<Atividade> listaAtividades = FXCollections.observableArrayList();
    private TableView<Atividade> tabela;

    public AtividadesController(Stage stage) {
        this.stage = stage;
    }

    /* ==============================
       Interface da Janela Principal
       ============================== */
    public void mostrar() {
        criarUI();
        stage.show();
    }

    private void criarUI() {
        stage.setTitle("Cadastro de Atividades");

        VBox layout = new VBox(20);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        /* ---------- Topo ---------- */
        Label titulo = new Label("Cadastro de Atividades");
        titulo.setFont(new Font("Arial", 26));

        Button btnVoltar = new Button("Voltar a Home");
        btnVoltar.setFont(new Font("Arial", 9));
        btnVoltar.setOnAction(e -> new HomeController(stage).mostrar());

        HBox topo = new HBox(10, btnVoltar);
        topo.setStyle("-fx-alignment: top-left;");

        /* ---------- Botão Novo ---------- */
        Button btnAdicionar = new Button("Adicionar atividades");
        btnAdicionar.setFont(new Font("Arial", 18));
        btnAdicionar.setOnAction(e -> abrirFormulario());

        /* ---------- Tabela ---------- */
        tabela = new TableView<>();
        tabela.setPrefHeight(300);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setTableMenuButtonVisible(false);

        TableColumn<Atividade, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNome()));

        TableColumn<Atividade, String> colDesc = new TableColumn<>("Descrição");
        colDesc.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDescricao()));

        TableColumn<Atividade, String> colData = new TableColumn<>("Data de adição");
        colData.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDataDeAdicao()));

        /* ---- Coluna Ações ---- */
        TableColumn<Atividade, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar   = new Button("Editar");
            private final Button btnDeletar  = new Button("Deletar");
            private final Button btnVideos   = new Button("Cadastrar Vídeo");
            private final HBox   hBox        = new HBox(10, btnEditar, btnDeletar, btnVideos);

            {
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
                    new VideoController(stage, a, AtividadesController.this).mostrar();
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

        layout.getChildren().addAll(topo, titulo, btnAdicionar, tabela);
        stage.setScene(new Scene(layout, 800, 500));
    }

    /* ============ Formulário Novo ============ */
    private void abrirFormulario() {
        Stage modal = new Stage();
        modal.setTitle("Nova atividade");

        VBox form = new VBox(25);
        form.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome da atividade");
        campoNome.setMaxWidth(300);

        TextField campoDesc = new TextField();
        campoDesc.setPromptText("Descrição da atividade");
        campoDesc.setMaxWidth(300);

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            String desc = campoDesc.getText().trim();

            if (!nome.isEmpty() && !desc.isEmpty()) {
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
                mostrarAviso("Preencha todos os campos!");
            }
        });

        form.getChildren().addAll(new Label("Nome:"), campoNome,
                new Label("Descrição:"), campoDesc,
                btnSalvar);

        modal.setScene(new Scene(form, 450, 300));
        modal.initOwner(stage);
        modal.showAndWait();
    }

    /* ============ Formulário Edição ============ */
    private void abrirFormularioEdicao(Atividade a) {
        Stage modal = new Stage();
        modal.setTitle("Editar atividade");

        VBox form = new VBox(25);
        form.setStyle("-fx-padding: 20; -fx-alignment: center;");

        TextField campoNome = new TextField(a.getNome());
        campoNome.setMaxWidth(300);

        TextField campoDesc = new TextField(a.getDescricao());
        campoDesc.setMaxWidth(300);

        Button btnSalvar = new Button("Salvar alterações");
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            String desc = campoDesc.getText().trim();

            if (!nome.isEmpty() && !desc.isEmpty()) {
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
                mostrarAviso("Preencha todos os campos!");
            }
        });

        form.getChildren().addAll(new Label("Nome:"), campoNome,
                new Label("Descrição:"), campoDesc,
                btnSalvar);

        modal.setScene(new Scene(form, 450, 300));
        modal.initOwner(stage);
        modal.showAndWait();
    }

    /* ============ Persistência ============ */
    public void salvar() {
        try {
            FileChannel.open(arquivo.toPath(), StandardOpenOption.WRITE).close();
            salvarDados();
        } catch (NoSuchFileException | FileNotFoundException e) {
            System.out.println("Arquivo não encontrado. Criando novo arquivo...");
            try {
                salvarDados();
                System.out.println("Arquivo criado com sucesso.");
            } catch (IOException ex) {
                mostrarErro("Erro ao salvar os dados após criar o arquivo.", ex);
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
        if (!arquivo.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            List<Atividade> atividades = (List<Atividade>) ois.readObject();
            listaAtividades.setAll(atividades);
        } catch (IOException | ClassNotFoundException ex) {
            mostrarErro("Erro ao carregar atividades.", ex);
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

