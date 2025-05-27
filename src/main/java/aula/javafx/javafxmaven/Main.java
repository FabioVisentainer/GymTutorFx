package aula.javafx.javafxmaven;

import aula.javafx.javafxmaven.controllers.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        MainController m = new MainController(stage);
        m.mostrar();
    }

}
