package modelo;

/**
 * Datos comunes de todas las personas que pueden iniciar sesión.
 * <p>
 * Es abstracta porque nunca se crea un "Usuario" a secas: siempre es un
 * {@link Cliente}, un {@link Empleado} o un {@link Administrador}.
 */
public abstract class Usuario {

    /** Id de MongoDB (null hasta que se guarda). */
    private String id;

    /** Nombre completo. */
    private String nombre;

    /** Nombre para iniciar sesión. */
    private String nombreUsuario;

    /** Contraseña. */
    private String contrasena;

    /** Correo electrónico. */
    private String correo;

    /** Cuenta bancaria (IBAN). */
    private String cuentaBancaria;

    /**
     * Crea un objeto Usuario.
     *
     * @param id id de MongoDB (null hasta que se guarda)
     * @param nombre nombre completo
     * @param nombreUsuario nombre para iniciar sesión
     * @param contrasena contraseña
     * @param correo correo electrónico
     * @param cuentaBancaria cuenta bancaria (IBAN)
     */
    public Usuario(String id, String nombre, String nombreUsuario, String contrasena, String correo, String cuentaBancaria) {
        this.id = id;
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.correo = correo;
        this.cuentaBancaria = cuentaBancaria;
    }

    /**
     * Devuelve: id de MongoDB (null hasta que se guarda).
     *
     * @return id de MongoDB (null hasta que se guarda)
     */
    public String getId() {
        return id;
    }

    /**
     * Cambia: id de MongoDB (null hasta que se guarda).
     *
     * @param id id de MongoDB (null hasta que se guarda)
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve: nombre completo.
     *
     * @return nombre completo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia: nombre completo.
     *
     * @param nombre nombre completo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve: nombre para iniciar sesión.
     *
     * @return nombre para iniciar sesión
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Cambia: nombre para iniciar sesión.
     *
     * @param nombreUsuario nombre para iniciar sesión
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Devuelve: contraseña.
     *
     * @return contraseña
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Cambia: contraseña.
     *
     * @param contrasena contraseña
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Devuelve: correo electrónico.
     *
     * @return correo electrónico
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Cambia: correo electrónico.
     *
     * @param correo correo electrónico
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Devuelve: cuenta bancaria (IBAN).
     *
     * @return cuenta bancaria (IBAN)
     */
    public String getCuentaBancaria() {
        return cuentaBancaria;
    }

    /**
     * Cambia: cuenta bancaria (IBAN).
     *
     * @param cuentaBancaria cuenta bancaria (IBAN)
     */
    public void setCuentaBancaria(String cuentaBancaria) {
        this.cuentaBancaria = cuentaBancaria;
    }
}
