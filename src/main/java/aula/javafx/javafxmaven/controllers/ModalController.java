package aula.javafx.javafxmaven.controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModalController {
    private Stage stage;

    private Stage stageOwner;
    private Scene cena;

    public ModalController(Stage stageOwner) {
        this.stageOwner = stageOwner;
    }

    public void mostrar() {
        criarUI();
        stage.showAndWait();
    }

    private void criarUI() {
        this.stage = new Stage();
        stage.setTitle("Janela modal ");

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.stageOwner);

        Label labelMensagem = new Label("Esta é uma janela Modal");
        labelMensagem.setFont(new Font("Arial", 26));

        Label labelText = new Label("Ela permanece sobre a outra até ser fechada");
        labelText.setFont(new Font("Arial", 18));

        Button btnFechar = new Button("Fechar Janela");
        btnFechar.setOnAction(( event) -> {this.stage.close();});
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().add(labelMensagem);
        layout.getChildren().add(labelText);
        layout.getChildren().add(btnFechar);
        this.cena = new Scene(layout, 500, 200 );
        this.stage.setScene(this.cena);

    }

}
