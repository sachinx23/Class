package com.mycompany.reto_1;

import controlador.Ventana;
import dao.MongoConnection;
import exception.OurException;
import exception.ShowAlert;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Reto_1 extends Application {

    // Dirección de MongoDB y nombre de la base de datos
    private static final String URI = "mongodb://localhost:27017/?serverSelectionTimeoutMS=5000";
    private static final String BASE_DATOS = "Tel--ONDA";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // La conexión se abre una sola vez al arrancar y se cierra en stop()
        try {
            MongoConnection.connect(URI, BASE_DATOS);
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
            Platform.exit();
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("/vista/IniciarSesion.fxml"));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));
        primaryStage.setTitle("Tel-ONDA");
        primaryStage.setScene(Ventana.crearEscena(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        MongoConnection.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
