package controlador;

/**
 * Comprobaciones de los formularios de empleados y administradores.
 */
public class ValidarPerfil {

    // Devuelve el mensaje de error, o null si los datos son correctos
    public static String comprobar(String nombre, String nombreUsuario, String contrasena, String correo) {
        if (nombre.trim().isEmpty() || nombreUsuario.trim().isEmpty()
                || contrasena.trim().isEmpty() || correo.trim().isEmpty()) {
            return "Por favor, rellena todos los campos.";
        }
        if (contrasena.trim().length() < 4) {
            return "La contraseña debe tener mínimo 4 caracteres.";
        }
        if (!correo.contains("@") || !correo.contains(".")) {
            return "El correo no es válido.";
        }
        return null;
    }
}
