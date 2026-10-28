package controlador;

import dao.DAO;
import dao.DAOImplementacion;
import modelo.Usuario;

/**
 * Guarda el DAO y el usuario que ha iniciado sesión, para usarlos desde cualquier pantalla.
 */
public class Sesion {

    private static DAO dao = new DAOImplementacion();
    private static Usuario usuario;

    public static DAO getDao() {
        return dao;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario nuevoUsuario) {
        usuario = nuevoUsuario;
    }

    public static void cerrar() {
        usuario = null;
    }
}
