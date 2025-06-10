package aula.javafx.javafxmaven.controllers;

import aula.javafx.javafxmaven.models.Atividade;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomeController {

    // Declaração dos atributos principais da classe: o palco (janela) e a cena (conteúdo dentro da janela)
    private Stage stage;
    private Scene cena;

    // Construtor que recebe o Stage (janela principal)
    public HomeController(Stage stage) {
        this.stage = stage;
    }

    // Metodo que será chamado para exibir a tela
    public void mostrar() {
        criarUI();        // Cria a interface gráfica
        this.stage.show(); // Exibe a janela
    }

    // Metodo responsável por montar toda a interface gráfica da tela Home
    private void criarUI() {
        stage.setTitle("Tela Home"); // Define o título da janela

        // Cria o layout vertical (VBox) que organiza os componentes em coluna
        VBox layout = new VBox();
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;"); // Define o padding e alinha os elementos ao centro
        layout.setSpacing(30); // Espaçamento vertical entre os componentes

        // Cria um rótulo de título
        // Cria um rótulo (Label) com texto explicativo
        Label labelMensagem = new Label("Bem vindo ao cadastro de atividades e videos");
        labelMensagem.setFont(new Font("Arial", 26)); // Define a fonte e o tamanho do texto
        layout.getChildren().add(labelMensagem); // Adiciona o rótulo ao layout


        // Cria um botão chamado "Cadastrar Atividades"
        Button btnAtividades = new Button("Cadastrar Atividades");
        btnAtividades.setFont(new Font("Arial", 18)); // Define a fonte do botão
        // Define a ação ao clicar no botão
        btnAtividades.setOnAction(e -> {
            //Abre o controlador que
            AtividadesController home = new AtividadesController(this.stage);
            home.mostrar();
        });
        // Adiciona o botão "Cadastrar Atividades" ao layout
        layout.getChildren().add(btnAtividades);

//        // Cria um segundo botão chamado "Cadastrar Vídeos"
//        Button btnVideos = new Button("Cadastrar Vídeos");
//        btnVideos.setFont(new Font("Arial", 18)); // Define a fonte do botão
//        // Define a ação ao clicar no botão
//        btnVideos.setOnAction(e -> {
//            //Abre o controlador que
//            VideoController video = new VideoController(this.stage);
//            video.mostrar();
//        });
//        // Adiciona o botão "Cadastrar Vídeos" ao layout
//        layout.getChildren().add(btnVideos);

        // Cria uma nova cena com o layout e define o tamanho da janela
        this.cena = new Scene(layout, 800, 500);
        // Define a cena atual da janela como sendo essa nova cena
        this.stage.setScene(this.cena);
    }
}