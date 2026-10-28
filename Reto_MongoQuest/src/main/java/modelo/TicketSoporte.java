package modelo;

/**
 * Incidencia que abre un cliente. Se guarda en la colección <b>TicketsSoportes</b>.
 * <p>
 * El estado puede ser: pendiente, en proceso, resuelto o cerrado (ver constantes TICKET_ en {@link dao.DAO}).
 */
public class TicketSoporte {

    /** Id de MongoDB (null hasta que se guarda). */
    private String id;

    /** Resumen corto del problema. */
    private String asunto;

    /** Explicación del problema. */
    private String descripcion;

    /** Fecha de creación (aaaa-mm-dd). */
    private String fechaCreacion;

    /** Estado del ticket. */
    private String estado;

    /** Id del cliente que lo abre. */
    private String idCliente;

    /** Id del empleado que lo gestiona (null si nadie). */
    private String idEmpleado;

    /** Nombre del cliente (se busca en Clientes al consultar, no se guarda). */
    private String nombreCliente;

    /**
     * Crea un objeto TicketSoporte.
     *
     * @param id id de MongoDB (null hasta que se guarda)
     * @param asunto resumen corto del problema
     * @param descripcion explicación del problema
     * @param fechaCreacion fecha de creación (aaaa-mm-dd)
     * @param estado estado del ticket
     * @param idCliente id del cliente que lo abre
     * @param idEmpleado id del empleado que lo gestiona (null si nadie)
     */
    public TicketSoporte(String id, String asunto, String descripcion, String fechaCreacion, String estado, String idCliente, String idEmpleado) {
        this.id = id;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
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
     * Devuelve: resumen corto del problema.
     *
     * @return resumen corto del problema
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Cambia: resumen corto del problema.
     *
     * @param asunto resumen corto del problema
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * Devuelve: explicación del problema.
     *
     * @return explicación del problema
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Cambia: explicación del problema.
     *
     * @param descripcion explicación del problema
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve: fecha de creación (aaaa-mm-dd).
     *
     * @return fecha de creación (aaaa-mm-dd)
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Cambia: fecha de creación (aaaa-mm-dd).
     *
     * @param fechaCreacion fecha de creación (aaaa-mm-dd)
     */
    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Devuelve: estado del ticket.
     *
     * @return estado del ticket
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Cambia: estado del ticket.
     *
     * @param estado estado del ticket
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve: id del cliente que lo abre.
     *
     * @return id del cliente que lo abre
     */
    public String getIdCliente() {
        return idCliente;
    }

    /**
     * Cambia: id del cliente que lo abre.
     *
     * @param idCliente id del cliente que lo abre
     */
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Devuelve: id del empleado que lo gestiona (null si nadie).
     *
     * @return id del empleado que lo gestiona (null si nadie)
     */
    public String getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * Cambia: id del empleado que lo gestiona (null si nadie).
     *
     * @param idEmpleado id del empleado que lo gestiona (null si nadie)
     */
    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * Devuelve: nombre del cliente (se busca en Clientes al consultar, no se guarda).
     *
     * @return nombre del cliente (se busca en Clientes al consultar, no se guarda)
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Cambia: nombre del cliente (se busca en Clientes al consultar, no se guarda).
     *
     * @param nombreCliente nombre del cliente (se busca en Clientes al consultar, no se guarda)
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
