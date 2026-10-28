package modelo;

/**
 * Contrato de una tarifa por parte de un cliente. Se guarda en la colección <b>Contratos</b>.
 * <p>
 * El estado puede ser: pendiente, activo, baja pendiente o cancelado (ver constantes ESTADO_ en {@link dao.DAO}).
 */
public class Contrato {

    /** Id de MongoDB (null hasta que se guarda). */
    private String id;

    /** Teléfono asociado a la línea. */
    private String telefono;

    /** Estado del contrato. */
    private String estado;

    /** Id del cliente. */
    private String idCliente;

    /** Id de la tarifa. */
    private String idTarifa;

    /** Id del empleado que lo validó (null si nadie). */
    private String idEmpleado;

    /** Fecha de inicio (aaaa-mm-dd). */
    private String fechaComienzo;

    /** Fecha de fin (aaaa-mm-dd). */
    private String fechaFinalizacion;

    /** Nombre de la tarifa (embebido en el contrato). */
    private String nombreTarifa;

    /** Precio de la tarifa en euros (embebido en el contrato). */
    private double precio;

    /** Nombre del cliente (se busca en Clientes al consultar, no se guarda). */
    private String nombreCliente;

    /** DNI del cliente (se busca en Clientes al consultar, no se guarda). */
    private String dniCliente;

    /**
     * Crea un objeto Contrato.
     *
     * @param id id de MongoDB (null hasta que se guarda)
     * @param telefono teléfono asociado a la línea
     * @param estado estado del contrato
     * @param idCliente id del cliente
     * @param idTarifa id de la tarifa
     * @param idEmpleado id del empleado que lo validó (null si nadie)
     * @param fechaComienzo fecha de inicio (aaaa-mm-dd)
     * @param fechaFinalizacion fecha de fin (aaaa-mm-dd)
     */
    public Contrato(String id, String telefono, String estado, String idCliente, String idTarifa, String idEmpleado, String fechaComienzo, String fechaFinalizacion) {
        this.id = id;
        this.telefono = telefono;
        this.estado = estado;
        this.idCliente = idCliente;
        this.idTarifa = idTarifa;
        this.idEmpleado = idEmpleado;
        this.fechaComienzo = fechaComienzo;
        this.fechaFinalizacion = fechaFinalizacion;
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
     * Devuelve: teléfono asociado a la línea.
     *
     * @return teléfono asociado a la línea
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Cambia: teléfono asociado a la línea.
     *
     * @param telefono teléfono asociado a la línea
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Devuelve: estado del contrato.
     *
     * @return estado del contrato
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Cambia: estado del contrato.
     *
     * @param estado estado del contrato
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve: id del cliente.
     *
     * @return id del cliente
     */
    public String getIdCliente() {
        return idCliente;
    }

    /**
     * Cambia: id del cliente.
     *
     * @param idCliente id del cliente
     */
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Devuelve: id de la tarifa.
     *
     * @return id de la tarifa
     */
    public String getIdTarifa() {
        return idTarifa;
    }

    /**
     * Cambia: id de la tarifa.
     *
     * @param idTarifa id de la tarifa
     */
    public void setIdTarifa(String idTarifa) {
        this.idTarifa = idTarifa;
    }

    /**
     * Devuelve: id del empleado que lo validó (null si nadie).
     *
     * @return id del empleado que lo validó (null si nadie)
     */
    public String getIdEmpleado() {
        return idEmpleado;
    }

    /**
     * Cambia: id del empleado que lo validó (null si nadie).
     *
     * @param idEmpleado id del empleado que lo validó (null si nadie)
     */
    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /**
     * Devuelve: fecha de inicio (aaaa-mm-dd).
     *
     * @return fecha de inicio (aaaa-mm-dd)
     */
    public String getFechaComienzo() {
        return fechaComienzo;
    }

    /**
     * Cambia: fecha de inicio (aaaa-mm-dd).
     *
     * @param fechaComienzo fecha de inicio (aaaa-mm-dd)
     */
    public void setFechaComienzo(String fechaComienzo) {
        this.fechaComienzo = fechaComienzo;
    }

    /**
     * Devuelve: fecha de fin (aaaa-mm-dd).
     *
     * @return fecha de fin (aaaa-mm-dd)
     */
    public String getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    /**
     * Cambia: fecha de fin (aaaa-mm-dd).
     *
     * @param fechaFinalizacion fecha de fin (aaaa-mm-dd)
     */
    public void setFechaFinalizacion(String fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    /**
     * Devuelve: nombre de la tarifa (embebido en el contrato).
     *
     * @return nombre de la tarifa (embebido en el contrato)
     */
    public String getNombreTarifa() {
        return nombreTarifa;
    }

    /**
     * Cambia: nombre de la tarifa (embebido en el contrato).
     *
     * @param nombreTarifa nombre de la tarifa (embebido en el contrato)
     */
    public void setNombreTarifa(String nombreTarifa) {
        this.nombreTarifa = nombreTarifa;
    }

    /**
     * Devuelve: precio de la tarifa en euros (embebido en el contrato).
     *
     * @return precio de la tarifa en euros (embebido en el contrato)
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Cambia: precio de la tarifa en euros (embebido en el contrato).
     *
     * @param precio precio de la tarifa en euros (embebido en el contrato)
     */
    public void setPrecio(double precio) {
        this.precio = precio;
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

    /**
     * Devuelve: DNI del cliente (se busca en Clientes al consultar, no se guarda).
     *
     * @return DNI del cliente (se busca en Clientes al consultar, no se guarda)
     */
    public String getDniCliente() {
        return dniCliente;
    }

    /**
     * Cambia: DNI del cliente (se busca en Clientes al consultar, no se guarda).
     *
     * @param dniCliente DNI del cliente (se busca en Clientes al consultar, no se guarda)
     */
    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }
}
