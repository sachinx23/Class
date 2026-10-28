package controlador;

import exception.ErrorMessages;
import exception.ShowAlert;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import log.LogApp;

/**
 * Panel de administrador: cada opción del menú carga su pantalla dentro de panelContenido.
 */
public class AdministradorController {

    @FXML private AnchorPane panelContenido;

    @FXML
    public void initialize() {
        Ventana.cargarEn(panelContenido, "/vista/PerfilAdmin.fxml");
    }

    @FXML
    public void miPerfil(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/PerfilAdmin.fxml");
    }

    @FXML
    public void altaEmpleado(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/AltaEmpleado.fxml");
    }

    @FXML
    public void cambiarPuesto(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/CambiarPuesto.fxml");
    }

    @FXML
    public void borrarEmpleado(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/BorrarEmpleado.fxml");
    }

    @FXML
    public void gestionSucursal(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/GestionSucursal.fxml");
    }

    @FXML
    public void verLog(ActionEvent event) {
        try {
            TextArea areaLog = new TextArea(LogApp.leerLog());
            areaLog.setEditable(false);
            areaLog.setWrapText(true);
            areaLog.setPrefSize(panelContenido.getPrefWidth(), panelContenido.getPrefHeight());
            panelContenido.getChildren().setAll(areaLog);
        } catch (IOException e) {
            ShowAlert.error(ErrorMessages.READ_LOG);
        }
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        if (ShowAlert.confirmar("Salir", "¿Deseas salir del panel de administrador?")) {
            Sesion.cerrar();
            Ventana.cambiar(panelContenido, "/vista/IniciarSesion.fxml");
        }
    }
}
