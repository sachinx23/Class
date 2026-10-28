package modelo;

/**
 * Administrador de Tel-ONDA. Se guarda en la colección <b>Administradores</b>.
 * <p>
 * Tiene los mismos datos que un {@link Empleado}, pero no pertenece a ninguna sucursal.
 */
public class Administrador extends Empleado {

    /**
     * Crea un administrador.
     *
     * @param id             id de MongoDB
     * @param nombre         nombre completo
     * @param nombreUsuario  nombre para iniciar sesión
     * @param contrasena     contraseña
     * @param correo         correo electrónico
     * @param cuentaBancaria cuenta bancaria (IBAN)
     */
    public Administrador(String id, String nombre, String nombreUsuario, String contrasena, String correo,
            String cuentaBancaria) {
        super(id, nombre, nombreUsuario, contrasena, correo, cuentaBancaria, null);
    }
}
