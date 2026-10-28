package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Administrador;

/**
 * Perfil del administrador (PerfilAdmin.fxml): ver y modificar sus datos.
 */
public class PerfilAdminController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public PerfilAdminController() {
    }

    /** Acceso a la base de datos. */
    private final DAO dao = Sesion.getDao();

    @FXML private TextField txtNombre;
    @FXML private TextField txtNombreUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtCuentaBancaria;

    /**
     * Se ejecuta al abrir la pantalla: rellena el formulario con los datos del administrador.
     */
    @FXML
    public void initialize() {
        Administrador admin = (Administrador) Sesion.getUsuario();
        txtNombre.setText(admin.getNombre());
        txtNombreUsuario.setText(admin.getNombreUsuario());
        txtContrasena.setText(admin.getContrasena());
        txtCorreo.setText(admin.getCorreo());
        txtCuentaBancaria.setText(admin.getCuentaBancaria());
    }

    /**
     * Botón "Modificar": valida y guarda los datos nuevos.
     *
     * @param event pulsación del botón
     */
    @FXML
    public void btnModificar(ActionEvent event) {
        String error = Validaciones.comprobarPerfil(txtNombre.getText(), txtNombreUsuario.getText(),
                txtContrasena.getText(), txtCorreo.getText());
        if (error != null) {
            ShowAlert.info("Datos inválidos", error);
            return;
        }
        if (!ShowAlert.confirmar("Confirmar cambios", "¿Seguro que deseas guardar los cambios?")) {
            return;
        }
        // Se crea un objeto nuevo con los cambios. Solo si se guarda bien pasa a ser el usuario de la sesión
        Administrador nuevo = new Administrador(Sesion.getUsuario().getId(), txtNombre.getText().trim(),
                txtNombreUsuario.getText().trim(), txtContrasena.getText().trim(), txtCorreo.getText().trim(),
                txtCuentaBancaria.getText().trim());
        try {
            dao.actualizarEmpleado(nuevo);
            Sesion.setUsuario(nuevo);
            ShowAlert.info("Modificación exitosa", "Tus datos se han actualizado correctamente.");
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }
}
