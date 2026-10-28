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

public class IniciarSesionController {

    private final DAO dao = Sesion.getDao();

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Label lblMensajeError;

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

    @FXML
    public void btnRegistrarse(ActionEvent event) {
        Ventana.cambiar(txtUsuario, "/vista/Registrarse.fxml");
    }
}
