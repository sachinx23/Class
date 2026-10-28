package modelo;

/**
 * Un administrador tiene los mismos datos que un empleado pero no pertenece a ninguna sucursal.
 */
public class Administrador extends Empleado {

    public Administrador(String id, String nombre, String nombreUsuario, String contrasena, String correo,
            String cuentaBancaria) {
        super(id, nombre, nombreUsuario, contrasena, correo, cuentaBancaria, null);
    }
}
