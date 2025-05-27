package aula.javafx.javafxmaven.persistencia;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Exemplo2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Exemplo2 de JavaFX ");

        VBox box1 = new VBox(); // Layout vertical, adiciona tudo verticalmente
        box1.setAlignment(Pos.CENTER);
        box1.setSpacing(20);

        Label lbl1 = new Label("Outro exemplo de JavaFX");
        lbl1.setAlignment(Pos.CENTER);
        lbl1.setFont(new Font("Arial", 28));
        box1.getChildren().add(lbl1);

        Button btn1 = new Button();
        btn1.setText("Clique aqui");
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Aqui");
            }
        });
        box1.getChildren().add(btn1);

        Button btn2 = new Button("Fechar");
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        box1.getChildren().add(btn2);

        Scene scene = new Scene(box1, 400, 300);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
