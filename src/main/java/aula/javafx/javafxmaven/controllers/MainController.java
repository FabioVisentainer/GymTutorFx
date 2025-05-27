package aula.javafx.javafxmaven.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainController {
    private Stage stage;
    private Scene cena;


    public MainController(Stage stage) {
        this.stage = stage;
    }

    public void mostrar() {
        criarUI();
        this.stage.show();
    }



    public void criarUI() {
        stage.setTitle("Janela Main");

        VBox layout = new VBox();
        //colcando css para conf layout
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.setSpacing(30); // intervalo entre os componentes do layuot
        // label para mostrar um texto
        Label labelMensagem = new Label("Exemplo de janelas modais");
        labelMensagem.setFont(new Font("Arial", 26));
        layout.getChildren().add(labelMensagem);

        // Abre um modal
        Button btn1 = new Button("Mostrar Modal");
        btn1.setOnAction(e -> {
            ModalController modal = new ModalController(this.stage);
            modal.mostrar();
        });
        layout.getChildren().add(btn1);

        //Redireciona para pagina Home
        Button btnHome = new Button("Login: Ir para Home");
        btnHome.setOnAction(e -> {
            //Abre o controlador que
            HomeController home = new HomeController(this.stage);
            home.mostrar();
        });
        layout.getChildren().add(btnHome);


        //Tamanho da pagina
        this.cena = new Scene(layout, 800, 500 );
        this.stage.setScene(this.cena);
    }


}
