package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import modelo.Empleado;
import modelo.Sucursal;

/**
 * Gestión de sucursales (GestionSucursal.fxml): crear, modificar y eliminar tiendas,
 * ver los empleados de cada una y trasladarlos a otra.
 * Casos de uso "Crear sucursal" y "Gestionar sucursal".
 */
public class GestionSucursalController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public GestionSucursalController() {
    }

    /** Acceso a la base de datos. */
    private DAO dao = Sesion.getDao();

    // Controles de la pantalla. @FXML une cada variable con el control del FXML que tiene el mismo fx:id
    @FXML private TableView<Sucursal> tablaSucursales;
    @FXML private TableColumn<Sucursal, String> colNombre;
    @FXML private TableColumn<Sucursal, String> colDireccion;
    @FXML private TableColumn<Sucursal, String> colLocalidad;
    @FXML private TableColumn<Sucursal, String> colTelefono;
    @FXML private TableColumn<Sucursal, String> colHorario;

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtLocalidad;
    @FXML private TextField txtCodigoPostal;
    @FXML private TextField txtHorario;

    /** Empleados de la sucursal seleccionada. */
    @FXML private TableView<Empleado> tablaPlantilla;
    @FXML private TableColumn<Empleado, String> colEmpleadoNombre;
    @FXML private TableColumn<Empleado, String> colEmpleadoCorreo;
    /** Sucursal a la que se traslada el empleado. */
    @FXML private ComboBox<Sucursal> cmbSucursalDestino;

    /**
     * Se ejecuta al abrir la pantalla: prepara las columnas y carga las sucursales.
     */
    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("direccion"));
        colLocalidad.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("localidad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("telefono"));
        colHorario.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("horario"));
        colEmpleadoNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombre"));
        colEmpleadoCorreo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("correo"));
        cargarSucursales();
    }

    /**
     * Carga las sucursales en la tabla.
     */
    private void cargarSucursales() {
        try {
            tablaSucursales.setItems(FXCollections.observableArrayList(dao.obtenerSucursales()));
            cmbSucursalDestino.getItems().setAll(tablaSucursales.getItems());
            tablaPlantilla.getItems().clear();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Clic en la tabla: copia los datos de la sucursal seleccionada al formulario.
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void seleccionarSucursal(MouseEvent event) {
        Sucursal s = tablaSucursales.getSelectionModel().getSelectedItem();
        if (s != null) {
            txtNombre.setText(s.getNombre());
            txtTelefono.setText(s.getTelefono());
            txtDireccion.setText(s.getDireccion());
            txtLocalidad.setText(s.getLocalidad());
            if (s.getCodigoPostal() == 0) {
                txtCodigoPostal.setText("");
            } else {
                txtCodigoPostal.setText(String.valueOf(s.getCodigoPostal()));
            }
            txtHorario.setText(s.getHorario());
            cargarPlantilla(s);
        }
    }

    /**
     * Carga en la tabla de plantilla los empleados de una sucursal.
     * Caso de uso "Gestionar sucursal" (consultar plantilla).
     *
     * @param s sucursal seleccionada
     */
    private void cargarPlantilla(Sucursal s) {
        try {
            tablaPlantilla.setItems(FXCollections.observableArrayList(dao.obtenerEmpleadosSucursal(s.getId())));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Trasladar": cambia el empleado seleccionado a la sucursal elegida en el desplegable.
     * Caso de uso "Gestionar sucursal" (traslado de empleados).
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnTrasladar(ActionEvent event) {
        Sucursal origen = tablaSucursales.getSelectionModel().getSelectedItem();
        Empleado empleado = tablaPlantilla.getSelectionModel().getSelectedItem();
        Sucursal destino = cmbSucursalDestino.getValue();
        if (origen == null || empleado == null) {
            ShowAlert.info("Aviso", "Selecciona una sucursal y después un empleado de su plantilla.");
            return;
        }
        if (destino == null) {
            ShowAlert.info("Aviso", "Elige la sucursal de destino en el desplegable.");
            return;
        }
        if (destino.getId().equals(origen.getId())) {
            ShowAlert.info("Aviso", "El empleado ya trabaja en esa sucursal.");
            return;
        }
        if (!ShowAlert.confirmar("Trasladar empleado", "¿Trasladar a " + empleado.getNombre() + " a " + destino.getNombre() + "?")) {
            return;
        }
        try {
            dao.trasladarEmpleado(empleado.getId(), destino.getId());
            ShowAlert.info("Traslado hecho", empleado.getNombre() + " ahora trabaja en " + destino.getNombre() + ".");
            cmbSucursalDestino.setValue(null);
            cargarPlantilla(origen);
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Comprueba el formulario.
     *
     * @return el mensaje de error, o null si el formulario es correcto
     */
    private String validar() {
        if (txtNombre.getText().trim().isEmpty() || txtDireccion.getText().trim().isEmpty()
                || txtTelefono.getText().trim().isEmpty()) {
            return "Nombre, dirección y teléfono son obligatorios.";
        }
        if (!txtCodigoPostal.getText().trim().isEmpty() && !Validaciones.codigoPostalValido(txtCodigoPostal.getText().trim())) {
            return "El código postal debe tener 5 números.";
        }
        return null;
    }

    /**
     * Copia los datos del formulario a la sucursal.
     *
     * @param s sucursal donde se copian los datos
     */
    private void leerFormulario(Sucursal s) {
        s.setNombre(txtNombre.getText().trim());
        s.setTelefono(txtTelefono.getText().trim());
        s.setDireccion(txtDireccion.getText().trim());
        s.setLocalidad(txtLocalidad.getText().trim());
        String cp = txtCodigoPostal.getText().trim();
        if (cp.isEmpty()) {
            s.setCodigoPostal(0);
        } else {
            s.setCodigoPostal(Integer.parseInt(cp));
        }
        s.setHorario(txtHorario.getText().trim());
    }

    /**
     * Botón "Crear": guarda una sucursal nueva con los datos del formulario.
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnCrear(ActionEvent event) {
        String error = validar();
        if (error != null) {
            ShowAlert.info("Campos incompletos", error);
            return;
        }
        if (ShowAlert.confirmar("Confirmar creación", "¿Seguro que deseas crear la sucursal " + txtNombre.getText().trim() + "?")) {
            Sucursal s = new Sucursal(null, "", "", "", "", "", 0);
            leerFormulario(s);
            try {
                dao.insertarSucursal(s);
                ShowAlert.info("Sucursal registrada", "La nueva sucursal ha sido dada de alta.");
                btnLimpiar(event);
                cargarSucursales();
            } catch (OurException e) {
                ShowAlert.error(e.getMessage());
            }
        }
    }

    /**
     * Botón "Modificar": guarda los cambios de la sucursal seleccionada.
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnModificar(ActionEvent event) {
        Sucursal s = tablaSucursales.getSelectionModel().getSelectedItem();
        if (s == null) {
            ShowAlert.info("Aviso", "Selecciona una sucursal de la tabla primero.");
            return;
        }
        String error = validar();
        if (error != null) {
            ShowAlert.info("Datos inválidos", error);
            return;
        }
        if (ShowAlert.confirmar("Confirmar modificación", "¿Seguro que deseas guardar los cambios de " + s.getNombre() + "?")) {
            leerFormulario(s);
            try {
                dao.actualizarSucursal(s);
                ShowAlert.info("Modificación exitosa", "La sucursal se ha actualizado.");
                cargarSucursales();
            } catch (OurException e) {
                ShowAlert.error(e.getMessage());
                cargarSucursales();
            }
        }
    }

    /**
     * Botón "Eliminar": borra la sucursal seleccionada (solo si no tiene empleados).
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnEliminar(ActionEvent event) {
        Sucursal s = tablaSucursales.getSelectionModel().getSelectedItem();
        if (s == null) {
            ShowAlert.info("Aviso", "Selecciona una sucursal de la tabla primero.");
            return;
        }
        if (ShowAlert.confirmar("Confirmar eliminación", "¿Seguro que deseas eliminar la sucursal " + s.getNombre() + "?")) {
            try {
                dao.borrarSucursal(s.getId());
                ShowAlert.info("Sucursal eliminada", "La sucursal ha sido eliminada.");
                btnLimpiar(event);
                cargarSucursales();
            } catch (OurException e) {
                ShowAlert.error(e.getMessage());
            }
        }
    }

    /**
     * Botón "Limpiar": vacía el formulario y quita la selección de la tabla.
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnLimpiar(ActionEvent event) {
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtLocalidad.clear();
        txtCodigoPostal.clear();
        txtHorario.clear();
        tablaSucursales.getSelectionModel().clearSelection();
        tablaPlantilla.getItems().clear();
    }
}
