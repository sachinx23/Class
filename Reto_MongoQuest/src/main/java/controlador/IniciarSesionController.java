package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Administrador;
import modelo.Empleado;
import modelo.Usuario;

/**
 * Pantalla de inicio de sesión (IniciarSesion.fxml).
 * <p>
 * Según el tipo de usuario abre el panel de administrador, de empleado o de cliente.
 */
public class IniciarSesionController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public IniciarSesionController() {
    }

    /** Acceso a la base de datos. */
    private final DAO dao = Sesion.getDao();

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    /** Etiqueta roja donde se escriben los errores del formulario. */
    @FXML private Label lblMensajeError;

    /**
     * Botón "Iniciar sesión": comprueba los datos y abre el panel que le toca al usuario.
     *
     * @param event pulsación del botón
     */
    @FXML
    public void btnIniciarSesion(ActionEvent event) {
        String nombreUsuario = txtUsuario.getText().trim();
        String contrasena = txtContrasena.getText().trim();

        if (nombreUsuario.isEmpty() || contrasena.isEmpty()) {
            lblMensajeError.setText("Por favor, rellena todos los campos.");
            return;
        }
        lblMensajeError.setText("");

        try {
            Usuario usuario = dao.login(nombreUsuario, contrasena);
            if (usuario == null) {
                lblMensajeError.setText("Usuario o contraseña incorrectos.");
                return;
            }
            Sesion.setUsuario(usuario);

            // Administrador va antes que Empleado porque un Administrador también es un Empleado
            if (usuario instanceof Administrador) {
                ShowAlert.info("Inicio de sesión", "¡Bienvenido/a " + usuario.getNombre() + "!\nRol: Administrador");
                Ventana.cambiar(txtUsuario, "/vista/Admin.fxml");
            } else if (usuario instanceof Empleado) {
                ShowAlert.info("Inicio de sesión", "¡Bienvenido/a " + usuario.getNombre() + "!\nRol: Empleado");
                Ventana.cambiar(txtUsuario, "/vista/Empleado.fxml");
            } else {
                ShowAlert.info("Inicio de sesión", "¡Bienvenido/a " + usuario.getNombre() + "!\nRol: Cliente");
                Ventana.cambiar(txtUsuario, "/vista/Cliente.fxml");
            }
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Registrarse": abre el formulario de registro de clientes.
     *
     * @param event pulsación del botón
     */
    @FXML
    public void btnRegistrarse(ActionEvent event) {
        Ventana.cambiar(txtUsuario, "/vista/Registrarse.fxml");
    }
}
