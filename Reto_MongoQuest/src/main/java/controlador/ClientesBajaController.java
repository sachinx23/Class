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
import modelo.Cliente;

/**
 * Clientes dados de baja (ClientesBaja.fxml): el administrador borra sus datos personales
 * cuando el cliente se lo pide. Caso de uso "Eliminar datos sensibles del cliente".
 */
public class ClientesBajaController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public ClientesBajaController() {
    }

    /** Acceso a la base de datos. */
    private final DAO dao = Sesion.getDao();

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colNombreUsuario;
    @FXML private TableColumn<Cliente, String> colDni;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colDireccion;

    /**
     * Se ejecuta al abrir la pantalla: prepara las columnas y carga los clientes dados de baja.
     */
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
        colNombreUsuario.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombreUsuario"));
        colDni.setCellValueFactory(new PropertyValueFactory<Cliente, String>("dni"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccion"));
        cargarClientes();
    }

    /**
     * Carga en la tabla los clientes dados de baja.
     */
    private void cargarClientes() {
        try {
            tablaClientes.setItems(FXCollections.observableArrayList(dao.obtenerClientesDadosDeBaja()));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Eliminar datos": borra los datos personales del cliente seleccionado.
     *
     * @param event pulsación del botón
     */
    @FXML
    public void btnEliminarDatos(ActionEvent event) {
        Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();
        if (cliente == null) {
            ShowAlert.info("Aviso", "Selecciona un cliente de la tabla primero.");
            return;
        }
        if (!ShowAlert.confirmar("Eliminar datos personales", "¿Borrar el DNI, teléfono, dirección y cuenta bancaria de "
                + cliente.getNombre() + "? No se puede deshacer.")) {
            return;
        }
        try {
            dao.eliminarDatosSensibles(cliente.getId());
            ShowAlert.info("Datos eliminados", "Los datos personales de " + cliente.getNombre() + " se han eliminado.");
            cargarClientes();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }
}
