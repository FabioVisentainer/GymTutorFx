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

        //Redireciona para pagina Home
        Button btnAtividades = new Button("Cadastrar Atividades");
        btnAtividades.setFont(new Font("Arial", 18));
        btnAtividades.setOnAction(e -> {
            //Abre o controlador que
            HomeController home = new HomeController(this.stage);
            home.mostrar();
        });
        layout.getChildren().add(btnAtividades);

        //Redireciona para pagina Home
        Button btnVideos = new Button("Cadastrar Vídeos");
        btnVideos.setFont(new Font("Arial", 18));
        btnVideos.setOnAction(e -> {
            //Abre o controlador que
            HomeController home = new HomeController(this.stage);
            home.mostrar();
        });
        layout.getChildren().add(btnVideos);


        this.cena = new Scene(layout, 800, 500);
        this.stage.setScene(this.cena);
    }
}