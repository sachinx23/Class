package controlador;

import dao.DAO;
import exception.OurException;
import exception.ShowAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import modelo.Cliente;
import modelo.Contrato;
import modelo.Empleado;
import modelo.TicketSoporte;

/**
 * Panel de empleado (Empleado.fxml).
 * <p>
 * Desde el desplegable "Opciones" se cambia de panel: tickets pendientes, solicitudes de baja
 * y validar tarifas. Solo se ve un panel a la vez.
 */
public class EmpleadoController {

    /**
     * Constructor vacío. JavaFX crea el controlador al cargar el FXML.
     */
    public EmpleadoController() {
    }

    /** Acceso a la base de datos. */
    private DAO dao = Sesion.getDao();

    // Textos del desplegable de opciones
    private static final String OP_TICKETS = "Tickets pendientes";
    private static final String OP_BAJAS = "Solicitudes de baja";
    private static final String OP_VALIDAR = "Validar tarifas";
    private static final String OP_CERRAR = "Cerrar Sesion";

    /** Empleado que ha iniciado sesión. */
    private Empleado empleado;

    // Controles de la pantalla. @FXML une cada variable con el control del FXML que tiene el mismo fx:id
    @FXML private Label lblBienvenida;
    @FXML private ComboBox<String> cmbOpciones;

    @FXML private AnchorPane panelPerfil;
    @FXML private AnchorPane panelTickets;
    @FXML private AnchorPane panelBajas;
    @FXML private AnchorPane panelValidar;

    @FXML private TextField txtNombre;
    @FXML private TextField txtNombreUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtCuentaBancaria;

    @FXML private TableView<TicketSoporte> tablaTickets;
    @FXML private TableColumn<TicketSoporte, String> colTicketCliente;
    @FXML private TableColumn<TicketSoporte, String> colTicketAsunto;
    @FXML private TableColumn<TicketSoporte, String> colTicketDescripcion;
    @FXML private TableColumn<TicketSoporte, String> colTicketFecha;
    @FXML private TableColumn<TicketSoporte, String> colTicketEstado;
    /** Desplegable con el estado nuevo del ticket (en proceso, resuelto, cerrado). */
    @FXML private ComboBox<String> cmbEstadoTicket;

    @FXML private TableView<Cliente> tablaBajas;
    @FXML private TableColumn<Cliente, String> colBajaCliente;
    @FXML private TableColumn<Cliente, String> colBajaDni;

    @FXML private TableView<Contrato> tablaPeticiones;
    @FXML private TableColumn<Contrato, String> colPeticionCliente;
    @FXML private TableColumn<Contrato, String> colPeticionTarifa;
    @FXML private TableColumn<Contrato, Double> colPeticionPrecio;

    /**
     * Se ejecuta al abrir la pantalla: prepara desplegables y columnas, y enseña el perfil.
     */
    @FXML
    public void initialize() {
        empleado = (Empleado) Sesion.getUsuario();
        lblBienvenida.setText("Bienvenido/a " + empleado.getNombre());
        cmbOpciones.getItems().addAll(OP_TICKETS, OP_BAJAS, OP_VALIDAR, OP_CERRAR);
        cmbEstadoTicket.getItems().addAll(DAO.TICKET_EN_PROCESO, DAO.TICKET_RESUELTO, DAO.TICKET_CERRADO);

        colTicketCliente.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("nombreCliente"));
        colTicketAsunto.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("asunto"));
        colTicketDescripcion.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("descripcion"));
        colTicketFecha.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("fechaCreacion"));
        colTicketEstado.setCellValueFactory(new PropertyValueFactory<TicketSoporte, String>("estado"));

        colBajaCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
        colBajaDni.setCellValueFactory(new PropertyValueFactory<Cliente, String>("dni"));

        colPeticionCliente.setCellValueFactory(new PropertyValueFactory<Contrato, String>("nombreCliente"));
        colPeticionTarifa.setCellValueFactory(new PropertyValueFactory<Contrato, String>("nombreTarifa"));
        colPeticionPrecio.setCellValueFactory(new PropertyValueFactory<Contrato, Double>("precio"));

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
        panelTickets.setVisible(false);
        panelBajas.setVisible(false);
        panelValidar.setVisible(false);
        panel.setVisible(true);
    }

    /**
     * Rellena el formulario de perfil con los datos del empleado.
     */
    private void cargarPerfil() {
        txtNombre.setText(empleado.getNombre());
        txtNombreUsuario.setText(empleado.getNombreUsuario());
        txtContrasena.setText(empleado.getContrasena());
        txtCorreo.setText(empleado.getCorreo());
        txtCuentaBancaria.setText(empleado.getCuentaBancaria());
    }

    /**
     * Carga en la tabla los tickets sin terminar.
     */
    private void cargarTickets() {
        try {
            tablaTickets.setItems(FXCollections.observableArrayList(dao.obtenerTicketsPendientes()));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Carga en la tabla los clientes que han pedido la baja.
     */
    private void cargarBajas() {
        try {
            tablaBajas.setItems(FXCollections.observableArrayList(dao.obtenerClientesConBajaPendiente()));
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Carga en la tabla los contratos pendientes de validar.
     */
    private void cargarPeticiones() {
        try {
            tablaPeticiones.setItems(FXCollections.observableArrayList(
                    dao.obtenerContratosPorEstado(DAO.ESTADO_PENDIENTE)));
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
        if (opcion.equals(OP_TICKETS)) {
            mostrarPanel(panelTickets);
            cargarTickets();
        } else if (opcion.equals(OP_BAJAS)) {
            mostrarPanel(panelBajas);
            cargarBajas();
        } else if (opcion.equals(OP_VALIDAR)) {
            mostrarPanel(panelValidar);
            cargarPeticiones();
        } else if (opcion.equals(OP_CERRAR)) {
            if (ShowAlert.confirmar("Salir", "¿Deseas salir del panel de empleado?")) {
                Sesion.cerrar();
                Ventana.cambiar(cmbOpciones, "/vista/IniciarSesion.fxml");
                return;
            }
        }
        cmbOpciones.getSelectionModel().clearSelection();
    }

    /**
     * Botón "Modificar": valida y guarda los datos del perfil.
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnModificar(ActionEvent event) {
        String error = Validaciones.comprobarPerfil(txtNombre.getText(), txtNombreUsuario.getText(),
                txtContrasena.getText(), txtCorreo.getText());
        if (error != null) {
            ShowAlert.info("Datos inválidos", error);
            return;
        }
        if (!ShowAlert.confirmar("Confirmar cambios", "¿Seguro que deseas guardar los cambios?")) {
            return;
        }
        // Se crea un objeto nuevo con los cambios. Solo si se guarda bien pasa a ser el usuario de la sesión
        Empleado nuevo = new Empleado(empleado.getId(), txtNombre.getText().trim(), txtNombreUsuario.getText().trim(),
                txtContrasena.getText().trim(), txtCorreo.getText().trim(), txtCuentaBancaria.getText().trim(),
                empleado.getIdSucursal());
        try {
            dao.actualizarEmpleado(nuevo);
            empleado = nuevo;
            Sesion.setUsuario(nuevo);
            ShowAlert.info("Modificación exitosa", "Tus datos se han actualizado correctamente.");
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Cambiar estado": cambia el estado del ticket seleccionado al elegido en el desplegable. Caso de uso "Cambiar estado ticket".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnCambiarEstadoTicket(ActionEvent event) {
        TicketSoporte ticket = tablaTickets.getSelectionModel().getSelectedItem();
        String estado = cmbEstadoTicket.getValue();
        if (ticket == null) {
            ShowAlert.info("Aviso", "Selecciona un ticket de la tabla primero.");
            return;
        }
        if (estado == null) {
            ShowAlert.info("Aviso", "Elige el estado nuevo en el desplegable.");
            return;
        }
        if (!ShowAlert.confirmar("Actualizar ticket", "¿Deseas cambiar el estado de este ticket a '" + estado + "'?")) {
            return;
        }
        try {
            dao.cambiarEstadoTicket(ticket.getId(), estado, empleado.getId());
            ShowAlert.info("Ticket actualizado", "El ticket ahora está " + estado + ".");
            cmbEstadoTicket.setValue(null);
            cargarTickets();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Aprobar baja": cancela los contratos en baja pendiente del cliente seleccionado. Caso de uso "Dar de baja cliente".
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnAprobarBaja(ActionEvent event) {
        Cliente cliente = tablaBajas.getSelectionModel().getSelectedItem();
        if (cliente == null) {
            ShowAlert.info("Aviso", "Selecciona una solicitud de la tabla primero.");
            return;
        }
        if (!ShowAlert.confirmar("Tramitar baja", "¿Confirmas la rescisión de contrato y la baja definitiva de este cliente?")) {
            return;
        }
        try {
            dao.aprobarBaja(cliente.getId(), empleado.getId());
            ShowAlert.info("Gestión completada", "La operación sobre la cuenta del cliente se ha procesado con éxito.");
            cargarBajas();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }

    /**
     * Botón "Validar": activa el contrato seleccionado. Caso de uso "Elegir tarifa" (parte del empleado).
     *
     * @param event evento de la pantalla
     */
    @FXML
    public void btnValidarTarifa(ActionEvent event) {
        Contrato peticion = tablaPeticiones.getSelectionModel().getSelectedItem();
        if (peticion == null) {
            ShowAlert.info("Aviso", "Selecciona una petición de la tabla primero.");
            return;
        }
        if (!ShowAlert.confirmar("Validar tarifa", "¿Validar y activar esta tarifa para el cliente seleccionado?")) {
            return;
        }
        try {
            dao.validarContrato(peticion.getId(), empleado.getId());
            ShowAlert.info("Tarifa validada", "La tarifa se ha validado y el contrato está activo.");
            cargarPeticiones();
        } catch (OurException e) {
            ShowAlert.error(e.getMessage());
        }
    }
}
