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
 * Borrar empleados (BorrarEmpleado.fxml). Caso de uso "Borrar cuenta (Empleado)".
 */
public class BorrarEmpleadoController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public BorrarEmpleadoController() {
    }

    /** Acceso a la base de datos. */
    private DAO dao = Sesion.getDao();

    // Controles de la pantalla. @FXML une cada variable con el control del FXML que tiene el mismo fx:id
    @FXML private TableView<Empleado> tablaEmpleados;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colNombreUsuario;
    @FXML private TableColumn<Empleado, String> colCorreo;

    /**
     * Se ejecuta al abrir la pantalla: prepara las columnas y carga los empleados.
     */
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombre"));
        colNombreUsuario.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreUsuario"));
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
     * Botón "Borrar": elimina al empleado seleccionado después de pedir confirmación.
     *
     * @param event pulsación del botón
     */
    @FXML
    public void btnBorrar(ActionEvent event) {
        Empleado empleado = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (empleado == null) {
            ShowAlert.info("Aviso", "Selecciona un empleado de la tabla primero.");
            return;
        }
        if (!ShowAlert.confirmar("Eliminar cuenta de empleado", "¿Seguro que quieres eliminar al empleado "
                + empleado.getNombre() + "? Perderá su acceso al sistema.")) {
            return;
        }
        try {
            dao.borrarEmpleado(empleado.getId());
            ShowAlert.info("Borrado exitoso", "El empleado ha sido eliminado.");
            cargarEmpleados();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }
}
