package exception;

/**
 * Mensajes de error que se muestran al usuario.
 * <p>
 * Están todos juntos para no repetir textos y poder cambiarlos en un solo sitio.
 */
public class ErrorMessages {

    /** Esta clase solo guarda constantes: no se crean objetos de ella. */
    private ErrorMessages() {
    }

    /** Mensaje: "No se pudo conectar con la base de datos. Comprueba que MongoDB está arrancado." */
    public static final String DATABASE = "No se pudo conectar con la base de datos. Comprueba que MongoDB está arrancado.";
    /** Mensaje: "Error al iniciar sesión. Inténtalo de nuevo." */
    public static final String LOGIN = "Error al iniciar sesión. Inténtalo de nuevo.";
    /** Mensaje: "No se pudo registrar el usuario. Comprueba los datos." */
    public static final String REGISTER_USER = "No se pudo registrar el usuario. Comprueba los datos.";
    /** Mensaje: "Ese nombre de usuario ya está en uso." */
    public static final String USER_EXISTS = "Ese nombre de usuario ya está en uso.";
    /** Mensaje: "El dni de usuario ya está registrado." */
    public static final String DNI_EXISTS = "El dni de usuario ya está registrado.";
    /** Mensaje: "No se pudieron guardar los cambios del usuario." */
    public static final String UPDATE_USER = "No se pudieron guardar los cambios del usuario.";
    /** Mensaje: "No se pudieron cargar las tarifas." */
    public static final String GET_TARIFAS = "No se pudieron cargar las tarifas.";
    /** Mensaje: "No se pudieron cargar los contratos." */
    public static final String GET_CONTRATOS = "No se pudieron cargar los contratos.";
    /** Mensaje: "No se pudo crear el contrato." */
    public static final String INSERT_CONTRATO = "No se pudo crear el contrato.";
    /** Mensaje: "No se pudo tramitar la baja del contrato." */
    public static final String BAJA_CONTRATO = "No se pudo tramitar la baja del contrato.";
    /** Mensaje: "No se pudo validar la tarifa." */
    public static final String VALIDAR_CONTRATO = "No se pudo validar la tarifa.";
    /** Mensaje: "No se pudieron cargar los tickets." */
    public static final String GET_TICKETS = "No se pudieron cargar los tickets.";
    /** Mensaje: "No se pudo guardar el ticket." */
    public static final String INSERT_TICKET = "No se pudo guardar el ticket.";
    /** Mensaje: "No se pudo cambiar el estado del ticket." */
    public static final String ESTADO_TICKET = "No se pudo cambiar el estado del ticket.";
    /** Mensaje: "No se pudo leer el fichero de log." */
    public static final String READ_LOG = "No se pudo leer el fichero de log.";
    /** Mensaje: "No se pudieron cargar los empleados." */
    public static final String GET_EMPLEADOS = "No se pudieron cargar los empleados.";
    /** Mensaje: "No se pudo dar de alta el empleado." */
    public static final String INSERT_EMPLEADO = "No se pudo dar de alta el empleado.";
    /** Mensaje: "No se pudo ascender al empleado." */
    public static final String ASCENDER_EMPLEADO = "No se pudo ascender al empleado.";
    /** Mensaje: "No se pudo borrar el empleado." */
    public static final String DELETE_EMPLEADO = "No se pudo borrar el empleado.";
    /** Mensaje: "No se pudieron cargar las sucursales." */
    public static final String GET_SUCURSALES = "No se pudieron cargar las sucursales.";
    /** Mensaje: "No se pudo guardar la sucursal." */
    public static final String SAVE_SUCURSAL = "No se pudo guardar la sucursal.";
    /** Mensaje: "La sucursal tiene empleados, no se puede eliminar." */
    public static final String SUCURSAL_CON_EMPLEADOS = "La sucursal tiene empleados, no se puede eliminar.";
    /** Mensaje: "No se pudieron cargar los clientes." */
    public static final String GET_CLIENTES = "No se pudieron cargar los clientes.";
    /** Mensaje: "No se pudieron eliminar los datos personales." */
    public static final String DATOS_SENSIBLES = "No se pudieron eliminar los datos personales.";
    /** Mensaje: "No se pudo trasladar al empleado." */
    public static final String TRASLADO_EMPLEADO = "No se pudo trasladar al empleado.";
    /** Mensaje: "No se pudieron cargar las valoraciones." */
    public static final String GET_VALORACIONES = "No se pudieron cargar las valoraciones.";
    /** Mensaje: "No se pudo guardar la valoración." */
    public static final String SAVE_VALORACION = "No se pudo guardar la valoración.";
}
