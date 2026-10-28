package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Cliente;

public class RegistrarseController {

    private final DAO dao = Sesion.getDao();

    @FXML private TextField txtUsuario;
    @FXML private TextField txtNombre;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private Label lblMensajeError;

    @FXML
    public void btnRegistrarse(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String nombre = txtNombre.getText().trim();
        String contrasena = txtContrasena.getText().trim();
        String correo = txtCorreo.getText().trim();
        String dni = txtDni.getText().trim().toUpperCase();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        if (usuario.isEmpty() || nombre.isEmpty() || contrasena.isEmpty() || correo.isEmpty()
                || dni.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            lblMensajeError.setText("Por favor, rellena todos los campos.");
            return;
        }
        if (!correo.contains("@") || !correo.contains(".")) {
            lblMensajeError.setText("El correo no es válido.");
            return;
        }
        if (!dni.matches("[0-9]{8}[A-Z]")) {
            lblMensajeError.setText("DNI introducido incorrectamente.");
            return;
        }
        if (!telefono.matches("[0-9 ]{9,15}")) {
            lblMensajeError.setText("El teléfono no es válido.");
            return;
        }
        if (contrasena.length() < 4) {
            lblMensajeError.setText("La contraseña debe tener mínimo 4 caracteres.");
            return;
        }
        lblMensajeError.setText("");

        Cliente cliente = new Cliente(null, nombre, usuario, contrasena, correo, "", dni, telefono, direccion);
        try {
            dao.insertarCliente(cliente);
            ShowAlert.info("Usuario registrado", "Tu cuenta ha sido creada correctamente.");
            Ventana.cambiar(txtUsuario, "/vista/IniciarSesion.fxml");
        } catch (OurException e) {
            lblMensajeError.setText(e.getMessage());
        }
    }

    @FXML
    public void btnSalir(ActionEvent event) {
        if (ShowAlert.confirmar("Cancelar registro", "¿Seguro que deseas cancelar el registro?")) {
            Ventana.cambiar(txtUsuario, "/vista/IniciarSesion.fxml");
        }
    }
}
