package aula.javafx.javafxmaven;

import aula.javafx.javafxmaven.controllers.HomeController;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            HomeController m = new HomeController(stage);
            m.mostrar();
        }catch (Exception ex){
            Alert a = new Alert(Alert.AlertType.ERROR,  "Ocorreu um erro inesperado ao iniciar a aplicação!");
            a.setHeaderText("Erro");
            a.showAndWait();
        }

    }

}
