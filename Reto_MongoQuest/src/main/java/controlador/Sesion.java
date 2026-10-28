package controlador;

import dao.DAO;
import dao.DAOImplementacion;
import modelo.Usuario;

/**
 * Datos compartidos por todas las pantallas: el DAO y el usuario que ha iniciado sesión.
 * <p>
 * Todo es estático, así cualquier controlador puede usar {@code Sesion.getDao()} o
 * {@code Sesion.getUsuario()} sin tener que pasarse objetos entre pantallas.
 */
public class Sesion {

    /** Único objeto DAO de la aplicación. Es el único sitio donde aparece la clase DAOImplementacion. */
    private static final DAO dao = new DAOImplementacion();
    /** Usuario que ha iniciado sesión (null si nadie). */
    private static Usuario usuario;

    /** Esta clase solo tiene métodos estáticos: no se crean objetos de ella. */
    private Sesion() {
    }

    /**
     * Devuelve el DAO de la aplicación.
     *
     * @return el DAO para hablar con la base de datos
     */
    public static DAO getDao() {
        return dao;
    }

    /**
     * Devuelve el usuario que ha iniciado sesión.
     *
     * @return el usuario que ha iniciado sesión, o null si nadie
     */
    public static Usuario getUsuario() {
        return usuario;
    }

    /**
     * Guarda el usuario que ha iniciado sesión.
     *
     * @param nuevoUsuario usuario que ha iniciado sesión
     */
    public static void setUsuario(Usuario nuevoUsuario) {
        usuario = nuevoUsuario;
    }

    /**
     * Cierra la sesión (olvida el usuario).
     */
    public static void cerrar() {
        usuario = null;
    }
}
