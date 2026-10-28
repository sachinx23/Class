package modelo;

/**
 * Cliente de Tel-ONDA. Se guarda en la colección <b>Clientes</b>.
 */
public class Cliente extends Usuario {

    /** DNI (8 números y una letra). */
    private String dni;

    /** Teléfono de contacto. */
    private String telefono;

    /** Dirección postal. */
    private String direccion;

    /** True si el cliente se ha dado de baja (su perfil se conserva). */
    private boolean dadoDeBaja;

    /**
     * Crea un objeto Cliente.
     *
     * @param id id de MongoDB (null hasta que se guarda)
     * @param nombre nombre completo
     * @param nombreUsuario nombre para iniciar sesión
     * @param contrasena contraseña
     * @param correo correo electrónico
     * @param cuentaBancaria cuenta bancaria (IBAN)
     * @param dni DNI (8 números y una letra)
     * @param telefono teléfono de contacto
     * @param direccion dirección postal
     */
    public Cliente(String id, String nombre, String nombreUsuario, String contrasena, String correo, String cuentaBancaria, String dni, String telefono, String direccion) {
        super(id, nombre, nombreUsuario, contrasena, correo, cuentaBancaria);
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.dadoDeBaja = false;
    }

    /**
     * Devuelve: DNI (8 números y una letra).
     *
     * @return DNI (8 números y una letra)
     */
    public String getDni() {
        return dni;
    }

    /**
     * Cambia: DNI (8 números y una letra).
     *
     * @param dni DNI (8 números y una letra)
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Devuelve: teléfono de contacto.
     *
     * @return teléfono de contacto
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Cambia: teléfono de contacto.
     *
     * @param telefono teléfono de contacto
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve: dirección postal.
     *
     * @return dirección postal
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Cambia: dirección postal.
     *
     * @param direccion dirección postal
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve: true si el cliente se ha dado de baja (su perfil se conserva).
     *
     * @return true si el cliente se ha dado de baja (su perfil se conserva)
     */
    public boolean isDadoDeBaja() {
        return dadoDeBaja;
    }

    /**
     * Cambia: true si el cliente se ha dado de baja (su perfil se conserva).
     *
     * @param dadoDeBaja true si el cliente se ha dado de baja (su perfil se conserva)
     */
    public void setDadoDeBaja(boolean dadoDeBaja) {
        this.dadoDeBaja = dadoDeBaja;
    }
}
