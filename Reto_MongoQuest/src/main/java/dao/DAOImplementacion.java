package dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import exception.ErrorMessages;
import exception.OurException;
import java.util.ArrayList;
import java.util.List;
import log.LogApp;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Contrato;
import modelo.Empleado;
import modelo.Sucursal;
import modelo.Tarifa;
import modelo.TicketSoporte;
import modelo.Usuario;
import modelo.Valoracion;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Implementación del {@link DAO} con MongoDB.
 * <p>
 * Cómo se trabaja con MongoDB en esta clase:
 * <ul>
 *   <li>Un {@link Document} es como un HashMap: se le meten campos con put() y se leen con get().</li>
 *   <li>Se usa un Document para guardar datos, para buscar (el "filtro") y para modificar (los "cambios").</li>
 *   <li>Cada método escribe en el log lo que ha hecho, y si falla MongoDB lanza una
 *       {@link OurException} con un mensaje para el usuario.</li>
 * </ul>
 * Ver la documentación de cada método en la interfaz {@link DAO}.
 */
public class DAOImplementacion implements DAO {

    /**
     * Constructor vacío. Se crea uno solo, en la clase Sesion.
     */
    public DAOImplementacion() {
    }

    /** Nombre de la colección de administradores. */
    private static final String COL_ADMINISTRADORES = "Administradores";
    /** Nombre de la colección de empleados. */
    private static final String COL_EMPLEADOS = "Empleados";
    /** Nombre de la colección de clientes. */
    private static final String COL_CLIENTES = "Clientes";
    /** Nombre de la colección de tarifas. */
    private static final String COL_TARIFAS = "Tarifas";
    /** Nombre de la colección de contratos. */
    private static final String COL_CONTRATOS = "Contratos";
    /** Nombre de la colección de tickets de soporte. */
    private static final String COL_TICKETS = "TicketsSoportes";
    /** Nombre de la colección de sucursales. */
    private static final String COL_SUCURSALES = "Sucursales";
    /** Nombre de la colección de valoraciones. */
    private static final String COL_VALORACIONES = "Valoraciones";

    /**
     * Devuelve una colección de la base de datos.
     *
     * @param nombre nombre de la colección (una de las constantes COL_...)
     * @return la colección
     * @throws OurException si no hay conexión con MongoDB
     */
    private MongoCollection<Document> coleccion(String nombre) throws OurException {
        return MongoConnection.getDatabase().getCollection(nombre);
    }

    // ---------- LOGIN Y CLIENTES ----------

    @Override
    public Usuario login(String nombreUsuario, String contrasena) throws OurException {
        try {
            Document filtro = new Document();
            filtro.put("nombreUsuario", nombreUsuario);
            filtro.put("contrasena", contrasena);

            Document d = coleccion(COL_ADMINISTRADORES).find(filtro).first();
            if (d != null) {
                LogApp.info("Login de administrador: " + nombreUsuario);
                return new Administrador(idTexto(d, "_id"), texto(d, "nombre"), texto(d, "nombreUsuario"),
                        texto(d, "contrasena"), texto(d, "correo"), texto(d, "cuentaBancaria"));
            }

            d = coleccion(COL_EMPLEADOS).find(filtro).first();
            if (d != null) {
                LogApp.info("Login de empleado: " + nombreUsuario);
                return documentoAEmpleado(d);
            }

            d = coleccion(COL_CLIENTES).find(filtro).first();
            if (d != null) {
                LogApp.info("Login de cliente: " + nombreUsuario);
                return documentoACliente(d);
            }

            LogApp.info("Login fallido: " + nombreUsuario);
            return null;
        } catch (MongoException e) {
            LogApp.error("Error en el login de " + nombreUsuario, e);
            throw new OurException(ErrorMessages.LOGIN);
        }
    }

    /**
     * Mira si otra persona (cliente, empleado o administrador) ya usa ese nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario a buscar
     * @param idPropio      id de quien modifica sus datos, para no contarse a sí mismo (null al dar de alta)
     * @return true si ya lo usa otra persona
     * @throws OurException si no hay conexión con MongoDB
     */
    private boolean nombreUsuarioOcupado(String nombreUsuario, String idPropio) throws OurException {
        String[] colecciones = {COL_ADMINISTRADORES, COL_EMPLEADOS, COL_CLIENTES};
        for (String nombreColeccion : colecciones) {
            Document d = coleccion(nombreColeccion).find(new Document("nombreUsuario", nombreUsuario)).first();
            if (d != null && !idTexto(d, "_id").equals(idPropio)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mira si otro cliente ya tiene ese DNI.
     *
     * @param dni      DNI a buscar
     * @param idPropio id del cliente que modifica sus datos, para no contarse a sí mismo (null al dar de alta)
     * @return true si ya lo tiene otro cliente
     * @throws OurException si no hay conexión con MongoDB
     */
    private boolean dniOcupado(String dni, String idPropio) throws OurException {
        Document d = coleccion(COL_CLIENTES).find(new Document("DNI", dni)).first();
        return d != null && !idTexto(d, "_id").equals(idPropio);
    }

    @Override
    public void insertarCliente(Cliente cliente) throws OurException {
        try {
            if (dniOcupado(cliente.getDni(), null)) {
                LogApp.info("Registro rechazado, DNI repetido: " + cliente.getDni());
                throw new OurException(ErrorMessages.DNI_EXISTS);
            }
            if (nombreUsuarioOcupado(cliente.getNombreUsuario(), null)) {
                LogApp.info("Registro rechazado, usuario repetido: " + cliente.getNombreUsuario());
                throw new OurException(ErrorMessages.USER_EXISTS);
            }

            Document d = new Document();
            d.put("nombreUsuario", cliente.getNombreUsuario());
            d.put("nombre", cliente.getNombre());
            d.put("contrasena", cliente.getContrasena());
            d.put("DNI", cliente.getDni());
            d.put("correo", cliente.getCorreo());
            d.put("telefono", cliente.getTelefono());
            d.put("direccion", cliente.getDireccion());
            d.put("cuentaBancaria", cliente.getCuentaBancaria());
            d.put("dadoDeBaja", false);
            d.put("ticketSoporte", new ArrayList<ObjectId>());
            coleccion(COL_CLIENTES).insertOne(d);

            // Al insertar, MongoDB le pone un _id nuevo al documento
            cliente.setId(idTexto(d, "_id"));
            LogApp.info("Cliente registrado: " + cliente.getNombreUsuario() + " (id " + cliente.getId() + ")");
        } catch (MongoException e) {
            LogApp.error("Error registrando el cliente " + cliente.getNombreUsuario(), e);
            throw new OurException(ErrorMessages.REGISTER_USER);
        }
    }

    @Override
    public void actualizarCliente(Cliente cliente) throws OurException {
        try {
            if (nombreUsuarioOcupado(cliente.getNombreUsuario(), cliente.getId())) {
                throw new OurException(ErrorMessages.USER_EXISTS);
            }
            if (dniOcupado(cliente.getDni(), cliente.getId())) {
                throw new OurException(ErrorMessages.DNI_EXISTS);
            }
            Document cambios = new Document();
            cambios.put("nombre", cliente.getNombre());
            cambios.put("nombreUsuario", cliente.getNombreUsuario());
            cambios.put("DNI", cliente.getDni());
            cambios.put("correo", cliente.getCorreo());
            cambios.put("direccion", cliente.getDireccion());
            cambios.put("telefono", cliente.getTelefono());
            cambios.put("cuentaBancaria", cliente.getCuentaBancaria());

            if (!modificar(COL_CLIENTES, cliente.getId(), cambios)) {
                throw new OurException(ErrorMessages.UPDATE_USER);
            }
            LogApp.info("Cliente modificado: " + cliente.getNombreUsuario() + " (id " + cliente.getId() + ")");
        } catch (MongoException e) {
            LogApp.error("Error modificando el cliente " + cliente.getId(), e);
            throw new OurException(ErrorMessages.UPDATE_USER);
        }
    }

    // ---------- TARIFAS ----------

    @Override
    public List<Tarifa> obtenerTarifas() throws OurException {
        List<Tarifa> tarifas = new ArrayList<>();
        try {
            // Solo las tarifas activas. "$ne" significa "distinto de": así también salen
            // las tarifas que no tienen el campo "activa"
            Document filtro = new Document("activa", new Document("$ne", false));
            for (Document d : coleccion(COL_TARIFAS).find(filtro)) {
                tarifas.add(new Tarifa(idTexto(d, "_id"), texto(d, "nombreTarifa"), numero(d, "precio"),
                        texto(d, "detalles"), texto(d, "tipotarifa"), true));
            }
            LogApp.info("Consultadas " + tarifas.size() + " tarifas");
            return tarifas;
        } catch (MongoException e) {
            LogApp.error("Error consultando tarifas", e);
            throw new OurException(ErrorMessages.GET_TARIFAS);
        }
    }

    // ---------- CONTRATOS ----------

    @Override
    public List<Contrato> obtenerContratosCliente(String idCliente) throws OurException {
        try {
            List<Contrato> contratos = buscarContratos(new Document("idCliente", new ObjectId(idCliente)));
            LogApp.info("Consultados " + contratos.size() + " contratos del cliente " + idCliente);
            return contratos;
        } catch (MongoException e) {
            LogApp.error("Error consultando contratos del cliente " + idCliente, e);
            throw new OurException(ErrorMessages.GET_CONTRATOS);
        }
    }

    @Override
    public List<Contrato> obtenerContratosPorEstado(String estado) throws OurException {
        try {
            List<Contrato> contratos = buscarContratos(new Document("estado", estado));
            LogApp.info("Consultados " + contratos.size() + " contratos en estado " + estado);
            return contratos;
        } catch (MongoException e) {
            LogApp.error("Error consultando contratos en estado " + estado, e);
            throw new OurException(ErrorMessages.GET_CONTRATOS);
        }
    }

    /**
     * Busca contratos y los convierte en objetos {@link Contrato}.
     * Rellena también el nombre de la tarifa (embebida en el contrato) y el nombre y DNI del cliente.
     *
     * @param filtro condiciones de búsqueda
     * @return lista de contratos encontrados
     * @throws OurException si no hay conexión con MongoDB
     */
    private List<Contrato> buscarContratos(Document filtro) throws OurException {
        List<Contrato> contratos = new ArrayList<>();
        for (Document d : coleccion(COL_CONTRATOS).find(filtro)) {
            Contrato c = new Contrato(idTexto(d, "_id"), texto(d, "telefono"), texto(d, "estado"),
                    idTexto(d, "idCliente"), idTexto(d, "idTarifa"), idTexto(d, "idEmpleado"),
                    texto(d, "fechaComienzo"), texto(d, "fechaFinalizacion"));

            // La tarifa está embebida dentro del contrato
            Document tarifa = (Document) d.get("tarifa");
            if (tarifa != null) {
                c.setNombreTarifa(texto(tarifa, "nombreTarifa"));
                c.setPrecio(numero(tarifa, "precio"));
            }
            // El nombre y el DNI del cliente se buscan en su colección
            Document cliente = coleccion(COL_CLIENTES).find(new Document("_id", d.get("idCliente"))).first();
            if (cliente != null) {
                c.setNombreCliente(texto(cliente, "nombre"));
                c.setDniCliente(texto(cliente, "DNI"));
            }
            contratos.add(c);
        }
        return contratos;
    }

    @Override
    public void insertarContrato(Contrato contrato) throws OurException {
        try {
            Document d = new Document();
            d.put("telefono", contrato.getTelefono());
            d.put("estado", contrato.getEstado());
            d.put("idCliente", new ObjectId(contrato.getIdCliente()));
            d.put("idTarifa", new ObjectId(contrato.getIdTarifa()));
            d.put("idEmpleado", aObjectId(contrato.getIdEmpleado()));

            // Información embebida: los datos de la tarifa se guardan dentro del contrato
            Document tarifa = new Document();
            tarifa.put("nombreTarifa", contrato.getNombreTarifa());
            tarifa.put("precio", contrato.getPrecio());
            d.put("tarifa", tarifa);

            d.put("fechaComienzo", contrato.getFechaComienzo());
            d.put("fechaFinalizacion", contrato.getFechaFinalizacion());
            coleccion(COL_CONTRATOS).insertOne(d);

            contrato.setId(idTexto(d, "_id"));
            LogApp.info("Contrato creado: id " + contrato.getId() + ", cliente " + contrato.getIdCliente()
                    + ", tarifa " + contrato.getIdTarifa() + ", estado " + contrato.getEstado());
        } catch (MongoException e) {
            LogApp.error("Error creando un contrato del cliente " + contrato.getIdCliente(), e);
            throw new OurException(ErrorMessages.INSERT_CONTRATO);
        }
    }

    @Override
    public boolean borrarPeticion(String idCliente, String idTarifa) throws OurException {
        try {
            Document filtro = new Document();
            filtro.put("idCliente", new ObjectId(idCliente));
            filtro.put("idTarifa", new ObjectId(idTarifa));
            filtro.put("estado", ESTADO_PENDIENTE);
            long borrados = coleccion(COL_CONTRATOS).deleteOne(filtro).getDeletedCount();
            LogApp.info("Petición de tarifa borrada: cliente " + idCliente + ", tarifa " + idTarifa + ", borrados " + borrados);
            return borrados > 0;
        } catch (MongoException e) {
            LogApp.error("Error borrando la petición del cliente " + idCliente, e);
            throw new OurException(ErrorMessages.BAJA_CONTRATO);
        }
    }

    @Override
    public void solicitarBaja(String idContrato) throws OurException {
        cambiarEstadoContrato(idContrato, ESTADO_BAJA_PENDIENTE, null, ErrorMessages.BAJA_CONTRATO);
        LogApp.info("Baja solicitada del contrato " + idContrato);
    }

    @Override
    public void validarContrato(String idContrato, String idEmpleado) throws OurException {
        cambiarEstadoContrato(idContrato, ESTADO_ACTIVO, idEmpleado, ErrorMessages.VALIDAR_CONTRATO);
        LogApp.info("Contrato " + idContrato + " validado por el empleado " + idEmpleado);
    }

    /**
     * Cambia el estado de un contrato y, si se indica, guarda qué empleado lo ha hecho.
     *
     * @param idContrato   id del contrato
     * @param estado       estado nuevo
     * @param idEmpleado   id del empleado, o null si lo hace el cliente
     * @param mensajeError mensaje para el usuario si algo falla
     * @throws OurException si el contrato no existe o falla MongoDB
     */
    private void cambiarEstadoContrato(String idContrato, String estado, String idEmpleado, String mensajeError) throws OurException {
        try {
            Document cambios = new Document();
            cambios.put("estado", estado);
            if (idEmpleado != null) {
                cambios.put("idEmpleado", new ObjectId(idEmpleado));
            }
            if (!modificar(COL_CONTRATOS, idContrato, cambios)) {
                throw new OurException(mensajeError);
            }
        } catch (MongoException e) {
            LogApp.error("Error cambiando el contrato " + idContrato + " a " + estado, e);
            throw new OurException(mensajeError);
        }
    }

    @Override
    public List<Cliente> obtenerClientesConBajaPendiente() throws OurException {
        List<Cliente> clientes = new ArrayList<>();
        List<String> idsVistos = new ArrayList<>();
        try {
            for (Document contrato : coleccion(COL_CONTRATOS).find(new Document("estado", ESTADO_BAJA_PENDIENTE))) {
                String idCliente = idTexto(contrato, "idCliente");
                // Un cliente puede tener varias bajas pendientes: solo se añade una vez
                if (!idsVistos.contains(idCliente)) {
                    idsVistos.add(idCliente);
                    Document d = coleccion(COL_CLIENTES).find(new Document("_id", new ObjectId(idCliente))).first();
                    if (d != null) {
                        clientes.add(documentoACliente(d));
                    }
                }
            }
            LogApp.info("Consultados " + clientes.size() + " clientes con baja pendiente");
            return clientes;
        } catch (MongoException e) {
            LogApp.error("Error consultando las bajas pendientes", e);
            throw new OurException(ErrorMessages.GET_CONTRATOS);
        }
    }

    @Override
    public void aprobarBaja(String idCliente, String idEmpleado) throws OurException {
        try {
            // 1. Cancelar todos los contratos del cliente que están en "baja pendiente"
            Document filtro = new Document();
            filtro.put("idCliente", new ObjectId(idCliente));
            filtro.put("estado", ESTADO_BAJA_PENDIENTE);
            Document cambios = new Document();
            cambios.put("estado", ESTADO_CANCELADO);
            cambios.put("idEmpleado", new ObjectId(idEmpleado));
            long cancelados = coleccion(COL_CONTRATOS).updateMany(filtro, new Document("$set", cambios)).getModifiedCount();
            LogApp.info("Baja aprobada por el empleado " + idEmpleado + ": " + cancelados + " contratos cancelados del cliente " + idCliente);

            // 2. Si ya no le queda ningún contrato sin cancelar, el cliente queda dado de baja (se mantiene su perfil).
            //    countDocuments cuenta en MongoDB sin traer los documentos a Java
            Document filtroVivos = new Document();
            filtroVivos.put("idCliente", new ObjectId(idCliente));
            filtroVivos.put("estado", new Document("$ne", ESTADO_CANCELADO));
            long contratosVivos = coleccion(COL_CONTRATOS).countDocuments(filtroVivos);
            if (contratosVivos == 0) {
                modificar(COL_CLIENTES, idCliente, new Document("dadoDeBaja", true));
                LogApp.info("Cliente " + idCliente + " dado de baja");
            }
        } catch (MongoException e) {
            LogApp.error("Error aprobando la baja del cliente " + idCliente, e);
            throw new OurException(ErrorMessages.BAJA_CONTRATO);
        }
    }

    // ---------- TICKETS ----------

    @Override
    public List<TicketSoporte> obtenerTicketsCliente(String idCliente) throws OurException {
        try {
            List<TicketSoporte> tickets = buscarTickets(new Document("idCliente", new ObjectId(idCliente)));
            LogApp.info("Consultados " + tickets.size() + " tickets del cliente " + idCliente);
            return tickets;
        } catch (MongoException e) {
            LogApp.error("Error consultando tickets del cliente " + idCliente, e);
            throw new OurException(ErrorMessages.GET_TICKETS);
        }
    }

    @Override
    public List<TicketSoporte> obtenerTicketsPendientes() throws OurException {
        try {
            // "$in" = el estado es uno de los de la lista
            List<String> estados = List.of(TICKET_PENDIENTE, TICKET_EN_PROCESO);
            List<TicketSoporte> tickets = buscarTickets(new Document("estado", new Document("$in", estados)));
            LogApp.info("Consultados " + tickets.size() + " tickets pendientes");
            return tickets;
        } catch (MongoException e) {
            LogApp.error("Error consultando tickets pendientes", e);
            throw new OurException(ErrorMessages.GET_TICKETS);
        }
    }

    /**
     * Busca tickets y los convierte en objetos {@link TicketSoporte}. Rellena también el nombre del cliente.
     *
     * @param filtro condiciones de búsqueda
     * @return lista de tickets encontrados
     * @throws OurException si no hay conexión con MongoDB
     */
    private List<TicketSoporte> buscarTickets(Document filtro) throws OurException {
        List<TicketSoporte> tickets = new ArrayList<>();
        for (Document d : coleccion(COL_TICKETS).find(filtro)) {
            TicketSoporte t = new TicketSoporte(idTexto(d, "_id"), texto(d, "asunto"), texto(d, "descripcion"),
                    texto(d, "fechaCreacion"), texto(d, "estado"), idTexto(d, "idCliente"), idTexto(d, "idEmpleado"));
            Document cliente = coleccion(COL_CLIENTES).find(new Document("_id", d.get("idCliente"))).first();
            if (cliente != null) {
                t.setNombreCliente(texto(cliente, "nombre"));
            }
            tickets.add(t);
        }
        return tickets;
    }

    @Override
    public void insertarTicket(TicketSoporte ticket) throws OurException {
        try {
            Document d = new Document();
            d.put("asunto", ticket.getAsunto());
            d.put("descripcion", ticket.getDescripcion());
            d.put("fechaCreacion", ticket.getFechaCreacion());
            d.put("estado", TICKET_PENDIENTE);
            d.put("idCliente", new ObjectId(ticket.getIdCliente()));
            d.put("idEmpleado", null);
            coleccion(COL_TICKETS).insertOne(d);
            ticket.setId(idTexto(d, "_id"));

            // Array de _id: el cliente guarda los _id de sus tickets en "ticketSoporte".
            // "$push" añade un elemento al final del array
            Document filtroCliente = new Document("_id", new ObjectId(ticket.getIdCliente()));
            Document nuevoTicket = new Document("ticketSoporte", d.get("_id"));
            coleccion(COL_CLIENTES).updateOne(filtroCliente, new Document("$push", nuevoTicket));
            LogApp.info("Ticket creado: id " + ticket.getId() + ", cliente " + ticket.getIdCliente() + ", asunto \"" + ticket.getAsunto() + "\"");
        } catch (MongoException e) {
            LogApp.error("Error creando un ticket del cliente " + ticket.getIdCliente(), e);
            throw new OurException(ErrorMessages.INSERT_TICKET);
        }
    }

    @Override
    public void cambiarEstadoTicket(String idTicket, String estado, String idEmpleado) throws OurException {
        try {
            Document cambios = new Document();
            cambios.put("estado", estado);
            cambios.put("idEmpleado", new ObjectId(idEmpleado));
            if (!modificar(COL_TICKETS, idTicket, cambios)) {
                throw new OurException(ErrorMessages.ESTADO_TICKET);
            }
            LogApp.info("Ticket " + idTicket + " cambiado a \"" + estado + "\" por el empleado " + idEmpleado);
        } catch (MongoException e) {
            LogApp.error("Error cambiando el ticket " + idTicket + " a " + estado, e);
            throw new OurException(ErrorMessages.ESTADO_TICKET);
        }
    }

    // ---------- EMPLEADOS Y ADMINISTRADORES ----------

    @Override
    public void actualizarEmpleado(Empleado empleado) throws OurException {
        String nombreColeccion = COL_EMPLEADOS;
        if (empleado instanceof Administrador) {
            nombreColeccion = COL_ADMINISTRADORES;
        }
        try {
            if (nombreUsuarioOcupado(empleado.getNombreUsuario(), empleado.getId())) {
                throw new OurException(ErrorMessages.USER_EXISTS);
            }
            Document cambios = new Document();
            cambios.put("nombre", empleado.getNombre());
            cambios.put("nombreUsuario", empleado.getNombreUsuario());
            cambios.put("contrasena", empleado.getContrasena());
            cambios.put("correo", empleado.getCorreo());
            cambios.put("cuentaBancaria", empleado.getCuentaBancaria());
            if (!modificar(nombreColeccion, empleado.getId(), cambios)) {
                throw new OurException(ErrorMessages.UPDATE_USER);
            }
            LogApp.info("Datos modificados en " + nombreColeccion + ": " + empleado.getNombreUsuario());
        } catch (MongoException e) {
            LogApp.error("Error modificando " + empleado.getNombreUsuario() + " en " + nombreColeccion, e);
            throw new OurException(ErrorMessages.UPDATE_USER);
        }
    }

    @Override
    public List<Empleado> obtenerEmpleados() throws OurException {
        List<Empleado> empleados = new ArrayList<>();
        try {
            for (Document d : coleccion(COL_EMPLEADOS).find()) {
                empleados.add(documentoAEmpleado(d));
            }
            LogApp.info("Consultados " + empleados.size() + " empleados");
            return empleados;
        } catch (MongoException e) {
            LogApp.error("Error consultando empleados", e);
            throw new OurException(ErrorMessages.GET_EMPLEADOS);
        }
    }

    @Override
    public void insertarEmpleado(Empleado empleado) throws OurException {
        try {
            if (nombreUsuarioOcupado(empleado.getNombreUsuario(), null)) {
                throw new OurException(ErrorMessages.USER_EXISTS);
            }
            Document d = documentoEmpleado(empleado, "Empleado");
            d.put("idSucursal", aObjectId(empleado.getIdSucursal()));
            coleccion(COL_EMPLEADOS).insertOne(d);

            empleado.setId(idTexto(d, "_id"));
            LogApp.info("Empleado dado de alta: " + empleado.getNombreUsuario() + " (id " + empleado.getId() + ")");
        } catch (MongoException e) {
            LogApp.error("Error dando de alta el empleado " + empleado.getNombreUsuario(), e);
            throw new OurException(ErrorMessages.INSERT_EMPLEADO);
        }
    }

    @Override
    public void ascenderEmpleado(Empleado empleado) throws OurException {
        // Paso 1: copiarlo a Administradores con el mismo _id
        Document admin = documentoEmpleado(empleado, "Admin");
        admin.put("_id", new ObjectId(empleado.getId()));
        try {
            coleccion(COL_ADMINISTRADORES).insertOne(admin);
        } catch (MongoException e) {
            LogApp.error("Error ascendiendo al empleado " + empleado.getId(), e);
            throw new OurException(ErrorMessages.ASCENDER_EMPLEADO);
        }

        // Paso 2: borrarlo de Empleados. Si falla, se deshace el paso 1 para que no quede en las dos colecciones
        try {
            borrar(COL_EMPLEADOS, empleado.getId());
            LogApp.info("Empleado ascendido a administrador: " + empleado.getNombreUsuario());
        } catch (MongoException e) {
            LogApp.error("Error ascendiendo al empleado " + empleado.getId() + ", se deshace el cambio", e);
            try {
                borrar(COL_ADMINISTRADORES, empleado.getId());
            } catch (MongoException e2) {
                LogApp.error("No se pudo deshacer el ascenso del empleado " + empleado.getId(), e2);
            }
            throw new OurException(ErrorMessages.ASCENDER_EMPLEADO);
        }
    }

    @Override
    public void borrarEmpleado(String idEmpleado) throws OurException {
        try {
            if (!borrar(COL_EMPLEADOS, idEmpleado)) {
                throw new OurException(ErrorMessages.DELETE_EMPLEADO);
            }
            LogApp.info("Empleado borrado: id " + idEmpleado);
        } catch (MongoException e) {
            LogApp.error("Error borrando el empleado " + idEmpleado, e);
            throw new OurException(ErrorMessages.DELETE_EMPLEADO);
        }
    }

    // ---------- SUCURSALES ----------

    @Override
    public List<Sucursal> obtenerSucursales() throws OurException {
        List<Sucursal> sucursales = new ArrayList<>();
        try {
            for (Document d : coleccion(COL_SUCURSALES).find()) {
                // La dirección, la localidad y el código postal están dentro del campo "Ubicacion", que es otro Document
                Document ubicacion = (Document) d.get("Ubicacion");
                if (ubicacion == null) {
                    ubicacion = new Document();
                }
                sucursales.add(new Sucursal(idTexto(d, "_id"), texto(d, "nombre"), texto(d, "horario"), texto(d, "telefono"),
                        texto(ubicacion, "Direccion"), texto(ubicacion, "Localidad"), (int) numero(ubicacion, "CodigoPostal")));
            }
            LogApp.info("Consultadas " + sucursales.size() + " sucursales");
            return sucursales;
        } catch (MongoException e) {
            LogApp.error("Error consultando sucursales", e);
            throw new OurException(ErrorMessages.GET_SUCURSALES);
        }
    }

    @Override
    public void insertarSucursal(Sucursal sucursal) throws OurException {
        try {
            Document d = new Document();
            d.put("nombre", sucursal.getNombre());
            d.put("horario", sucursal.getHorario());
            d.put("telefono", sucursal.getTelefono());
            d.put("Ubicacion", documentoUbicacion(sucursal));
            coleccion(COL_SUCURSALES).insertOne(d);

            sucursal.setId(idTexto(d, "_id"));
            LogApp.info("Sucursal creada: " + sucursal.getNombre() + " (id " + sucursal.getId() + ")");
        } catch (MongoException e) {
            LogApp.error("Error creando la sucursal " + sucursal.getNombre(), e);
            throw new OurException(ErrorMessages.SAVE_SUCURSAL);
        }
    }

    @Override
    public void actualizarSucursal(Sucursal sucursal) throws OurException {
        try {
            Document cambios = new Document();
            cambios.put("nombre", sucursal.getNombre());
            cambios.put("horario", sucursal.getHorario());
            cambios.put("telefono", sucursal.getTelefono());
            cambios.put("Ubicacion", documentoUbicacion(sucursal));
            if (!modificar(COL_SUCURSALES, sucursal.getId(), cambios)) {
                throw new OurException(ErrorMessages.SAVE_SUCURSAL);
            }
            LogApp.info("Sucursal modificada: " + sucursal.getNombre() + " (id " + sucursal.getId() + ")");
        } catch (MongoException e) {
            LogApp.error("Error modificando la sucursal " + sucursal.getId(), e);
            throw new OurException(ErrorMessages.SAVE_SUCURSAL);
        }
    }

    @Override
    public void borrarSucursal(String idSucursal) throws OurException {
        try {
            Document empleado = coleccion(COL_EMPLEADOS).find(new Document("idSucursal", new ObjectId(idSucursal))).first();
            if (empleado != null) {
                LogApp.info("No se borra la sucursal " + idSucursal + " porque tiene empleados");
                throw new OurException(ErrorMessages.SUCURSAL_CON_EMPLEADOS);
            }
            if (!borrar(COL_SUCURSALES, idSucursal)) {
                throw new OurException(ErrorMessages.SAVE_SUCURSAL);
            }
            LogApp.info("Sucursal borrada: id " + idSucursal);
        } catch (MongoException e) {
            LogApp.error("Error borrando la sucursal " + idSucursal, e);
            throw new OurException(ErrorMessages.SAVE_SUCURSAL);
        }
    }

    // ---------- VALORACIONES ----------

    @Override
    public List<Valoracion> obtenerValoracionesCliente(String idCliente) throws OurException {
        List<Valoracion> valoraciones = new ArrayList<>();
        try {
            for (Document d : coleccion(COL_VALORACIONES).find(new Document("idCliente", new ObjectId(idCliente)))) {
                valoraciones.add(new Valoracion(idTexto(d, "_id"), (int) numero(d, "puntuacion"), texto(d, "texto"),
                        idTexto(d, "idCliente"), idTexto(d, "idTarifa")));
            }
            LogApp.info("Consultadas " + valoraciones.size() + " valoraciones del cliente " + idCliente);
            return valoraciones;
        } catch (MongoException e) {
            LogApp.error("Error consultando valoraciones del cliente " + idCliente, e);
            throw new OurException(ErrorMessages.GET_VALORACIONES);
        }
    }

    @Override
    public void insertarValoracion(Valoracion valoracion) throws OurException {
        try {
            Document d = new Document();
            d.put("puntuacion", valoracion.getPuntuacion());
            d.put("texto", valoracion.getTexto());
            d.put("idCliente", new ObjectId(valoracion.getIdCliente()));
            d.put("idTarifa", aObjectId(valoracion.getIdTarifa()));
            coleccion(COL_VALORACIONES).insertOne(d);

            valoracion.setId(idTexto(d, "_id"));
            LogApp.info("Valoración publicada: id " + valoracion.getId() + ", puntuación " + valoracion.getPuntuacion());
        } catch (MongoException e) {
            LogApp.error("Error publicando una valoración del cliente " + valoracion.getIdCliente(), e);
            throw new OurException(ErrorMessages.SAVE_VALORACION);
        }
    }

    @Override
    public void actualizarValoracion(Valoracion valoracion) throws OurException {
        try {
            Document cambios = new Document();
            cambios.put("puntuacion", valoracion.getPuntuacion());
            cambios.put("texto", valoracion.getTexto());
            if (!modificar(COL_VALORACIONES, valoracion.getId(), cambios)) {
                throw new OurException(ErrorMessages.SAVE_VALORACION);
            }
            LogApp.info("Valoración modificada: id " + valoracion.getId());
        } catch (MongoException e) {
            LogApp.error("Error modificando la valoración " + valoracion.getId(), e);
            throw new OurException(ErrorMessages.SAVE_VALORACION);
        }
    }

    @Override
    public void borrarValoracion(String idValoracion) throws OurException {
        try {
            if (!borrar(COL_VALORACIONES, idValoracion)) {
                throw new OurException(ErrorMessages.SAVE_VALORACION);
            }
            LogApp.info("Valoración borrada: id " + idValoracion);
        } catch (MongoException e) {
            LogApp.error("Error borrando la valoración " + idValoracion, e);
            throw new OurException(ErrorMessages.SAVE_VALORACION);
        }
    }

    // ---------- DE DOCUMENT A OBJETO Y AL REVÉS ----------

    /**
     * Convierte un documento de la colección Clientes en un objeto {@link Cliente}.
     *
     * @param d documento de MongoDB
     * @return el cliente
     */
    private Cliente documentoACliente(Document d) {
        Cliente c = new Cliente(idTexto(d, "_id"), texto(d, "nombre"), texto(d, "nombreUsuario"), texto(d, "contrasena"),
                texto(d, "correo"), texto(d, "cuentaBancaria"), texto(d, "DNI"), texto(d, "telefono"), texto(d, "direccion"));
        c.setDadoDeBaja(d.getBoolean("dadoDeBaja", false));
        return c;
    }

    /**
     * Convierte un documento de la colección Empleados en un objeto {@link Empleado}.
     *
     * @param d documento de MongoDB
     * @return el empleado
     */
    private Empleado documentoAEmpleado(Document d) {
        return new Empleado(idTexto(d, "_id"), texto(d, "nombre"), texto(d, "nombreUsuario"), texto(d, "contrasena"),
                texto(d, "correo"), texto(d, "cuentaBancaria"), idTexto(d, "idSucursal"));
    }

    /**
     * Crea el documento con los datos comunes de un empleado o administrador.
     *
     * @param empleado empleado o administrador
     * @param rol      "Empleado" o "Admin"
     * @return documento listo para guardar
     */
    private Document documentoEmpleado(Empleado empleado, String rol) {
        Document d = new Document();
        d.put("nombre", empleado.getNombre());
        d.put("nombreUsuario", empleado.getNombreUsuario());
        d.put("rol", rol);
        d.put("contrasena", empleado.getContrasena());
        d.put("correo", empleado.getCorreo());
        d.put("cuentaBancaria", empleado.getCuentaBancaria());
        return d;
    }

    /**
     * Crea el documento embebido "Ubicacion" de una sucursal.
     *
     * @param sucursal sucursal
     * @return documento con Direccion, Localidad y CodigoPostal
     */
    private Document documentoUbicacion(Sucursal sucursal) {
        Document ubicacion = new Document();
        ubicacion.put("Direccion", sucursal.getDireccion());
        ubicacion.put("Localidad", sucursal.getLocalidad());
        ubicacion.put("CodigoPostal", sucursal.getCodigoPostal());
        return ubicacion;
    }

    // ---------- AYUDANTES ----------

    /**
     * Modifica los campos de "cambios" en el documento con ese id.
     *
     * @param nombreColeccion colección donde está el documento
     * @param id              id del documento
     * @param cambios         campos a cambiar
     * @return false si no existe ningún documento con ese id
     * @throws OurException si no hay conexión con MongoDB
     */
    private boolean modificar(String nombreColeccion, String id, Document cambios) throws OurException {
        Document filtro = new Document("_id", new ObjectId(id));
        // "$set" le dice a MongoDB que solo cambie esos campos y deje el resto como está
        long encontrados = coleccion(nombreColeccion).updateOne(filtro, new Document("$set", cambios)).getMatchedCount();
        return encontrados > 0;
    }

    /**
     * Borra el documento con ese id.
     *
     * @param nombreColeccion colección donde está el documento
     * @param id              id del documento
     * @return false si no existe ningún documento con ese id
     * @throws OurException si no hay conexión con MongoDB
     */
    private boolean borrar(String nombreColeccion, String id) throws OurException {
        Document filtro = new Document("_id", new ObjectId(id));
        long borrados = coleccion(nombreColeccion).deleteOne(filtro).getDeletedCount();
        return borrados > 0;
    }

    /**
     * Convierte un id de texto en ObjectId.
     *
     * @param id id en texto (24 caracteres) o null
     * @return el ObjectId, o null si no hay id
     */
    private ObjectId aObjectId(String id) {
        if (id == null) {
            return null;
        }
        return new ObjectId(id);
    }

    /**
     * Lee un campo como texto.
     *
     * @param d     documento
     * @param campo nombre del campo
     * @return el valor como texto, o "" si no existe
     */
    private String texto(Document d, String campo) {
        Object valor = d.get(campo);
        if (valor == null) {
            return "";
        }
        return valor.toString();
    }

    /**
     * Lee un campo que guarda un id (ObjectId) y lo devuelve como texto.
     *
     * @param d     documento
     * @param campo nombre del campo
     * @return el id como texto, o null si no existe
     */
    private String idTexto(Document d, String campo) {
        Object valor = d.get(campo);
        if (valor == null) {
            return null;
        }
        return valor.toString();
    }

    /**
     * Lee un campo numérico (puede estar guardado como int o como double).
     *
     * @param d     documento
     * @param campo nombre del campo
     * @return el número, o 0 si no existe
     */
    private double numero(Document d, String campo) {
        Object valor = d.get(campo);
        if (valor == null) {
            return 0;
        }
        return Double.parseDouble(valor.toString());
    }
}
