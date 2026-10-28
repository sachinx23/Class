package exception;

/**
 * Excepción propia de la aplicación.
 * <p>
 * La lanza el DAO cuando algo sale mal. Su mensaje (uno de {@link ErrorMessages}) es el que
 * se le enseña al usuario en la pantalla, así el usuario nunca ve errores técnicos de MongoDB.
 */
public class OurException extends Exception {

    /**
     * Crea la excepción con el mensaje para el usuario.
     *
     * @param mensaje texto que se enseñará en pantalla
     */
    public OurException(String mensaje) {
        super(mensaje);
    }
}
