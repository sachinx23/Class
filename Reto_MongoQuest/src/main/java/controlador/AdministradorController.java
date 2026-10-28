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
 * Panel de administrador (Admin.fxml).
 * <p>
 * Tiene un menú a la izquierda. Cada opción carga su pantalla dentro de {@code panelContenido}.
 */
public class AdministradorController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public AdministradorController() {
    }

    /** Panel donde se cargan las pantallas de cada opción del menú. */
    @FXML private AnchorPane panelContenido;

    /**
     * Se ejecuta al abrir la pantalla: enseña el perfil del administrador.
     */
    @FXML
    public void initialize() {
        Ventana.cargarEn(panelContenido, "/vista/PerfilAdmin.fxml");
    }

    /**
     * Opción "Mi perfil".
     *
     * @param event pulsación del botón
     */
    @FXML
    public void miPerfil(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/PerfilAdmin.fxml");
    }

    /**
     * Opción "Alta empleado". Caso de uso "Crear empleado".
     *
     * @param event pulsación del botón
     */
    @FXML
    public void altaEmpleado(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/AltaEmpleado.fxml");
    }

    /**
     * Opción "Cambiar puesto". Caso de uso "Ascender".
     *
     * @param event pulsación del botón
     */
    @FXML
    public void cambiarPuesto(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/CambiarPuesto.fxml");
    }

    /**
     * Opción "Borrar empleado". Caso de uso "Borrar cuenta (Empleado)".
     *
     * @param event pulsación del botón
     */
    @FXML
    public void borrarEmpleado(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/BorrarEmpleado.fxml");
    }

    /**
     * Opción "Sucursales". Casos de uso "Crear sucursal" y "Gestionar sucursal".
     *
     * @param event pulsación del botón
     */
    @FXML
    public void gestionSucursal(ActionEvent event) {
        Ventana.cargarEn(panelContenido, "/vista/GestionSucursal.fxml");
    }

    /**
     * Opción "Ver log": enseña el fichero de log en un área de texto de solo lectura.
     *
     * @param event pulsación del botón
     */
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

    /**
     * Opción "Cerrar sesión": vuelve al inicio de sesión si el usuario lo confirma.
     *
     * @param event pulsación del botón
     */
    @FXML
    public void cerrarSesion(ActionEvent event) {
        if (ShowAlert.confirmar("Salir", "¿Deseas salir del panel de administrador?")) {
            Sesion.cerrar();
            Ventana.cambiar(panelContenido, "/vista/IniciarSesion.fxml");
        }
    }
}
