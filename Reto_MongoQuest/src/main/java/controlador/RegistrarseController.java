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

/**
 * Pantalla de registro de clientes nuevos (Registrarse.fxml). Caso de uso "Crear usuario".
 */
public class RegistrarseController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public RegistrarseController() {
    }

    /** Acceso a la base de datos. */
    private DAO dao = Sesion.getDao();

    // Controles de la pantalla. @FXML une cada variable con el control del FXML que tiene el mismo fx:id
    @FXML private TextField txtUsuario;
    @FXML private TextField txtNombre;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    /** Etiqueta roja donde se escriben los errores del formulario. */
    @FXML private Label lblMensajeError;

    /**
     * Botón "Registrarse": valida el formulario y guarda el cliente nuevo.
     *
     * @param event pulsación del botón
     */
    @FXML
    public void btnRegistrarse(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String nombre = txtNombre.getText().trim();
        String contrasena = txtContrasena.getText().trim();
        String correo = txtCorreo.getText().trim();
        String dni = txtDni.getText().trim().toUpperCase();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();

        String error = comprobar(usuario, nombre, contrasena, correo, dni, telefono, direccion);
        if (error != null) {
            lblMensajeError.setText(error);
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

    /**
     * Comprueba el formulario de registro.
     *
     * @return el mensaje de error, o null si todo es correcto
     */
    private String comprobar(String usuario, String nombre, String contrasena, String correo,
            String dni, String telefono, String direccion) {
        if (usuario.isEmpty() || nombre.isEmpty() || contrasena.isEmpty() || correo.isEmpty()
                || dni.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            return "Por favor, rellena todos los campos.";
        }
        if (!Validaciones.correoValido(correo)) {
            return "El correo no es válido.";
        }
        if (!Validaciones.dniValido(dni)) {
            return "DNI introducido incorrectamente.";
        }
        if (!Validaciones.telefonoValido(telefono)) {
            return "El teléfono no es válido.";
        }
        if (!Validaciones.contrasenaValida(contrasena)) {
            return "La contraseña debe tener mínimo " + Validaciones.MIN_CONTRASENA + " caracteres.";
        }
        return null;
    }

    /**
     * Botón "Salir": vuelve al inicio de sesión si el usuario lo confirma.
     *
     * @param event pulsación del botón
     */
    @FXML
    public void btnSalir(ActionEvent event) {
        if (ShowAlert.confirmar("Cancelar registro", "¿Seguro que deseas cancelar el registro?")) {
            Ventana.cambiar(txtUsuario, "/vista/IniciarSesion.fxml");
        }
    }
}
