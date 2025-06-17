package aula.javafx.javafxmaven.controllers;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HomeController {

    private Stage stage;
    private BorderPane rootLayout; // Reference to the main BorderPane

    public HomeController(Stage stage, BorderPane rootLayout) {
        this.stage = stage;
        this.rootLayout = rootLayout;
    }

    // Method to display the Home fragment in the center of the rootLayout
    public void showHomeFragment() {
        VBox homeContent = new VBox();
        homeContent.setAlignment(Pos.CENTER);
        homeContent.setPadding(new javafx.geometry.Insets(50));
        homeContent.setStyle("-fx-background-color: #f0f0f0;"); // Light grey background

        Label labelMensagem = new Label("Bem vindo ao GymTutor Pro!");
        labelMensagem.setFont(new Font("Arial", 30));
        labelMensagem.setStyle("-fx-text-fill: #333333;"); // Darker text color

        homeContent.getChildren().add(labelMensagem);

        // Set this VBox as the center content of the main BorderPane
        rootLayout.setCenter(homeContent);
    }
}