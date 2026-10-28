package modelo;

/**
 * Empleado de una sucursal. Se guarda en la colección <b>Empleados</b>.
 */
public class Empleado extends Usuario {

    /** Id de la sucursal donde trabaja (null si no tiene). */
    private String idSucursal;

    /**
     * Crea un objeto Empleado.
     *
     * @param id id de MongoDB (null hasta que se guarda)
     * @param nombre nombre completo
     * @param nombreUsuario nombre para iniciar sesión
     * @param contrasena contraseña
     * @param correo correo electrónico
     * @param cuentaBancaria cuenta bancaria (IBAN)
     * @param idSucursal id de la sucursal donde trabaja (null si no tiene)
     */
    public Empleado(String id, String nombre, String nombreUsuario, String contrasena, String correo, String cuentaBancaria, String idSucursal) {
        super(id, nombre, nombreUsuario, contrasena, correo, cuentaBancaria);
        this.idSucursal = idSucursal;
    }

    /**
     * Devuelve: id de la sucursal donde trabaja (null si no tiene).
     *
     * @return id de la sucursal donde trabaja (null si no tiene)
     */
    public String getIdSucursal() {
        return idSucursal;
    }

    /**
     * Cambia: id de la sucursal donde trabaja (null si no tiene).
     *
     * @param idSucursal id de la sucursal donde trabaja (null si no tiene)
     */
    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }
}
