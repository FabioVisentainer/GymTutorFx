module aula.javafx.javafxmaven {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens aula.javafx.javafxmaven to javafx.fxml;
    exports aula.javafx.javafxmaven;
    exports aula.javafx.javafxmaven.controllers;
    opens aula.javafx.javafxmaven.controllers to javafx.fxml;
    exports aula.javafx.javafxmaven.models;
    opens aula.javafx.javafxmaven.models to javafx.fxml;
}