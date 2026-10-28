package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Administrador;

public class PerfilAdminController {

    private final DAO dao = Sesion.getDao();

    @FXML private TextField txtNombre;
    @FXML private TextField txtNombreUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtCuentaBancaria;

    private Administrador admin;

    @FXML
    public void initialize() {
        admin = (Administrador) Sesion.getUsuario();
        txtNombre.setText(admin.getNombre());
        txtNombreUsuario.setText(admin.getNombreUsuario());
        txtContrasena.setText(admin.getContrasena());
        txtCorreo.setText(admin.getCorreo());
        txtCuentaBancaria.setText(admin.getCuentaBancaria());
    }

    @FXML
    public void btnModificar(ActionEvent event) {
        String error = ValidarPerfil.comprobar(txtNombre.getText(), txtNombreUsuario.getText(),
                txtContrasena.getText(), txtCorreo.getText());
        if (error != null) {
            ShowAlert.info("Datos inválidos", error);
            return;
        }
        if (!ShowAlert.confirmar("Confirmar cambios", "¿Seguro que deseas guardar los cambios?")) {
            return;
        }
        admin.setNombre(txtNombre.getText().trim());
        admin.setNombreUsuario(txtNombreUsuario.getText().trim());
        admin.setContrasena(txtContrasena.getText().trim());
        admin.setCorreo(txtCorreo.getText().trim());
        admin.setCuentaBancaria(txtCuentaBancaria.getText().trim());
        try {
            dao.actualizarEmpleado(admin);
            ShowAlert.info("Modificación exitosa", "Tus datos se han actualizado correctamente.");
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }
}
