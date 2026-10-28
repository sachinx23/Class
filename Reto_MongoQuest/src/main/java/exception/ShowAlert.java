package exception;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Ventanas emergentes (avisos) de la aplicación.
 */
public class ShowAlert {

    /** Esta clase solo tiene métodos estáticos: no se crean objetos de ella. */
    private ShowAlert() {
    }

    /**
     * Enseña un aviso y espera a que el usuario lo cierre.
     *
     * @param titulo  título de la ventana
     * @param mensaje texto del aviso
     * @param tipo    tipo de aviso (información, error...)
     */
    public static void showAlert(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Enseña un aviso de información.
     *
     * @param titulo  título de la ventana
     * @param mensaje texto del aviso
     */
    public static void info(String titulo, String mensaje) {
        showAlert(titulo, mensaje, Alert.AlertType.INFORMATION);
    }

    /**
     * Enseña un aviso de error.
     *
     * @param mensaje texto del error
     */
    public static void error(String mensaje) {
        showAlert("Error", mensaje, Alert.AlertType.ERROR);
    }

    /**
     * Pregunta algo al usuario con los botones Aceptar y Cancelar.
     *
     * @param titulo  título de la ventana
     * @param mensaje pregunta
     * @return true si pulsa Aceptar
     */
    public static boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }
}
