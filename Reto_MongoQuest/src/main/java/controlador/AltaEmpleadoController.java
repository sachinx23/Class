package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Empleado;
import modelo.Sucursal;

public class AltaEmpleadoController {

    private final DAO dao = Sesion.getDao();

    @FXML private TextField txtNombre;
    @FXML private TextField txtNombreUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtCuentaBancaria;
    @FXML private ComboBox<Sucursal> cmbSucursal;

    @FXML
    public void initialize() {
        try {
            cmbSucursal.getItems().setAll(dao.obtenerSucursales());
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    @FXML
    public void btnDarDeAlta(ActionEvent event) {
        String error = ValidarPerfil.comprobar(txtNombre.getText(), txtNombreUsuario.getText(),
                txtContrasena.getText(), txtCorreo.getText());
        if (error != null) {
            ShowAlert.info("Campos incompletos", error);
            return;
        }
        if (!ShowAlert.confirmar("Confirmar alta", "¿Seguro que deseas dar de alta a " + txtNombre.getText().trim() + "?")) {
            return;
        }
        String idSucursal = null;
        if (cmbSucursal.getValue() != null) {
            idSucursal = cmbSucursal.getValue().getId();
        }
        Empleado empleado = new Empleado(null, txtNombre.getText().trim(), txtNombreUsuario.getText().trim(),
                txtContrasena.getText().trim(), txtCorreo.getText().trim(), txtCuentaBancaria.getText().trim(), idSucursal);
        try {
            dao.insertarEmpleado(empleado);
            ShowAlert.info("Empleado registrado", "La cuenta del nuevo trabajador ha sido generada en el sistema.");
            txtNombre.clear();
            txtNombreUsuario.clear();
            txtContrasena.clear();
            txtCorreo.clear();
            txtCuentaBancaria.clear();
            cmbSucursal.setValue(null);
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }
}
