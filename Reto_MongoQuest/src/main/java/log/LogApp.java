package log;

import controlador.Sesion;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Fichero de log (bitácora) de la aplicación: <b>telonda.log</b>.
 * <p>
 * Cada operación con la base de datos añade una línea así:
 * <pre>
 * 28/10/2026 09:15:02 | PedroDiaz0 | Contrato creado: id ... | OK
 * 28/10/2026 09:16:40 | Pablodiaz0 | Error borrando el empleado ... | ERROR: MongoTimeoutException - ...
 * </pre>
 * Es decir: fecha | usuario | tipo de operación | resultado o error.
 * El administrador puede leerlo desde la aplicación (opción "Ver log").
 */
public class LogApp {

    /** Nombre del fichero de log (se crea en la carpeta desde la que se arranca la aplicación). */
    public static final String FICHERO_LOG = "telonda.log";

    /** Formato de la fecha de cada línea. */
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /** Esta clase solo tiene métodos estáticos: no se crean objetos de ella. */
    private LogApp() {
    }

    /**
     * Apunta una operación que ha ido bien.
     *
     * @param operacion qué se ha hecho
     */
    public static void info(String operacion) {
        escribir(operacion + " | OK");
    }

    /**
     * Apunta una operación que ha fallado, con el tipo de excepción y su mensaje.
     *
     * @param operacion qué se intentaba hacer
     * @param e         excepción que se produjo
     */
    public static void error(String operacion, Exception e) {
        escribir(operacion + " | ERROR: " + e.getClass().getSimpleName() + " - " + e.getMessage());
    }

    /**
     * Añade una línea al final del fichero de log.
     *
     * @param texto operación y resultado
     */
    private static void escribir(String texto) {
        String fecha = LocalDateTime.now().format(FORMATO_FECHA);
        String usuario = "sin sesión";
        if (Sesion.getUsuario() != null) {
            usuario = Sesion.getUsuario().getNombreUsuario();
        }

        // true = se añade al final del fichero en vez de borrar lo que había.
        // try-with-resources: el fichero se cierra solo al terminar, aunque haya un error
        try (FileWriter fichero = new FileWriter(FICHERO_LOG, StandardCharsets.UTF_8, true)) {
            fichero.write(fecha + " | " + usuario + " | " + texto + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el log: " + e.getMessage());
        }
    }

    /**
     * Lee todo el fichero de log.
     *
     * @return el contenido del log, o "" si todavía no existe
     * @throws IOException si no se puede leer el fichero
     */
    public static String leerLog() throws IOException {
        if (!Files.exists(Paths.get(FICHERO_LOG))) {
            return "";
        }
        return Files.readString(Paths.get(FICHERO_LOG), StandardCharsets.UTF_8);
    }
}
