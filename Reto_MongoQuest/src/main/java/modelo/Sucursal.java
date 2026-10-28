package modelo;

/**
 * Tienda física de Tel-ONDA. Se guarda en la colección <b>Sucursales</b>.
 * <p>
 * La dirección, la localidad y el código postal se guardan embebidos en el campo "Ubicacion".
 */
public class Sucursal {

    /** Id de MongoDB (null hasta que se guarda). */
    private String id;

    /** Nombre de la sucursal. */
    private String nombre;

    /** Horario de atención. */
    private String horario;

    /** Teléfono de la tienda. */
    private String telefono;

    /** Calle y número. */
    private String direccion;

    /** Ciudad. */
    private String localidad;

    /** Código postal (0 si no se sabe). */
    private int codigoPostal;

    /**
     * Crea un objeto Sucursal.
     *
     * @param id id de MongoDB (null hasta que se guarda)
     * @param nombre nombre de la sucursal
     * @param horario horario de atención
     * @param telefono teléfono de la tienda
     * @param direccion calle y número
     * @param localidad ciudad
     * @param codigoPostal código postal (0 si no se sabe)
     */
    public Sucursal(String id, String nombre, String horario, String telefono, String direccion, String localidad, int codigoPostal) {
        this.id = id;
        this.nombre = nombre;
        this.horario = horario;
        this.telefono = telefono;
        this.direccion = direccion;
        this.localidad = localidad;
        this.codigoPostal = codigoPostal;
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
     * Devuelve: nombre de la sucursal.
     *
     * @return nombre de la sucursal
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Cambia: nombre de la sucursal.
     *
     * @param nombre nombre de la sucursal
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve: horario de atención.
     *
     * @return horario de atención
     */
    public String getHorario() {
        return horario;
    }

    /**
     * Cambia: horario de atención.
     *
     * @param horario horario de atención
     */
    public void setHorario(String horario) {
        this.horario = horario;
    }

    /**
     * Devuelve: teléfono de la tienda.
     *
     * @return teléfono de la tienda
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Cambia: teléfono de la tienda.
     *
     * @param telefono teléfono de la tienda
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve: calle y número.
     *
     * @return calle y número
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Cambia: calle y número.
     *
     * @param direccion calle y número
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve: ciudad.
     *
     * @return ciudad
     */
    public String getLocalidad() {
        return localidad;
    }

    /**
     * Cambia: ciudad.
     *
     * @param localidad ciudad
     */
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    /**
     * Devuelve: código postal (0 si no se sabe).
     *
     * @return código postal (0 si no se sabe)
     */
    public int getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Cambia: código postal (0 si no se sabe).
     *
     * @param codigoPostal código postal (0 si no se sabe)
     */
    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    /**
     * Texto que se ve en los ComboBox (por ejemplo, al elegir la sucursal de un empleado).
     *
     * @return el nombre de la sucursal
     */
    @Override
    public String toString() {
        return nombre;
    }

}
