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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AtividadesController {

    private Stage stage;
    private final File arquivo = new File("atividades.dat");
    private final ObservableList<Atividade> listaAtividades = FXCollections.observableArrayList();
    private TableView<Atividade> tabela;

    public AtividadesController(Stage stage) {
        this.stage = stage;
    }

    // Metodo que será chamado para exibir a tela
    public void mostrar() {
        criarUI();        // Cria a interface gráfica
        this.stage.show(); // Exibe a janela
    }

    private void criarUI() {
        stage.setTitle("Cadastro de Atividades");

        VBox layout = new VBox();
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.setSpacing(20);

        Label titulo = new Label("Cadastro de Imagens");
        titulo.setFont(new Font("Arial", 26));

        // Botão para adicionar nova atividade
        Button btnAdicionar = new Button("Adicionar atividades");
        btnAdicionar.setFont(new Font("Arial", 18));
        btnAdicionar.setOnAction(e -> abrirFormulario());

        // Tabela para listar as atividades
        tabela = new TableView<>();
        // Limpa colunas para evitar duplicação ou colunas extras
        tabela.getColumns().clear();
        TableColumn<Atividade, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));

        TableColumn<Atividade, String> colDesc = new TableColumn<>("Descrição");
        colDesc.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescricao()));

        TableColumn<Atividade, String> colData = new TableColumn<>("Data de adição");
        colData.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDataDeAdição()));

        TableColumn<Atividade, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setCellFactory(param -> new TableCell<>() {
                    private final Button btnEditar = new Button("Editar");
                    private final Button btnDeletar = new Button("Deletar");
                    private final HBox hBox = new HBox(10, btnEditar, btnDeletar);

                    {
                        btnEditar.setOnAction(e -> {
                            Atividade atividade = getTableView().getItems().get(getIndex());
                            abrirFormularioEdicao(atividade);
                        });

                        btnDeletar.setOnAction(e -> {
                            Atividade atividade = getTableView().getItems().get(getIndex());
                            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente deletar?", ButtonType.YES, ButtonType.NO);
                            confirmacao.showAndWait().ifPresent(resposta -> {
                                if (resposta == ButtonType.YES) {
                                    listaAtividades.remove(atividade);
                                    salvarAtividades();
                                }
                            });
                        });
                    }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                }
            }
        });


        tabela.getColumns().addAll(colNome, colDesc, colData, colAcoes);
        tabela.setItems(listaAtividades); // Itens da tabela
        tabela.setPrefHeight(300); // Altura da tabela

        // Ajusta automaticamente a largura das colunas para preencher a tabela (sem colunas extras)
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Remove o botão de menu de colunas (que permite ocultar/exibir colunas)

        carregarAtividades(); // Carrega os vídeos do arquivo

        layout.getChildren().addAll(titulo, btnAdicionar, tabela);

        Scene cena = new Scene(layout, 800, 500);
        this.stage.setScene(cena);
    }

    // Abre o Modal com o formulário para adicionar vídeo
    private void abrirFormulario() {
        Stage modal = new Stage();
        modal.setTitle("Nova atividade");

        VBox formLayout = new VBox();
        formLayout.setStyle("-fx-padding: 20; -fx-alignment: top_left;");
        formLayout.setSpacing(25);

        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome da atividade");
        campoNome.setMaxWidth(300);

        Label labelDesc = new Label("Descrição:");
        TextField campoDesc = new TextField();
        campoDesc.setPromptText("Descrição da atividade");
        campoDesc.setMaxWidth(300);

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setPrefWidth(200);
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            String descricao = campoDesc.getText().trim();
            if (!nome.isEmpty() && !descricao.isEmpty()) {
                LocalDate dataAtual = LocalDate.now();
                String dataFormatada = dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                Atividade atividade = new Atividade(nome, descricao, dataFormatada);
                listaAtividades.add(atividade);
                salvarAtividades(); // Salva no arquivo
                modal.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!");
                alert.showAndWait();
            }
        });

        formLayout.getChildren().addAll(labelNome, campoNome, labelDesc, campoDesc, btnSalvar);

        Scene formScene = new Scene(formLayout, 450, 300); // Tamanho maior da janela
        modal.setScene(formScene);
        modal.initOwner(this.stage); // Janela pai
        modal.showAndWait();
    }

    // Serializa e salva os vídeos no arquivo
    private void salvarAtividades() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            oos.writeObject(listaAtividades.stream().toList());
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao salvar atividade, arquivo não encontrado: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro desconhecido ao salvar atividade: " + e.getMessage());
        }
    }

    // Lê os vídeos salvos do arquivo
    private void carregarAtividades() {
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                List<Atividade> atividades = (List<Atividade>) ois.readObject();
                listaAtividades.setAll(atividades);
            } catch (FileNotFoundException e) {
                System.err.println("Erro ao carregar vídeo, arquivo não encontrado: " + e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro desconhecido ao carregar vídeo: " + e.getMessage());
            }
        }
    }

    private void abrirFormularioEdicao(Atividade atividade) {
        Stage modal = new Stage();
        modal.setTitle("Editar atividade");

        VBox formLayout = new VBox();
        formLayout.setStyle("-fx-padding: 20; -fx-alignment: top_left;");
        formLayout.setSpacing(25);

        Label labelNome = new Label("Nome:");
        TextField campoNome = new TextField(atividade.getNome());
        campoNome.setPromptText("Nome da atividade");
        campoNome.setMaxWidth(300);

        Label labelDesc = new Label("Descrição:");
        TextField campoDesc = new TextField(atividade.getDescricao());
        campoDesc.setPromptText("Descrição da atividade");
        campoDesc.setMaxWidth(300);

        Button btnSalvar = new Button("Salvar alterações");
        btnSalvar.setPrefWidth(200);
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim();
            String descricao = campoDesc.getText().trim();
            if (!nome.isEmpty() && !descricao.isEmpty()) {
                atividade.setNome(nome);
                atividade.setDescricao(descricao);
                tabela.refresh(); // Atualiza a tabela
                salvarAtividades();
                modal.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!");
                alert.showAndWait();
            }
        });

        formLayout.getChildren().addAll(labelNome, campoNome, labelDesc, campoDesc, btnSalvar);

        Scene formScene = new Scene(formLayout, 450, 300); // Aumenta o tamanho da janela
        modal.setScene(formScene);
        modal.initOwner(this.stage);
        modal.showAndWait();
    }
}
