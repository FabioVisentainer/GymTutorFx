package aula.javafx.javafxmaven;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;



public class Maindes extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Este é o meu exemplo em JavaFx");
        Label lbl1 = new Label("Este é meu primeiro teste JavaFx");
        lbl1.setFont(new Font("Arial", 24));
        lbl1.setAlignment(Pos.CENTER);
        Scene scene = new Scene(lbl1, 500, 300);
        stage.setScene(scene); // Colocar scena dentro do Stage
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }


}

