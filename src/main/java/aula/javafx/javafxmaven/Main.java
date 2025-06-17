package aula.javafx.javafxmaven;

import aula.javafx.javafxmaven.controllers.AtividadesController;
import aula.javafx.javafxmaven.controllers.HomeController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private BorderPane rootLayout; // The main layout for the application

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Set the stage to be maximized
            stage.setMaximized(true);
            stage.setTitle("GymTutor Pro");

            rootLayout = new BorderPane();
            Scene scene = new Scene(rootLayout, 1024, 768); // Initial size, will be maximized

            // Create the sidebar
            VBox sidebar = createSidebar(stage);
            rootLayout.setLeft(sidebar);

            // Set the initial content to the Home fragment
            HomeController homeController = new HomeController(stage, rootLayout);
            homeController.showHomeFragment(); // This will put the Home fragment in the center

            stage.setScene(scene);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace(); // Print stack trace for debugging
            Alert a = new Alert(Alert.AlertType.ERROR, "Ocorreu um erro inesperado ao iniciar a aplicação!");
            a.setHeaderText("Erro");
            a.showAndWait();
        }
    }

    private VBox createSidebar(Stage stage) {
        VBox sidebar = new VBox(10); // 10 pixels spacing
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #336699;"); // Dark blue background for sidebar

        Button btnHome = new Button("Home");
        btnHome.setPrefWidth(180);
        btnHome.setPrefHeight(40);
        btnHome.setStyle("-fx-font-size: 16px; -fx-background-color: #5cb85c; -fx-text-fill: white;");
        btnHome.setOnAction(e -> {
            HomeController homeController = new HomeController(stage, rootLayout);
            homeController.showHomeFragment();
        });

        Button btnAtividades = new Button("Cadastrar Atividades");
        btnAtividades.setPrefWidth(180);
        btnAtividades.setPrefHeight(40);
        btnAtividades.setStyle("-fx-font-size: 16px; -fx-background-color: #5bc0de; -fx-text-fill: white;");
        btnAtividades.setOnAction(e -> {
            AtividadesController atividadesController = new AtividadesController(stage, rootLayout);
            atividadesController.showAtividadesFragment();
        });

        sidebar.getChildren().addAll(btnHome, btnAtividades);
        return sidebar;
    }
}