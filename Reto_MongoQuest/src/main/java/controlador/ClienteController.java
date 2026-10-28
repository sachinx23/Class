package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import modelo.Cliente;
import modelo.Contrato;
import modelo.Tarifa;
import modelo.TicketSoporte;
import modelo.Valoracion;

/**
 * Panel de cliente (Cliente.fxml).
 * <p>
 * Desde el desplegable "Opciones" se cambia de panel: tarifas, contratos, soporte y valoraciones.
 * Solo se ve un panel a la vez.
 */
public class ClienteController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public ClienteController() {
    }

    /** Acceso a la base de datos. */
    private final DAO dao = Sesion.getDao();

    // Textos del desplegable de opciones
    private static final String OP_TARIFAS = "Mis Tarifas";
    private static final String OP_CONTRATOS = "Mis Contratos";
    private static final String OP_SOPORTE = "Soporte";
    private static final String OP_VALORACIONES = "Mis Valoraciones";
    private static final String OP_CERRAR = "Cerrar Sesion";

    /** Cliente que ha iniciado sesión. */
    private Cliente cliente;

    @FXML private Label lblBienvenida;
    @FXML private ComboBox<String> cmbOpciones;

    @FXML private AnchorPane panelPerfil;
    @FXML private AnchorPane panelTarifas;
    @FXML private AnchorPane panelContratos;
    @FXML private AnchorPane panelSoporte;
    @FXML private AnchorPane panelValoraciones;

    @FXML private TextField txtNombre;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtDni;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCuentaBancaria;

    @FXML private TextField txtSoporteAsunto;
    @FXML private TextField txtSoporteMensaje;

    @FXML private TableView<Tarifa> tablaTarifas;
    @FXML private TableColumn<Tarifa, String> colTarifaNombre;
    @FXML private TableColumn<Tarifa, Double> colTarifaPrecio;
    @FXML private TableColumn<Tarifa, String> colTarifaDetalles;
    @FXML private TableColumn<Tarifa, String> colTarifaTipo;

    @FXML private TableView<Contrato> tablaContratos;
    @FXML private TableColumn<Contrato, String> colContratoTelefono;
    @FXML private TableColumn<Contrato, String> colContratoEstado;
    @FXML private TableColumn<Contrato, String> colContratoFecha;
    @FXML private TableColumn<Contrato, String> colContratoIdTarifa;

    @FXML private TableView<TicketSoporte> tablaSoporte;
    @FXML private TableColumn<TicketSoporte, String> colSoporteAsunto;
    @FXML private TableColumn<TicketSoporte, String> colSoporteFecha;

    @FXML private TableView<Valoracion> tablaValoraciones;
    @FXML private TableColumn<Valoracion, Integer> colValoracionPuntuacion;
    @FXML private TableColumn<Valoracion, String> colValoracionTexto;
    @FXML private ComboBox<Integer> cmbPuntuacion;
    @FXML private TextField txtValoracion;

    /**
     * Se ejecuta al abrir la pantalla: prepara desplegables y columnas, y enseña el perfil.
     */
    @FXML
    public void initialize() {
        cliente = (Cliente) Sesion.getUsuario();
        lblBienvenida.setText("Bienvenido/a " + cliente.getNombre());

        cmbOpciones.getItems().addAll(OP_TARIFAS, OP_CONTRATOS, OP_SOPORTE, OP_VALORACIONES, OP_CERRAR);
        for (int i = 1; i <= 5; i++) {
            cmbPuntuacion.getItems().add(i);
        }

        // PropertyValueFactory usa el getter: "nombreTarifa" llama a getNombreTarifa()
        colTarifaNombre.setCellValueFactory(new PropertyValueFactory<Tarifa, String>("nombreTarifa"));
        colTarifaPrecio.setCellValueFactory(new PropertyValueFactory<Tarifa, Double>("precio"));
        colTarifaDetalles.setCellValueFactory(new PropertyValueFactory<Tarifa, String>("detalles"));
        colTarifaTipo.setCellValueFactory(new PropertyValueFactory<Tarifa, String>("tipoTarifa"));

        colContratoTelefono.setCellValueFactory(new PropertyValueFactory<Contrato, String>("telefono"));
        colContratoEstado.setCellValueFactory(new PropertyValueFactory<Contrato, String>("estado"));
        colContratoFecha.setCellValueFactory(new PropertyValueFactory<Contrato, String>("fechaFinalizacion"));
        colContratoIdTarifa.setCellValueFactory(new PropertyValueFactory<Contrato, String>("nombreTarifa"));

        colSoporteAsunto.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("asunto"));
        colSoporteFecha.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("fechaCreacion"));

        colValoracionPuntuacion.setCellValueFactory(new PropertyValueFactory<Valoracion, Integer>("puntuacion"));
        colValoracionTexto.setCellValueFactory(new PropertyValueFactory<Valoracion, String>("texto"));
        // Las celdas de texto se pueden editar con doble clic
        colValoracionTexto.setCellFactory(TextFieldTableCell.forTableColumn());

        mostrarPanel(panelPerfil);
        cargarPerfil();
    }

    /**
     * Enseña un panel y oculta los demás.
     *
     * @param panel panel que se quiere ver
     */
    private void mostrarPanel(AnchorPane panel) {
        panelPerfil.setVisible(false);
        panelTarifas.setVisible(false);
        panelContratos.setVisible(false);
        panelSoporte.setVisible(false);
        panelValoraciones.setVisible(false);
        panel.setVisible(true);
    }

    /**
     * Rellena el formulario de perfil con los datos del cliente. Caso de uso "Consultar datos personales".
     */
    private void cargarPerfil() {
        txtNombre.setText(cliente.getNombre());
        txtNombreUsuario.setText(cliente.getNombreUsuario());
        txtDni.setText(cliente.getDni());
        txtCorreo.setText(cliente.getCorreo());
        txtDireccion.setText(cliente.getDireccion());
        txtTelefono.setText(cliente.getTelefono());
        txtCuentaBancaria.setText(cliente.getCuentaBancaria());
    }

    /**
     * Carga en la tabla las tarifas que se pueden contratar.
     */
    private void cargarTarifas() {
        try {
            tablaTarifas.setItems(FXCollections.observableArrayList(dao.obtenerTarifas()));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Carga en la tabla los contratos del cliente. Caso de uso "Consultar contratos".
     */
    private void cargarContratos() {
        try {
            tablaContratos.setItems(FXCollections.observableArrayList(dao.obtenerContratosCliente(cliente.getId())));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Carga en la tabla los tickets del cliente.
     */
    private void cargarTickets() {
        try {
            tablaSoporte.setItems(FXCollections.observableArrayList(dao.obtenerTicketsCliente(cliente.getId())));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Carga en la tabla las valoraciones del cliente. Caso de uso "Ver mis valoraciones".
     */
    private void cargarValoraciones() {
        try {
            tablaValoraciones.setItems(FXCollections.observableArrayList(dao.obtenerValoracionesCliente(cliente.getId())));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Mi Perfil".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnMiPerfil(ActionEvent event) {
        mostrarPanel(panelPerfil);
        cargarPerfil();
    }

    /**
     * Desplegable "Opciones": enseña el panel elegido o cierra la sesión.
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void cmbOpcionSeleccionada(ActionEvent event) {
        String opcion = cmbOpciones.getValue();
        if (opcion == null) {
            return;
        }
        if (opcion.equals(OP_TARIFAS)) {
            mostrarPanel(panelTarifas);
            cargarTarifas();
        } else if (opcion.equals(OP_CONTRATOS)) {
            mostrarPanel(panelContratos);
            cargarContratos();
        } else if (opcion.equals(OP_SOPORTE)) {
            mostrarPanel(panelSoporte);
            cargarTickets();
        } else if (opcion.equals(OP_VALORACIONES)) {
            mostrarPanel(panelValoraciones);
            cargarValoraciones();
        } else if (opcion.equals(OP_CERRAR)) {
            if (ShowAlert.confirmar("Salir", "¿Deseas salir del panel de cliente?")) {
                Sesion.cerrar();
                Ventana.cambiar(cmbOpciones, "/vista/IniciarSesion.fxml");
                return;
            }
        }
        cmbOpciones.getSelectionModel().clearSelection();
    }

    /**
     * Botón "Modificar": valida y guarda los datos personales.
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnModificar(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String nombreUsuario = txtNombreUsuario.getText().trim();
        String dni = txtDni.getText().trim().toUpperCase();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();

        if (nombre.isEmpty() || nombreUsuario.isEmpty()) {
            ShowAlert.info("Datos inválidos", "El nombre y el nombre de usuario son obligatorios.");
            return;
        }
        if (!Validaciones.dniValido(dni)) {
            ShowAlert.info("Datos inválidos", "DNI introducido incorrectamente.");
            return;
        }
        if (!Validaciones.correoValido(correo)) {
            ShowAlert.info("Datos inválidos", "El correo no es válido.");
            return;
        }
        if (!Validaciones.telefonoValido(telefono)) {
            ShowAlert.info("Datos inválidos", "El teléfono no es válido.");
            return;
        }
        if (!ShowAlert.confirmar("Confirmar cambios", "¿Seguro que deseas guardar los cambios?")) {
            return;
        }

        // Se crea un objeto nuevo con los cambios. Solo si se guarda bien pasa a ser el usuario de la sesión
        Cliente nuevo = new Cliente(cliente.getId(), nombre, nombreUsuario, cliente.getContrasena(), correo,
                txtCuentaBancaria.getText().trim(), dni, telefono, txtDireccion.getText().trim());
        nuevo.setDadoDeBaja(cliente.isDadoDeBaja());
        try {
            dao.actualizarCliente(nuevo);
            cliente = nuevo;
            Sesion.setUsuario(nuevo);
            ShowAlert.info("Modificación exitosa", "Los datos se han actualizado correctamente.");
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Cambiar tarifa": pide la tarifa seleccionada. Queda pendiente hasta que la valide un empleado. Casos de uso "Elegir tarifa" y "Cambiar tarifa".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnCambiarTarifa(ActionEvent event) {
        Tarifa tarifa = tablaTarifas.getSelectionModel().getSelectedItem();
        if (tarifa == null) {
            ShowAlert.info("Aviso", "Por favor, selecciona una tarifa de la tabla primero.");
            return;
        }
        if (!ShowAlert.confirmar("Cambio de tarifa", "¿Estás seguro de que deseas contratar la tarifa " + tarifa.getNombreTarifa() + "?")) {
            return;
        }
        // Se guarda como petición pendiente: un empleado la valida en "Validar tarifas"
        LocalDate hoy = LocalDate.now();
        Contrato contrato = new Contrato(null, cliente.getTelefono(), DAO.ESTADO_PENDIENTE, cliente.getId(),
                tarifa.getId(), null, hoy.toString(), hoy.plusYears(1).toString());
        contrato.setNombreTarifa(tarifa.getNombreTarifa());
        contrato.setPrecio(tarifa.getPrecio());
        try {
            dao.insertarContrato(contrato);
            ShowAlert.info("Petición enviada", "Tu petición de la tarifa " + tarifa.getNombreTarifa()
                    + " está pendiente de validar por un empleado.");
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Eliminar tarifa": borra la petición pendiente de la tarifa seleccionada. Caso de uso "Eliminar tarifa".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnEliminarTarifa(ActionEvent event) {
        Tarifa tarifa = tablaTarifas.getSelectionModel().getSelectedItem();
        if (tarifa == null) {
            ShowAlert.info("Aviso", "Por favor, selecciona una tarifa de la tabla primero.");
            return;
        }
        if (!ShowAlert.confirmar("Confirmar eliminación", "¿Seguro que deseas eliminar tu petición de la tarifa " + tarifa.getNombreTarifa() + "?")) {
            return;
        }
        try {
            // Solo se puede eliminar una petición que todavía no ha validado ningún empleado
            boolean borrada = dao.borrarPeticion(cliente.getId(), tarifa.getId());
            if (borrada) {
                ShowAlert.info("Tarifa eliminada", "Tu petición de la tarifa " + tarifa.getNombreTarifa() + " ha sido eliminada.");
            } else {
                ShowAlert.info("Aviso", "No tienes ninguna petición pendiente de esa tarifa.");
            }
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Firmar contrato": informa del estado del contrato seleccionado. Caso de uso "Firmar contrato".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnFirmarContrato(ActionEvent event) {
        Contrato contrato = tablaContratos.getSelectionModel().getSelectedItem();
        if (contrato == null) {
            ShowAlert.info("Aviso", "Selecciona un contrato de la tabla primero.");
            return;
        }
        // El contrato queda firmado cuando un empleado valida la tarifa (pasa a "activo")
        if (contrato.getEstado().equals(DAO.ESTADO_ACTIVO)) {
            ShowAlert.info("Contrato firmado", "Este contrato ya está firmado y activo.");
        } else if (contrato.getEstado().equals(DAO.ESTADO_PENDIENTE)) {
            ShowAlert.info("Contrato pendiente", "Este contrato se firmará cuando un empleado valide la tarifa.");
        } else {
            ShowAlert.info("Aviso", "Este contrato no se puede firmar porque está " + contrato.getEstado() + ".");
        }
    }

    /**
     * Botón "Rescindir contrato": pide la baja del contrato seleccionado. Caso de uso "Rescindir un contrato".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnRescindirContrato(ActionEvent event) {
        Contrato contrato = tablaContratos.getSelectionModel().getSelectedItem();
        if (contrato == null) {
            ShowAlert.info("Aviso", "Selecciona un contrato de la tabla primero.");
            return;
        }
        if (!contrato.getEstado().equals(DAO.ESTADO_ACTIVO)) {
            ShowAlert.info("Aviso", "Solo se puede pedir la baja de un contrato activo.");
            return;
        }
        if (!ShowAlert.confirmar("Baja de compañía", "¿Estás seguro de solicitar la baja definitiva y rescindir tu contrato?")) {
            return;
        }
        try {
            // Queda en "baja pendiente" hasta que un empleado la apruebe
            dao.solicitarBaja(contrato.getId());
            ShowAlert.info("Solicitud enviada", "Tu solicitud de baja está pendiente de aprobar por un empleado.");
            cargarContratos();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Enviar ticket": guarda una incidencia nueva. Caso de uso "Crear ticket".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnEnviarTicket(ActionEvent event) {
        String asunto = txtSoporteAsunto.getText().trim();
        String descripcion = txtSoporteMensaje.getText().trim();
        if (asunto.isEmpty() || descripcion.isEmpty()) {
            ShowAlert.info("Campos incompletos", "Rellena el asunto y la descripción del ticket.");
            return;
        }
        if (!ShowAlert.confirmar("Confirmar envío", "¿Seguro que deseas enviar este ticket?")) {
            return;
        }
        TicketSoporte ticket = new TicketSoporte(null, asunto, descripcion, LocalDate.now().toString(), DAO.TICKET_PENDIENTE,
                cliente.getId(), null);
        try {
            dao.insertarTicket(ticket);
            ShowAlert.info("Ticket enviado", "Tu consulta ha sido enviada al departamento de soporte con éxito.");
            txtSoporteAsunto.clear();
            txtSoporteMensaje.clear();
            cargarTickets();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Clic en la tabla de valoraciones: copia la valoración seleccionada al formulario.
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void seleccionarValoracion(MouseEvent event) {
        Valoracion valoracion = tablaValoraciones.getSelectionModel().getSelectedItem();
        if (valoracion != null) {
            cmbPuntuacion.setValue(valoracion.getPuntuacion());
            txtValoracion.setText(valoracion.getTexto());
        }
    }

    /**
     * Comprueba que se ha elegido una puntuación y escrito una opinión.
     *
     * @return true si el formulario de valoración es correcto
     */
    private boolean valoracionValida() {
        if (cmbPuntuacion.getValue() == null || txtValoracion.getText().trim().isEmpty()) {
            ShowAlert.info("Aviso", "Elige una puntuación y escribe tu opinión.");
            return false;
        }
        return true;
    }

    /**
     * Botón "Valorar": publica una valoración nueva. Caso de uso "Valorar".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnValorar(ActionEvent event) {
        if (!valoracionValida()) {
            return;
        }
        if (!ShowAlert.confirmar("Confirmar valoración", "¿Seguro que deseas publicar esta valoración?")) {
            return;
        }
        Valoracion valoracion = new Valoracion(null, cmbPuntuacion.getValue(), txtValoracion.getText().trim(), cliente.getId(), null);
        try {
            dao.insertarValoracion(valoracion);
            ShowAlert.info("Valoración publicada", "Tu valoración ha sido registrada correctamente. ¡Gracias por tu opinión!");
            limpiarValoracion();
            cargarValoraciones();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Editar": guarda los cambios de la valoración seleccionada. Caso de uso "Editar valoración".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnEditarValoracion(ActionEvent event) {
        Valoracion valoracion = tablaValoraciones.getSelectionModel().getSelectedItem();
        if (valoracion == null) {
            ShowAlert.info("Aviso", "Selecciona una valoración de la tabla primero.");
            return;
        }
        if (!valoracionValida()) {
            return;
        }
        if (!ShowAlert.confirmar("Confirmar cambios", "¿Seguro que deseas guardar los cambios de tu valoración?")) {
            return;
        }
        valoracion.setPuntuacion(cmbPuntuacion.getValue());
        valoracion.setTexto(txtValoracion.getText().trim());
        guardarValoracion(valoracion);
    }

    /**
     * Se ejecuta al terminar de editar (doble clic + Enter) una celda de texto de la tabla de valoraciones.
     *
     * @param event evento de edición: tiene la fila editada y el texto nuevo
     */
    @FXML
    public void editarTextoValoracion(TableColumn.CellEditEvent<Valoracion, String> event) {
        Valoracion valoracion = event.getRowValue();
        String nuevoTexto = event.getNewValue().trim();
        if (nuevoTexto.isEmpty()) {
            ShowAlert.info("Aviso", "La opinión no puede estar vacía.");
            cargarValoraciones();
            return;
        }
        valoracion.setTexto(nuevoTexto);
        guardarValoracion(valoracion);
    }

    /**
     * Guarda una valoración modificada y recarga la tabla.
     *
     * @param valoracion valoración con los cambios
     */
    private void guardarValoracion(Valoracion valoracion) {
        try {
            dao.actualizarValoracion(valoracion);
            ShowAlert.info("Modificación exitosa", "Tu valoración se ha actualizado.");
            limpiarValoracion();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
        cargarValoraciones();
    }

    /**
     * Botón "Eliminar": borra la valoración seleccionada.
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnEliminarValoracion(ActionEvent event) {
        Valoracion valoracion = tablaValoraciones.getSelectionModel().getSelectedItem();
        if (valoracion == null) {
            ShowAlert.info("Aviso", "Selecciona una valoración de la tabla primero.");
            return;
        }
        if (!ShowAlert.confirmar("Confirmar eliminación", "¿Seguro que deseas eliminar esta valoración?")) {
            return;
        }
        try {
            dao.borrarValoracion(valoracion.getId());
            ShowAlert.info("Valoración eliminada", "La valoración ha sido eliminada.");
            limpiarValoracion();
            cargarValoraciones();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Vacía el formulario de valoración.
     */
    private void limpiarValoracion() {
        cmbPuntuacion.setValue(null);
        txtValoracion.clear();
    }
}
