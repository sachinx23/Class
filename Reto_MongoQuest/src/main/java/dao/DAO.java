package dao;

import exception.OurException;
import java.util.List;
import modelo.Cliente;
import modelo.Contrato;
import modelo.Empleado;
import modelo.Sucursal;
import modelo.Tarifa;
import modelo.TicketSoporte;
import modelo.Usuario;
import modelo.Valoracion;

/**
 * Todas las operaciones que la aplicación puede hacer con la base de datos.
 * <p>
 * Las pantallas (controladores) solo conocen esta interfaz, no saben que por debajo hay MongoDB.
 * Así, si mañana cambiamos de base de datos, solo hay que escribir otra clase que implemente DAO.
 * <p>
 * Todos los métodos lanzan {@link OurException} si algo sale mal. Su mensaje ya está preparado
 * para enseñárselo al usuario.
 */
public interface DAO {

    // ---------- Estados de un contrato ----------

    /** Contrato pedido por el cliente que todavía no ha validado ningún empleado. */
    String ESTADO_PENDIENTE = "pendiente";
    /** Contrato validado por un empleado: el cliente ya tiene la tarifa. */
    String ESTADO_ACTIVO = "activo";
    /** El cliente ha pedido la baja y falta que un empleado la apruebe. */
    String ESTADO_BAJA_PENDIENTE = "baja pendiente";
    /** Contrato terminado (baja aprobada). */
    String ESTADO_CANCELADO = "cancelado";

    // ---------- Estados de un ticket de soporte ----------

    /** Ticket recién creado por el cliente. */
    String TICKET_PENDIENTE = "pendiente";
    /** Un empleado está trabajando en el ticket. */
    String TICKET_EN_PROCESO = "en proceso";
    /** El problema está solucionado. */
    String TICKET_RESUELTO = "resuelto";
    /** El ticket se ha cerrado sin más cambios. */
    String TICKET_CERRADO = "cerrado";

    // ---------- Login y clientes ----------

    /**
     * Busca un usuario con ese nombre de usuario y contraseña en Administradores, Empleados y Clientes.
     *
     * @param nombreUsuario nombre para iniciar sesión
     * @param contrasena    contraseña
     * @return un {@link modelo.Administrador}, un {@link Empleado} o un {@link Cliente}; null si los datos no son correctos
     * @throws OurException si falla la base de datos
     */
    Usuario login(String nombreUsuario, String contrasena) throws OurException;

    /**
     * Da de alta un cliente nuevo. Al terminar, el cliente tiene el id que le ha puesto MongoDB.
     *
     * @param cliente cliente a guardar (sin id)
     * @throws OurException si el DNI o el nombre de usuario ya existen, o si falla la base de datos
     */
    void insertarCliente(Cliente cliente) throws OurException;

    /**
     * Guarda los datos personales modificados de un cliente.
     *
     * @param cliente cliente con los datos nuevos (y su id)
     * @throws OurException si el nombre de usuario o el DNI ya los usa otra persona, o si falla la base de datos
     */
    void actualizarCliente(Cliente cliente) throws OurException;

    // ---------- Tarifas ----------

    /**
     * Devuelve las tarifas que se pueden contratar (las que están activas).
     *
     * @return lista de tarifas activas
     * @throws OurException si falla la base de datos
     */
    List<Tarifa> obtenerTarifas() throws OurException;

    // ---------- Contratos ----------

    /**
     * Devuelve todos los contratos de un cliente.
     *
     * @param idCliente id del cliente
     * @return lista de contratos (puede estar vacía)
     * @throws OurException si falla la base de datos
     */
    List<Contrato> obtenerContratosCliente(String idCliente) throws OurException;

    /**
     * Devuelve los contratos que están en un estado concreto (por ejemplo, {@link #ESTADO_PENDIENTE}).
     *
     * @param estado uno de los estados ESTADO_...
     * @return lista de contratos en ese estado
     * @throws OurException si falla la base de datos
     */
    List<Contrato> obtenerContratosPorEstado(String estado) throws OurException;

    /**
     * Guarda un contrato nuevo. Los datos de la tarifa se guardan embebidos dentro del contrato.
     *
     * @param contrato contrato a guardar (sin id)
     * @throws OurException si falla la base de datos
     */
    void insertarContrato(Contrato contrato) throws OurException;

    /**
     * Borra la petición pendiente que tiene un cliente de una tarifa.
     *
     * @param idCliente id del cliente
     * @param idTarifa  id de la tarifa
     * @return true si se borró, false si no había ninguna petición pendiente
     * @throws OurException si falla la base de datos
     */
    boolean borrarPeticion(String idCliente, String idTarifa) throws OurException;

    /**
     * El cliente pide la baja de un contrato: pasa a {@link #ESTADO_BAJA_PENDIENTE}.
     *
     * @param idContrato id del contrato
     * @throws OurException si el contrato no existe o falla la base de datos
     */
    void solicitarBaja(String idContrato) throws OurException;

    /**
     * Un empleado valida un contrato pendiente: pasa a {@link #ESTADO_ACTIVO}.
     *
     * @param idContrato id del contrato
     * @param idEmpleado id del empleado que lo valida
     * @throws OurException si el contrato no existe o falla la base de datos
     */
    void validarContrato(String idContrato, String idEmpleado) throws OurException;

    /**
     * Devuelve los clientes que tienen al menos un contrato en {@link #ESTADO_BAJA_PENDIENTE}.
     *
     * @return lista de clientes (sin repetidos)
     * @throws OurException si falla la base de datos
     */
    List<Cliente> obtenerClientesConBajaPendiente() throws OurException;

    /**
     * Un empleado aprueba la baja: cancela los contratos en baja pendiente del cliente.
     * Si al cliente no le queda ningún contrato, se marca como dado de baja (su perfil se mantiene).
     *
     * @param idCliente  id del cliente
     * @param idEmpleado id del empleado que aprueba la baja
     * @throws OurException si falla la base de datos
     */
    void aprobarBaja(String idCliente, String idEmpleado) throws OurException;

    // ---------- Tickets de soporte ----------

    /**
     * Devuelve todos los tickets de un cliente.
     *
     * @param idCliente id del cliente
     * @return lista de tickets
     * @throws OurException si falla la base de datos
     */
    List<TicketSoporte> obtenerTicketsCliente(String idCliente) throws OurException;

    /**
     * Devuelve los tickets que todavía hay que atender ({@link #TICKET_PENDIENTE} o {@link #TICKET_EN_PROCESO}).
     *
     * @return lista de tickets sin terminar
     * @throws OurException si falla la base de datos
     */
    List<TicketSoporte> obtenerTicketsPendientes() throws OurException;

    /**
     * Guarda un ticket nuevo y añade su id al array "ticketSoporte" del cliente.
     *
     * @param ticket ticket a guardar (sin id)
     * @throws OurException si falla la base de datos
     */
    void insertarTicket(TicketSoporte ticket) throws OurException;

    /**
     * Un empleado cambia el estado de un ticket (en proceso, resuelto o cerrado).
     *
     * @param idTicket   id del ticket
     * @param estado     uno de los estados TICKET_...
     * @param idEmpleado id del empleado que lo gestiona
     * @throws OurException si el ticket no existe o falla la base de datos
     */
    void cambiarEstadoTicket(String idTicket, String estado, String idEmpleado) throws OurException;

    // ---------- Empleados y administradores ----------

    /**
     * Guarda los datos modificados de un empleado o de un administrador.
     *
     * @param empleado empleado (o administrador) con los datos nuevos
     * @throws OurException si el nombre de usuario ya existe o falla la base de datos
     */
    void actualizarEmpleado(Empleado empleado) throws OurException;

    /**
     * Devuelve todos los empleados (sin los administradores).
     *
     * @return lista de empleados
     * @throws OurException si falla la base de datos
     */
    List<Empleado> obtenerEmpleados() throws OurException;

    /**
     * Da de alta un empleado nuevo.
     *
     * @param empleado empleado a guardar (sin id)
     * @throws OurException si el nombre de usuario ya existe o falla la base de datos
     */
    void insertarEmpleado(Empleado empleado) throws OurException;

    /**
     * Convierte un empleado en administrador: lo pasa de la colección Empleados a Administradores.
     *
     * @param empleado empleado a ascender
     * @throws OurException si falla la base de datos (en ese caso no se cambia nada)
     */
    void ascenderEmpleado(Empleado empleado) throws OurException;

    /**
     * Borra un empleado.
     *
     * @param idEmpleado id del empleado
     * @throws OurException si el empleado no existe o falla la base de datos
     */
    void borrarEmpleado(String idEmpleado) throws OurException;

    // ---------- Sucursales ----------

    /**
     * Devuelve todas las sucursales.
     *
     * @return lista de sucursales
     * @throws OurException si falla la base de datos
     */
    List<Sucursal> obtenerSucursales() throws OurException;

    /**
     * Guarda una sucursal nueva. La dirección se guarda embebida en el campo "Ubicacion".
     *
     * @param sucursal sucursal a guardar (sin id)
     * @throws OurException si falla la base de datos
     */
    void insertarSucursal(Sucursal sucursal) throws OurException;

    /**
     * Guarda los datos modificados de una sucursal.
     *
     * @param sucursal sucursal con los datos nuevos
     * @throws OurException si la sucursal no existe o falla la base de datos
     */
    void actualizarSucursal(Sucursal sucursal) throws OurException;

    /**
     * Borra una sucursal. No se puede borrar si todavía tiene empleados.
     *
     * @param idSucursal id de la sucursal
     * @throws OurException si tiene empleados, no existe o falla la base de datos
     */
    void borrarSucursal(String idSucursal) throws OurException;

    // ---------- Valoraciones ----------

    /**
     * Devuelve las valoraciones escritas por un cliente.
     *
     * @param idCliente id del cliente
     * @return lista de valoraciones
     * @throws OurException si falla la base de datos
     */
    List<Valoracion> obtenerValoracionesCliente(String idCliente) throws OurException;

    /**
     * Guarda una valoración nueva.
     *
     * @param valoracion valoración a guardar (sin id)
     * @throws OurException si falla la base de datos
     */
    void insertarValoracion(Valoracion valoracion) throws OurException;

    /**
     * Guarda la puntuación y el texto modificados de una valoración.
     *
     * @param valoracion valoración con los datos nuevos
     * @throws OurException si no existe o falla la base de datos
     */
    void actualizarValoracion(Valoracion valoracion) throws OurException;

    /**
     * Borra una valoración.
     *
     * @param idValoracion id de la valoración
     * @throws OurException si no existe o falla la base de datos
     */
    void borrarValoracion(String idValoracion) throws OurException;
}
