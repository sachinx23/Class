package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Administrador;
import modelo.Contrato;
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
    private DAO dao = Sesion.getDao();

    // Controles de la pantalla. @FXML une cada variable con el control del FXML que tiene el mismo fx:id
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
                mostrarAvisos(usuario.getId());
                Ventana.cambiar(txtUsuario, "/vista/Cliente.fxml");
            }
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Avisa al cliente de los contratos que se han rescindido (baja aprobada o fecha de fin pasada)
     * desde la última vez que entró. Caso de uso "Rescindir un contrato" (notificación).
     *
     * @param idCliente id del cliente que acaba de iniciar sesión
     * @throws OurException si falla la base de datos
     */
    private void mostrarAvisos(String idCliente) throws OurException {
        List<Contrato> avisos = dao.obtenerAvisosCliente(idCliente);

        // Si no hay contratos rescindidos, no se enseña nada
        if (avisos.isEmpty()) {
            return;
        }

        // Se escribe una línea por cada contrato rescindido
        String texto = "Se han rescindido estos contratos:\n";
        for (Contrato c : avisos) {
            texto = texto + "\n- " + c.getNombreTarifa() + " (teléfono " + c.getTelefono() + ")";
        }
        ShowAlert.info("Aviso de contratos", texto);
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
