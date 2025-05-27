package aula.javafx.javafxmaven.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomeController {

    private Stage stage;
    private Scene cena;

    public HomeController(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        criarUI();
        this.stage.show();
    }

    private void criarUI() {
        stage.setTitle("Tela Home");

        VBox layout = new VBox();
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.setSpacing(30);

        Label titulo = new Label("Bem-vindo à Tela Home");
        titulo.setFont(new Font("Arial", 26));

        Button btnCadastrar = new Button("Cadastrar Atividades");
        btnCadastrar.setFont(new Font("Arial", 18));

        // Aqui você pode definir o que acontece ao clicar no botão
        btnCadastrar.setOnAction(e -> {
            System.out.println("Botão 'Cadastrar Atividades' clicado!");
            // Aqui você pode abrir outra janela, ou mudar a cena, etc.
        });

        layout.getChildren().addAll(titulo, btnCadastrar);

        this.cena = new Scene(layout, 800, 500);
        this.stage.setScene(this.cena);
    }
}