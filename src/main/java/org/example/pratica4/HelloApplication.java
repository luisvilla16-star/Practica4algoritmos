
package org.example.pratica4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {

        HelloController controlador = new HelloController();

        Scene escena = new Scene(
                controlador.crearInterfaz(),
                1100,
                500
        );

        stage.setTitle("Eight Off - Solitario");
        stage.setScene(escena);

        stage.setMinWidth(900);
        stage.setMinHeight(650);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
