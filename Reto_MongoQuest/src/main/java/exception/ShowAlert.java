package exception;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Ventanas emergentes (avisos) de la aplicación.
 */
public class ShowAlert {

    public static void showAlert(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void info(String titulo, String mensaje) {
        showAlert(titulo, mensaje, Alert.AlertType.INFORMATION);
    }

    public static void error(String mensaje) {
        showAlert("Error", mensaje, Alert.AlertType.ERROR);
    }

    // Pregunta algo al usuario. Devuelve true si pulsa Aceptar
    public static boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }
}
