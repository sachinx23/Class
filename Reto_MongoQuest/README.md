# Tel-ONDA (Reto 2.3 MongoQuest)

Aplicación de escritorio (JavaFX + MongoDB) para una operadora de telefonía.
Hay tres tipos de usuario: **cliente**, **empleado** y **administrador**.

Autores: Iñigo, Sachin y Eneko Plaza.

---

## 1. Cómo ponerlo en marcha

Necesitas **Java 21**, **Maven** y **MongoDB** arrancado en `localhost:27017`.

1. Crear la base de datos (borra la anterior y mete datos de ejemplo):
   ```bash
   mongosh "mongodb://localhost:27017" database/crear_bd.js
   ```
   También se puede copiar y pegar el script en la consola *mongosh* de MongoDB Compass.

2. Arrancar la aplicación:
   ```bash
   mvn javafx:run
   ```
   o desde NetBeans con el botón *Run*.

3. Usuarios de prueba (contraseña `abcd` en todos):

   | Rol           | Usuario           |
   |---------------|-------------------|
   | Administrador | `Aitorfernandez0` |
   | Empleado      | `Pablodiaz0`      |
   | Cliente       | `PedroDiaz0`      |

Otros comandos útiles:

| Comando                 | Qué hace                                                         |
|-------------------------|------------------------------------------------------------------|
| `mvn javadoc:javadoc`   | Genera el Javadoc en `target/site/apidocs/index.html`             |
| `mvn package`           | Crea el ejecutable `target/Reto_1-1.0-SNAPSHOT-ejecutable.jar`    |

---

## 2. Arquitectura en capas

```
 Pantallas (FXML)        src/main/resources/vista/*.fxml      -> lo que ve el usuario
        |
 Controladores           controlador/*Controller.java         -> qué pasa al pulsar cada botón
        |                controlador/Validaciones.java        -> comprueba los formularios
        |
 Interfaz DAO            dao/DAO.java                         -> QUÉ operaciones hay
        |
 Implementación DAO      dao/DAOImplementacion.java           -> CÓMO se hacen con MongoDB
        |                (también abre y cierra la conexión: conectar() y cerrar())
        |
 Base de datos           MongoDB "Tel--ONDA"                  (database/crear_bd.js)
```

Además:

- `modelo/` son las clases con los datos (Cliente, Contrato, Tarifa...).
- `exception/` tiene la excepción propia `OurException`, los mensajes de error y las ventanas de aviso.
- `log/LogApp.java` escribe el fichero `telonda.log`.

**Regla importante:** los controladores solo usan la interfaz `DAO` (la consiguen con `Sesion.getDao()`).
Nunca usan MongoDB directamente. Así cada capa está aislada de las demás.

### Cómo viaja un error

1. MongoDB falla y lanza una `MongoException`.
2. El DAO la captura, la apunta en el log y lanza una `OurException` con un mensaje claro
   (por ejemplo *"No se pudo crear el contrato."*).
3. El controlador captura la `OurException` y la enseña en una ventana de error.

Así el usuario nunca ve errores técnicos, pero en el log queda toda la información.

---

## 3. Modelo de datos (MongoDB)

8 colecciones, **todas con esquema de validación** (`$jsonSchema`):

| Colección         | Qué guarda                       | Relaciones                                     |
|-------------------|----------------------------------|------------------------------------------------|
| `Clientes`        | Datos personales del cliente     | `ticketSoporte`: **array de _id** de tickets   |
| `Tarifas`         | Tarifas que se pueden contratar  |                                                |
| `Contratos`       | Tarifa contratada por un cliente | `idCliente`, `idTarifa`, `idEmpleado`; **`tarifa` embebida** |
| `TicketsSoportes` | Incidencias de los clientes      | `idCliente`, `idEmpleado`                      |
| `Valoraciones`    | Opiniones de los clientes        | `idCliente`, `idTarifa`                        |
| `Empleados`       | Trabajadores                     | `idSucursal`                                   |
| `Administradores` | Administradores                  |                                                |
| `Sucursales`      | Tiendas físicas                  | **`Ubicacion` embebida** (dirección, localidad, CP) |

Por qué embebido o referenciado:

- **`Contratos.tarifa` embebida:** el contrato guarda el nombre y el precio de la tarifa *en el momento de firmarlo*.
  Si mañana la tarifa sube de precio, el contrato antiguo no cambia.
- **`Sucursales.Ubicacion` embebida:** la dirección solo tiene sentido dentro de su sucursal, nunca se consulta sola.
- **`Clientes.ticketSoporte` array de _id:** un cliente puede tener muchos tickets y cada ticket cambia de estado
  por su cuenta. Por eso los tickets van en su propia colección y el cliente solo guarda sus _id.

Estados:

- **Contrato:** `pendiente` → (el empleado valida) → `activo` → (el cliente pide la baja) → `baja pendiente` → (el empleado la aprueba) → `cancelado`.
  Un contrato `activo` cuya fecha de fin ya ha pasado también pasa a `cancelado`.
  El campo `notificado` indica si ya se ha avisado al cliente de la cancelación.
- **Ticket:** `pendiente` → `en proceso` → `resuelto` / `cerrado`.

---

## 4. Log

Cada operación con la base de datos añade una línea a `telonda.log`:

```
fecha | usuario | operación | resultado
28/10/2026 09:15:02 | PedroDiaz0 | Contrato creado: id ..., cliente ..., tarifa ..., estado pendiente | OK
28/10/2026 09:16:40 | Pablodiaz0 | Error creando un contrato ... | ERROR: MongoWriteException - Document failed validation ...
```

El administrador puede leerlo desde la aplicación: **Menú → Ver log**.

---

## 5. Casos de uso implementados

| Usuario       | Caso de uso                                   | Dónde                                             |
|---------------|-----------------------------------------------|---------------------------------------------------|
| Todos         | Iniciar sesión / Cerrar sesión                | IniciarSesion · Opciones → Cerrar sesión          |
| Cliente       | Crear usuario (registrarse)                   | IniciarSesion → Registrarse                       |
| Cliente       | Consultar y modificar datos personales        | Mi Perfil                                         |
| Cliente       | Eliminar sus datos sensibles (si está dado de baja) | Mi Perfil → Eliminar mis datos              |
| Cliente       | Aviso de contratos rescindidos o caducados    | Sale solo al entrar en el panel                   |
| Cliente       | Consultar las tiendas (sucursales)            | Opciones → Sucursales                             |
| Cliente       | Elegir / cambiar / eliminar tarifa            | Opciones → Mis Tarifas                            |
| Cliente       | Consultar, firmar y rescindir contratos       | Opciones → Mis Contratos                          |
| Cliente       | Crear ticket                                  | Opciones → Soporte                                |
| Cliente       | Valorar, ver, editar y eliminar valoraciones  | Opciones → Mis Valoraciones (doble clic en el texto para editarlo) |
| Empleado      | Validar tarifas                               | Opciones → Validar tarifas                        |
| Empleado      | Dar de baja cliente (aprobar la baja)         | Opciones → Solicitudes de baja                    |
| Empleado      | Consultar tickets y cambiar su estado         | Opciones → Tickets pendientes                     |
| Administrador | Crear empleado, ascender, borrar empleado     | Menú                                              |
| Administrador | Crear, modificar y eliminar sucursales        | Menú → Gestion Sucursal                           |
| Administrador | Consultar la plantilla y trasladar empleados  | Menú → Gestion Sucursal (clic en una sucursal)    |
| Administrador | Eliminar datos sensibles de clientes de baja  | Menú → Clientes dados de baja                     |
| Administrador | Ver el log                                    | Menú → Ver log                                    |


---

## 6. Cómo leer el código (para quien empieza)

Casi todos los botones de la aplicación siguen los mismos 4 pasos. Por ejemplo, "Enviar ticket" en `ClienteController`:

```java
@FXML
public void btnEnviarTicket(ActionEvent event) {
    // 1. Leer lo que ha escrito el usuario y comprobarlo
    String asunto = txtSoporteAsunto.getText().trim();
    if (asunto.isEmpty()) {
        ShowAlert.info("Campos incompletos", "...");
        return;                                   // si está mal, se para aquí
    }
    // 2. Pedir confirmación
    if (!ShowAlert.confirmar("Confirmar envío", "¿Seguro...?")) {
        return;
    }
    // 3. Llamar al DAO para guardar en MongoDB
    try {
        dao.insertarTicket(ticket);
        // 4. Avisar de que ha ido bien
        ShowAlert.info("Ticket enviado", "...");
    } catch (OurException e) {
        // Si MongoDB falla, el DAO lanza OurException con un mensaje para el usuario
        ShowAlert.error(e.getMessage());
    }
}
```

Y casi todos los métodos del DAO siguen estos pasos:

```java
public void insertarTicket(TicketSoporte ticket) throws OurException {
    try {
        Document d = new Document();              // 1. Un Document es como un HashMap: campo -> valor
        d.put("asunto", ticket.getAsunto());
        coleccion(COL_TICKETS).insertOne(d);      // 2. Se guarda en la colección
        LogApp.info("Ticket creado ...");         // 3. Se apunta en el log
    } catch (MongoException e) {
        LogApp.error("Error creando ...", e);     // Si falla: se apunta el error en el log...
        throw new OurException(ErrorMessages.INSERT_TICKET);  // ...y se avisa a la pantalla
    }
}
```

Las operaciones de MongoDB que se usan:

| Código | Qué hace |
|---|---|
| `coleccion(...).find(filtro)` | Busca los documentos que cumplen el filtro (se recorren con un `for`) |
| `.first()` | Se queda con el primero que encuentra, o `null` si no hay ninguno |
| `insertOne(d)` | Guarda un documento nuevo (MongoDB le pone un `_id`) |
| `modificar(coleccion, id, cambios)` | Cambia solo los campos de `cambios` (usa `$set` por dentro) |
| `borrar(coleccion, id)` | Borra el documento con ese `_id` |
| `$push` | Añade un elemento al final de un array (se usa para `Clientes.ticketSoporte`) |
