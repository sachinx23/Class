package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Empleado;

/**
 * Ascender empleados a administrador (CambiarPuesto.fxml). Caso de uso "Ascender".
 */
public class CambiarPuestoController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public CambiarPuestoController() {
    }

    /** Acceso a la base de datos. */
    private DAO dao = Sesion.getDao();

    // Controles de la pantalla. @FXML une cada variable con el control del FXML que tiene el mismo fx:id
    @FXML private TableView<Empleado> tablaEmpleados;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colCorreo;

    /**
     * Se ejecuta al abrir la pantalla: prepara las columnas y carga los empleados.
     */
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("correo"));
        cargarEmpleados();
    }

    /**
     * Carga los empleados en la tabla.
     */
    private void cargarEmpleados() {
        try {
            tablaEmpleados.setItems(FXCollections.observableArrayList(dao.obtenerEmpleados()));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Ascender": convierte al empleado seleccionado en administrador.
     *
     * @param event pulsación del botón
     */
    @FXML
    public void btnAscender(ActionEvent event) {
        Empleado empleado = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (empleado == null) {
            ShowAlert.info("Aviso", "Selecciona un empleado de la tabla primero.");
            return;
        }
        if (!ShowAlert.confirmar("Ascender empleado", "¿Estás seguro de ascender a " + empleado.getNombre()
                + "? Pasará a tener privilegios totales de Administrador.")) {
            return;
        }
        try {
            dao.ascenderEmpleado(empleado);
            ShowAlert.info("Cambio exitoso", empleado.getNombre() + " ahora es administrador.");
            cargarEmpleados();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }
}
