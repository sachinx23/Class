package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import modelo.Sucursal;

public class GestionSucursalController {

    private final DAO dao = Sesion.getDao();

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

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("direccion"));
        colLocalidad.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("localidad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("telefono"));
        colHorario.setCellValueFactory(new PropertyValueFactory<Sucursal, String>("horario"));
        cargarSucursales();
    }

    private void cargarSucursales() {
        try {
            tablaSucursales.setItems(FXCollections.observableArrayList(dao.obtenerSucursales()));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

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
        }
    }

    // Devuelve el mensaje de error, o null si el formulario es correcto
    private String validar() {
        if (txtNombre.getText().trim().isEmpty() || txtDireccion.getText().trim().isEmpty()
                || txtTelefono.getText().trim().isEmpty()) {
            return "Nombre, dirección y teléfono son obligatorios.";
        }
        if (!txtCodigoPostal.getText().trim().isEmpty() && !txtCodigoPostal.getText().trim().matches("[0-9]{5}")) {
            return "El código postal debe tener 5 números.";
        }
        return null;
    }

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

    @FXML
    public void btnLimpiar(ActionEvent event) {
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtLocalidad.clear();
        txtCodigoPostal.clear();
        txtHorario.clear();
        tablaSucursales.getSelectionModel().clearSelection();
    }
}
