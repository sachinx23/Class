package com.mycompany.reto_1;

import controlador.Sesion;
import controlador.Ventana;
import exception.OurException;
import exception.ShowAlert;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Aplicación Tel-ONDA (JavaFX).
 * <p>
 * Al arrancar se conecta a la base de datos y abre la pantalla de inicio de sesión.
 */
public class Reto_1 extends Application {

    /**
     * JavaFX crea el objeto al llamar a launch().
     */
    public Reto_1() {
    }

    /**
     * Se ejecuta al abrir la aplicación.
     *
     * @param primaryStage ventana principal
     * @throws Exception si no se puede cargar la primera pantalla
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // La conexión se abre una sola vez al arrancar y se cierra en stop()
        try {
            Sesion.getDao().conectar();
        } catch (OurException e) {
            // Si MongoDB está apagado, se avisa y se cierra la aplicación
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

    /**
     * Se ejecuta al cerrar la aplicación: cierra la conexión con la base de datos.
     */
    @Override
    public void stop() {
        Sesion.getDao().cerrar();
    }

    /**
     * Punto de entrada.
     *
     * @param args argumentos de la línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
