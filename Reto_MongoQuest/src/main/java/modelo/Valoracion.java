package modelo;

/**
 * Opinión de un cliente sobre el servicio. Se guarda en la colección <b>Valoraciones</b>.
 */
public class Valoracion {

    /** Id de MongoDB (null hasta que se guarda). */
    private String id;

    /** Puntuación de 1 a 5. */
    private int puntuacion;

    /** Opinión escrita. */
    private String texto;

    /** Id del cliente que la escribe. */
    private String idCliente;

    /** Id de la tarifa valorada (null si es una opinión general). */
    private String idTarifa;

    /**
     * Crea un objeto Valoracion.
     *
     * @param id id de MongoDB (null hasta que se guarda)
     * @param puntuacion puntuación de 1 a 5
     * @param texto opinión escrita
     * @param idCliente id del cliente que la escribe
     * @param idTarifa id de la tarifa valorada (null si es una opinión general)
     */
    public Valoracion(String id, int puntuacion, String texto, String idCliente, String idTarifa) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.texto = texto;
        this.idCliente = idCliente;
        this.idTarifa = idTarifa;
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
     * Devuelve: puntuación de 1 a 5.
     *
     * @return puntuación de 1 a 5
     */
    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Cambia: puntuación de 1 a 5.
     *
     * @param puntuacion puntuación de 1 a 5
     */
    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    /**
     * Devuelve: opinión escrita.
     *
     * @return opinión escrita
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Cambia: opinión escrita.
     *
     * @param texto opinión escrita
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Devuelve: id del cliente que la escribe.
     *
     * @return id del cliente que la escribe
     */
    public String getIdCliente() {
        return idCliente;
    }

    /**
     * Cambia: id del cliente que la escribe.
     *
     * @param idCliente id del cliente que la escribe
     */
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Devuelve: id de la tarifa valorada (null si es una opinión general).
     *
     * @return id de la tarifa valorada (null si es una opinión general)
     */
    public String getIdTarifa() {
        return idTarifa;
    }

    /**
     * Cambia: id de la tarifa valorada (null si es una opinión general).
     *
     * @param idTarifa id de la tarifa valorada (null si es una opinión general)
     */
    public void setIdTarifa(String idTarifa) {
        this.idTarifa = idTarifa;
    }
}
