package aula.javafx.javafxmaven.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainController {

    // Atributos que representam a janela principal (Stage) e o conteúdo dela (Scene)
    private Stage stage;
    private Scene cena;

    // Construtor da classe que recebe a janela principal e armazena na variável de instância
    public MainController(Stage stage) {
        this.stage = stage;
    }

    // Metodo chamado para exibir a interface dessa tela
    public void mostrar() {
        criarUI();        // Cria os elementos visuais da interface
        this.stage.show(); // Exibe a janela
    }

    // Metodo responsável por criar toda a interface gráfica da janela principal
    public void criarUI() {
        stage.setTitle("Janela Main"); // Define o título da janela

        // Cria um layout vertical para empilhar os elementos (VBox)
        VBox layout = new VBox();

        // Aplica estilo CSS diretamente: padding (espaço interno) e alinhamento ao centro
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.setSpacing(30); // Espaçamento vertical entre os componentes do VBox

        // Cria um rótulo (Label) com texto explicativo
        Label labelMensagem = new Label("Bem vindo ao cadastro de atividades e videos");
        labelMensagem.setFont(new Font("Arial", 26)); // Define a fonte e o tamanho do texto
        layout.getChildren().add(labelMensagem); // Adiciona o rótulo ao layout


        // Cria um segundo botão que redireciona para a tela "Home"
        Button btnHome = new Button("Home");
        btnHome.setOnAction(e -> {
            // Ao clicar, cria um novo HomeController e exibe a tela Home
            HomeController home = new HomeController(this.stage);
            home.mostrar();
        });
        layout.getChildren().add(btnHome); // Adiciona o botão ao layout

        // Cria uma nova cena com o layout definido e tamanho 800x500 pixels
        this.cena = new Scene(layout, 800, 500);

        // Define essa cena como a atual do palco (janela principal)
        this.stage.setScene(this.cena);
    }
}