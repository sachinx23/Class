package modelo;

/**
 * Tarifa que se puede contratar. Se guarda en la colección <b>Tarifas</b>.
 */
public class Tarifa {

    /** Id de MongoDB. */
    private String id;

    /** Nombre comercial de la tarifa. */
    private String nombreTarifa;

    /** Precio mensual en euros. */
    private double precio;

    /** Descripción de lo que incluye. */
    private String detalles;

    /** Tipo: Móvil, Fibra o Fibra + Móvil. */
    private String tipoTarifa;

    /** True si se puede contratar. */
    private boolean activa;

    /**
     * Crea un objeto Tarifa.
     *
     * @param id id de MongoDB
     * @param nombreTarifa nombre comercial de la tarifa
     * @param precio precio mensual en euros
     * @param detalles descripción de lo que incluye
     * @param tipoTarifa tipo: Móvil, Fibra o Fibra + Móvil
     * @param activa true si se puede contratar
     */
    public Tarifa(String id, String nombreTarifa, double precio, String detalles, String tipoTarifa, boolean activa) {
        this.id = id;
        this.nombreTarifa = nombreTarifa;
        this.precio = precio;
        this.detalles = detalles;
        this.tipoTarifa = tipoTarifa;
        this.activa = activa;
    }

    /**
     * Devuelve: id de MongoDB.
     *
     * @return id de MongoDB
     */
    public String getId() {
        return id;
    }

    /**
     * Cambia: id de MongoDB.
     *
     * @param id id de MongoDB
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve: nombre comercial de la tarifa.
     *
     * @return nombre comercial de la tarifa
     */
    public String getNombreTarifa() {
        return nombreTarifa;
    }

    /**
     * Cambia: nombre comercial de la tarifa.
     *
     * @param nombreTarifa nombre comercial de la tarifa
     */
    public void setNombreTarifa(String nombreTarifa) {
        this.nombreTarifa = nombreTarifa;
    }

    /**
     * Devuelve: precio mensual en euros.
     *
     * @return precio mensual en euros
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Cambia: precio mensual en euros.
     *
     * @param precio precio mensual en euros
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Devuelve: descripción de lo que incluye.
     *
     * @return descripción de lo que incluye
     */
    public String getDetalles() {
        return detalles;
    }

    /**
     * Cambia: descripción de lo que incluye.
     *
     * @param detalles descripción de lo que incluye
     */
    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    /**
     * Devuelve: tipo: Móvil, Fibra o Fibra + Móvil.
     *
     * @return tipo: Móvil, Fibra o Fibra + Móvil
     */
    public String getTipoTarifa() {
        return tipoTarifa;
    }

    /**
     * Cambia: tipo: Móvil, Fibra o Fibra + Móvil.
     *
     * @param tipoTarifa tipo: Móvil, Fibra o Fibra + Móvil
     */
    public void setTipoTarifa(String tipoTarifa) {
        this.tipoTarifa = tipoTarifa;
    }

    /**
     * Devuelve: true si se puede contratar.
     *
     * @return true si se puede contratar
     */
    public boolean isActiva() {
        return activa;
    }

    /**
     * Cambia: true si se puede contratar.
     *
     * @param activa true si se puede contratar
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
