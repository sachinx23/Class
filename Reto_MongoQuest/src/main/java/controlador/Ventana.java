package controlador;

import exception.ShowAlert;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import log.LogApp;

/**
 * Cambio de pantallas.
 */
public class Ventana {

    // Crea la escena con la hoja de estilos de la aplicación
    public static Scene crearEscena(Parent root) {
        Scene escena = new Scene(root);
        escena.getStylesheets().add(Ventana.class.getResource("/css/estilo.css").toExternalForm());
        return escena;
    }

    // Cambia la pantalla de la ventana donde está el nodo
    public static void cambiar(Node nodo, String ruta) {
        try {
            Parent root = FXMLLoader.load(Ventana.class.getResource(ruta));
            Stage stage = (Stage) nodo.getScene().getWindow();
            stage.setScene(crearEscena(root));
        } catch (IOException e) {
            LogApp.error("Abrir pantalla " + ruta, e);
            ShowAlert.error("No se pudo abrir la pantalla.");
        }
    }

    // Carga una pantalla dentro de un panel (lo usa el panel de administrador)
    public static void cargarEn(Pane panel, String ruta) {
        try {
            Parent vista = FXMLLoader.load(Ventana.class.getResource(ruta));
            panel.getChildren().setAll(vista);
        } catch (IOException e) {
            LogApp.error("Abrir pantalla " + ruta, e);
            ShowAlert.error("No se pudo abrir la pantalla.");
        }
    }
}
