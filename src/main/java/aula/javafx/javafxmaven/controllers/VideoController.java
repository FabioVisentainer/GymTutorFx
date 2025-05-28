package aula.javafx.javafxmaven.controllers;

import aula.javafx.javafxmaven.models.Atividade;
import aula.javafx.javafxmaven.models.Video;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        stage.setTitle("Cadastro de Vídeos");       // Define o título da janela principal

        // Cria um layout vertical (VBox) para organizar os componentes em coluna
        VBox layout = new VBox();
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;"); // Aplica estilo CSS para espaçamento interno (padding) e centralização dos elementos
        layout.setSpacing(20); // Define o espaçamento vertical entre os elementos do VBox

        // Cria um título para a tela com fonte Arial tamanho 26
        Label titulo = new Label("Cadastro de Vídeos");
        titulo.setFont(new Font("Arial", 26));

        // Cria um botão chamado "Voltar Home"
        Button btnVoltar = new Button("Voltar a Home");
        btnVoltar.setFont(new Font("Arial", 9)); // Define a fonte do botão
        // Define a ação ao clicar no botão
        btnVoltar.setOnAction(e -> {
            //Abre o controlador que
            HomeController home = new HomeController(this.stage);
            home.mostrar();
        });
        HBox topo = new HBox(btnVoltar); // Cria um HBox para alinhar o botão à esquerda
        topo.setStyle("-fx-alignment: top-left;");
        topo.setSpacing(10);
        layout.getChildren().add(topo); // Adiciona o botão ao layout

        // Cria um botão para adicionar novos vídeos
        Button btnAdicionar = new Button("Adicionar Vídeo");
        btnAdicionar.setFont(new Font("Arial", 18));
        btnAdicionar.setOnAction(e -> abrirFormulario()); // Configura ação do botão para abrir o formulário modal de cadastro

        // Inicializa a tabela que exibirá os vídeos
        tabela = new TableView<>();
        tabela.setItems(listaVideos); // Vincula a tabela à lista observável de vídeos para exibir os dados
        tabela.setPrefHeight(300); // Define altura preferencial da tabela para 300 pixels
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Ajusta automaticamente a largura das colunas para preencher a tabela (sem colunas extras)
        tabela.setTableMenuButtonVisible(false); // Remove o botão de menu de colunas (que permite ocultar/exibir colunas)

        // Cria a coluna "Nome" que exibirá o nome do vídeo
        TableColumn<Video, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome())); // Configura como a coluna vai obter o valor para cada célula (nome do vídeo)

        // Cria a coluna "Link" que exibirá o link do vídeo
        TableColumn<Video, String> colLink = new TableColumn<>("Link");
        colLink.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLink())); // Configura a coluna para obter o link do vídeo para cada célula

        // Cria a coluna "Data" que exibirá o link do vídeo
        TableColumn<Video, String> colData = new TableColumn<>("Data de adição");
        colData.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDataDeAdicao())); // Configura a coluna para obter o link do vídeo para cada célula

        TableColumn<Video, Void> colAcoes = new TableColumn<>("Ações");
        colAcoes.setCellFactory(param -> new TableCell<>() {
                    private final Button btnEditar = new Button("Editar");
                    private final Button btnDeletar = new Button("Deletar");
                    private final HBox hBox = new HBox(10, btnEditar, btnDeletar);

                    {
                        btnEditar.setOnAction(e -> {
                            Video video = getTableView().getItems().get(getIndex());
                            abrirFormularioEdicao(video);
                        });

                        btnDeletar.setOnAction(e -> {
                            Video video = getTableView().getItems().get(getIndex());
                            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente deletar?", ButtonType.YES, ButtonType.NO);
                            confirmacao.showAndWait().ifPresent(resposta -> {
                                if (resposta == ButtonType.YES) {
                                    listaVideos.remove(video);
                                    salvarVideos();
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

        // Define as colunas visíveis da tabela, substituindo qualquer coluna anterior
        tabela.getColumns().addAll(colNome, colLink, colData, colAcoes);

        // Adiciona os componentes criados ao layout principal em ordem vertical
        layout.getChildren().addAll(titulo, btnAdicionar, tabela);

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
        formLayout.setSpacing(25); // Espaçamento entre os componentes

        // Campo de texto para o nome do vídeo, com texto guia (placeholder)
        TextField campoNome = new TextField();
        campoNome.setPromptText("Nome do vídeo");
        campoNome.setMaxWidth(300);

        // Campo de texto para o link do vídeo, com texto guia
        TextField campoLink = new TextField();
        campoLink.setPromptText("Link do vídeo");
        campoNome.setMaxWidth(300);

        // Botão para salvar o novo vídeo
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            String nome = campoNome.getText().trim(); // Pega texto do campo nome e remove espaços extras
            String link = campoLink.getText().trim(); // Pega texto do campo link e remove espaços extras

            // Verifica se os campos estão preenchidos e se o link é válido
            if (!nome.isEmpty() && !link.isEmpty() && linkValido(link)) {
                LocalDate dataAtual = LocalDate.now();
                String dataFormatada = dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                Video video = new Video(nome, link, dataFormatada); // Cria objeto Video
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
    private void abrirFormularioEdicao(Video video) {
        Stage modal = new Stage();
        modal.setTitle("Editar Vídeo");

        VBox formLayout = new VBox();
        formLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        formLayout.setSpacing(25);

        // Campos preenchidos com dados atuais do vídeo selecionado
        TextField campoNome = new TextField(video.getNome());
        campoNome.setMaxWidth(300);
        TextField campoLink = new TextField(video.getLink());
        campoLink.setMaxWidth(300);


        Button btnSalvar = new Button("Salvar Alterações");
        btnSalvar.setOnAction(e -> {
            String novoNome = campoNome.getText().trim();
            String novoLink = campoLink.getText().trim();

            // Verifica dados preenchidos e validade do link
            if (!novoNome.isEmpty() && !novoLink.isEmpty() && linkValido(novoLink)) {
                video.setNome(novoNome); // Atualiza nome
                video.setLink(novoLink); // Atualiza link
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
        modal.setScene(new Scene(formLayout, 450, 300));
        modal.initOwner(this.stage);
        modal.showAndWait();
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