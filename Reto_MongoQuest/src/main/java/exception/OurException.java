package exception;

/**
 * Excepción propia de la aplicación. Su mensaje es el que se le enseña al usuario.
 */
public class OurException extends Exception {

    public OurException(String mensaje) {
        super(mensaje);
    }
}
