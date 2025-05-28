package aula.javafx.javafxmaven.controllers;

import aula.javafx.javafxmaven.models.Video;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

public class VideoController {

    // Janela principal
    private final Stage stage;

    // Arquivo onde os vídeos serão salvos (serialização)
    private final File arquivo = new File("videos.dat");

    // Lista observável que alimenta a Tabela
    private final ObservableList<Video> listaVideos = FXCollections.observableArrayList();

    // Tabela que será exibida na tela
    private TableView<Video> tabela;

    public VideoController(Stage stage) {
        this.stage = stage;
    }

    // Metodo público para exibir a tela
    public void mostrar() {
        criarUI(); // cria os componentes da interface
        this.stage.show(); // exibe a janela
    }

    // Cria a interface gráfica da janela principal
    private void criarUI() {
        // Define o título da janela principal
        stage.setTitle("Cadastro de Vídeos");

        // Cria um layout vertical (VBox) para organizar os componentes em coluna
        VBox layout = new VBox();
        // Aplica estilo CSS para espaçamento interno (padding) e centralização dos elementos
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        // Define o espaçamento vertical entre os elementos do VBox
        layout.setSpacing(20);

        // Cria um título para a tela com fonte Arial tamanho 26
        Label titulo = new Label("Cadastro de Vídeos");
        titulo.setFont(new Font("Arial", 26));

        // Cria um botão para adicionar novos vídeos
        Button btnAdicionar = new Button("Adicionar Vídeo");
        btnAdicionar.setFont(new Font("Arial", 18));
        // Configura ação do botão para abrir o formulário modal de cadastro
        btnAdicionar.setOnAction(e -> abrirFormulario());

        // Inicializa a tabela que exibirá os vídeos
        tabela = new TableView<>();
        // Vincula a tabela à lista observável de vídeos para exibir os dados
        tabela.setItems(listaVideos);
        // Define altura preferencial da tabela para 300 pixels
        tabela.setPrefHeight(300);
        // Ajusta automaticamente a largura das colunas para preencher a tabela (sem colunas extras)
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Remove o botão de menu de colunas (que permite ocultar/exibir colunas)
        tabela.setTableMenuButtonVisible(false);

        // Cria a coluna "Nome" que exibirá o nome do vídeo
        TableColumn<Video, String> colNome = new TableColumn<>("Nome");
        // Configura como a coluna vai obter o valor para cada célula (nome do vídeo)
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));

        // Cria a coluna "Link" que exibirá o link do vídeo
        TableColumn<Video, String> colLink = new TableColumn<>("Link");
        // Configura a coluna para obter o link do vídeo para cada célula
        colLink.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLink()));

        // Define as colunas visíveis da tabela, substituindo qualquer coluna anterior
        // Isso evita que apareçam colunas "fantasmas" indesejadas
        tabela.getColumns().setAll(colNome, colLink);

        // Cria botão para editar o vídeo selecionado na tabela
        Button btnEditar = new Button("Editar Selecionado");
        btnEditar.setFont(new Font("Arial", 16));
        // Define ação para abrir o formulário de edição do vídeo selecionado
        btnEditar.setOnAction(e -> editarSelecionado());

        // Cria botão para excluir o vídeo selecionado na tabela
        Button btnExcluir = new Button("Excluir Selecionado");
        btnExcluir.setFont(new Font("Arial", 16));
        // Define ação para excluir o vídeo selecionado após confirmação
        btnExcluir.setOnAction(e -> excluirSelecionado());

        // Agrupa os botões de editar e excluir horizontalmente com espaçamento de 10 pixels
        HBox botoesSecundarios = new HBox(10, btnEditar, btnExcluir);
        // Centraliza os botões dentro do HBox
        botoesSecundarios.setStyle("-fx-alignment: center;");

        // Adiciona os componentes criados ao layout principal em ordem vertical
        layout.getChildren().addAll(titulo, btnAdicionar, tabela, botoesSecundarios);

        // Carrega a lista de vídeos previamente salva em arquivo para popular a tabela ao iniciar
        carregarVideos();

        // Cria a cena principal com o layout e dimensões de 800x500 pixels
        Scene cena = new Scene(layout, 800, 500);
        // Define a cena da janela principal para esta criada
        this.stage.setScene(cena);
    }

    // Abre o formulário para adicionar um novo vídeo
    private void abrirFormulario() {
        Stage modal = new Stage(); // Cria uma nova janela modal (pop-up)
        modal.setTitle("Novo Vídeo"); // Define o título da janela

        VBox formLayout = new VBox(); // Layout vertical para organizar os campos
        formLayout.setStyle("-fx-padding: 20; -fx-alignment: center;"); // Espaçamento e centralização
        formLayout.setSpacing(15); // Espaçamento entre os componentes

        // Campo de texto para o nome do vídeo, com texto guia (placeholder)
        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome do vídeo");

        // Campo de texto para o link do vídeo, com texto guia
        TextField campoLink = new TextField();
        campoLink.setPromptText("Link do vídeo");

        // Botão para salvar o novo vídeo
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim(); // Pega texto do campo nome e remove espaços extras
            String link = campoLink.getText().trim(); // Pega texto do campo link e remove espaços extras

            // Verifica se os campos estão preenchidos e se o link é válido
            if (!nome.isEmpty() && !link.isEmpty() && linkValido(link)) {
                Video video = new Video(nome, link); // Cria objeto Video
                listaVideos.add(video); // Adiciona vídeo na lista observável
                salvarVideos(); // Salva a lista atualizada no arquivo
                modal.close(); // Fecha o formulário modal
            } else {
                // Exibe alerta de aviso caso os campos estejam inválidos ou incompletos
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Preencha todos os campos corretamente!\nO link deve ser uma URL válida.");
                alert.showAndWait();
            }
        });

        // Adiciona os componentes ao layout do formulário em ordem
        formLayout.getChildren().addAll(
                new Label("Nome:"), campoNome,
                new Label("Link:"), campoLink,
                btnSalvar
        );

        // Cria cena com o layout definido e tamanho
        Scene formScene = new Scene(formLayout, 300, 250);
        modal.setScene(formScene);
        modal.initOwner(this.stage); // Define a janela principal como dona do modal
        modal.showAndWait(); // Mostra o modal e aguarda seu fechamento
    }

    // Salva a lista de vídeos no arquivo como objeto serializado
    private void salvarVideos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            // Converte a lista observável para lista comum e grava no arquivo
            oos.writeObject(listaVideos.stream().toList());
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao salvar vídeo, arquivo não encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro desconhecido ao salvar vídeo: " + e.getMessage());
        }
    }

    // Carrega os vídeos do arquivo, se existir, e popula a lista observável
    private void carregarVideos() {
        if (arquivo.exists()) { // Verifica se o arquivo existe
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                // Lê a lista de vídeos do arquivo e atualiza a lista observável
                List<Video> videos = (List<Video>) ois.readObject();
                listaVideos.setAll(videos);
            } catch (FileNotFoundException e) {
                System.err.println("Erro ao carregar vídeo, arquivo não encontrado: " + e.getMessage());
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro desconhecido ao carregar vídeo: " + e.getMessage());
            }
        }
    }

    // Abre o formulário de edição do vídeo selecionado na tabela
    private void editarSelecionado() {
        Video selecionado = tabela.getSelectionModel().getSelectedItem(); // Obtém vídeo selecionado
        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Selecione um vídeo para editar.");
            alert.showAndWait();
            return; // Sai do método se nada estiver selecionado
        }

        Stage modal = new Stage();
        modal.setTitle("Editar Vídeo");

        VBox formLayout = new VBox();
        formLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        formLayout.setSpacing(15);

        // Campos preenchidos com dados atuais do vídeo selecionado
        TextField campoNome = new TextField(selecionado.getNome());
        TextField campoLink = new TextField(selecionado.getLink());

        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.setOnAction(e -> {
            String novoNome = campoNome.getText().trim();
            String novoLink = campoLink.getText().trim();

            // Verifica dados preenchidos e validade do link
            if (!novoNome.isEmpty() && !novoLink.isEmpty() && linkValido(novoLink)) {
                selecionado.setNome(novoNome); // Atualiza nome
                selecionado.setLink(novoLink); // Atualiza link
                tabela.refresh(); // Atualiza visualmente a tabela para refletir as mudanças
                salvarVideos(); // Persiste alterações no arquivo
                modal.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "Preencha todos os campos corretamente!\nO link deve ser uma URL válida.");
                alert.showAndWait();
            }
        });

        formLayout.getChildren().addAll(
                new Label("Nome:"), campoNome,
                new Label("Link:"), campoLink,
                btnSalvar
        );
        modal.setScene(new Scene(formLayout, 300, 250));
        modal.initOwner(this.stage);
        modal.showAndWait();
    }

    // Remove o vídeo selecionado após confirmação do usuário
    private void excluirSelecionado() {
        Video selecionado = tabela.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Selecione um vídeo para excluir.");
            alert.showAndWait();
            return;
        }

        // Confirmação antes de excluir
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja realmente excluir este vídeo?", ButtonType.YES, ButtonType.NO);
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.YES) {
                listaVideos.remove(selecionado); // Remove da lista observável
                salvarVideos(); // Atualiza o arquivo com a lista modificada
            }
        });
    }

    // Valida se uma string é uma URL válida
    private boolean linkValido(String link) {
        try {
            new java.net.URL(link); // Tenta criar um objeto URL
            return true; // Se der certo, link é válido
        } catch (Exception e) {
            return false; // Se lançar exceção, link é inválido
        }
    }
}