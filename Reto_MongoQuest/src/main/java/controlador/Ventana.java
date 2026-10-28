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
 * Métodos para cambiar de pantalla.
 */
public class Ventana {

    /** Esta clase solo tiene métodos estáticos: no se crean objetos de ella. */
    private Ventana() {
    }

    /**
     * Crea una escena con la hoja de estilos de la aplicación (css/estilo.css).
     *
     * @param root contenido de la pantalla
     * @return la escena lista para poner en una ventana
     */
    public static Scene crearEscena(Parent root) {
        Scene escena = new Scene(root);
        escena.getStylesheets().add(Ventana.class.getResource("/css/estilo.css").toExternalForm());
        return escena;
    }

    /**
     * Cambia la pantalla de la ventana donde está el nodo.
     *
     * @param nodo cualquier control de la pantalla actual (sirve para saber cuál es la ventana)
     * @param ruta ruta del FXML nuevo, por ejemplo "/vista/IniciarSesion.fxml"
     */
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

    /**
     * Carga una pantalla dentro de un panel (lo usa el panel de administrador).
     *
     * @param panel panel donde se pone la pantalla
     * @param ruta  ruta del FXML, por ejemplo "/vista/AltaEmpleado.fxml"
     */
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
