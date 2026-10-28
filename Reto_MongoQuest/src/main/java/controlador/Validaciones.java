package controlador;

/**
 * Comprobaciones de los datos que escribe el usuario en los formularios.
 * <p>
 * Todas las pantallas usan estos métodos, así cada regla (DNI, correo...) está escrita en un único sitio.
 * Los métodos devuelven {@code true} si el dato es correcto.
 */
public class Validaciones {

    /**
     * Esta clase solo tiene métodos estáticos: no se crean objetos de ella.
     */
    private Validaciones() {
    }

    /** Longitud mínima de una contraseña (la misma que pide el esquema de MongoDB). */
    public static final int MIN_CONTRASENA = 4;

    /**
     * Comprueba si un texto está vacío o solo tiene espacios.
     *
     * @param texto texto a comprobar (puede ser null)
     * @return true si es null, vacío o solo espacios
     */
    public static boolean vacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /**
     * Comprueba un DNI: 8 números y una letra mayúscula (por ejemplo 12345678F).
     *
     * @param dni DNI a comprobar
     * @return true si el formato es correcto
     */
    public static boolean dniValido(String dni) {
        return dni != null && dni.matches("[0-9]{8}[A-Z]");
    }

    /**
     * Comprueba un correo: algo, una arroba, algo, un punto y algo (por ejemplo ana@gmail.com).
     *
     * @param correo correo a comprobar
     * @return true si el formato es correcto
     */
    public static boolean correoValido(String correo) {
        return correo != null && correo.matches(".+@.+\\..+");
    }

    /**
     * Comprueba un teléfono: entre 9 y 15 caracteres, solo números y espacios (por ejemplo 666 666 666).
     *
     * @param telefono teléfono a comprobar
     * @return true si el formato es correcto
     */
    public static boolean telefonoValido(String telefono) {
        return telefono != null && telefono.matches("[0-9 ]{9,15}");
    }

    /**
     * Comprueba que la contraseña tiene al menos {@link #MIN_CONTRASENA} caracteres.
     *
     * @param contrasena contraseña a comprobar
     * @return true si es suficientemente larga
     */
    public static boolean contrasenaValida(String contrasena) {
        return contrasena != null && contrasena.trim().length() >= MIN_CONTRASENA;
    }

    /**
     * Comprueba un código postal: exactamente 5 números (por ejemplo 48480).
     *
     * @param codigoPostal código postal a comprobar
     * @return true si el formato es correcto
     */
    public static boolean codigoPostalValido(String codigoPostal) {
        return codigoPostal != null && codigoPostal.matches("[0-9]{5}");
    }

    /**
     * Comprueba el formulario de perfil de un empleado o administrador.
     *
     * @param nombre        nombre completo
     * @param nombreUsuario nombre para iniciar sesión
     * @param contrasena    contraseña
     * @param correo        correo electrónico
     * @return el mensaje de error que hay que enseñar, o null si todo es correcto
     */
    public static String comprobarPerfil(String nombre, String nombreUsuario, String contrasena, String correo) {
        if (vacio(nombre) || vacio(nombreUsuario) || vacio(contrasena) || vacio(correo)) {
            return "Por favor, rellena todos los campos.";
        }
        if (!contrasenaValida(contrasena)) {
            return "La contraseña debe tener mínimo " + MIN_CONTRASENA + " caracteres.";
        }
        if (!correoValido(correo.trim())) {
            return "El correo no es válido.";
        }
        return null;
    }
}
