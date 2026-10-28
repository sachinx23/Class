package log;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import controlador.Sesion;

/**
 * Fichero de log (telonda.log). Cada operación con la base de datos añade una línea con:
 * fecha | usuario | tipo de operación | error (si lo hay)
 */
public class LogApp {

    public static final String FICHERO_LOG = "telonda.log";

    public static void info(String operacion) {
        escribir(operacion + " | OK");
    }

    public static void error(String operacion, Exception e) {
        escribir(operacion + " | ERROR: " + e.getMessage());
    }

    private static void escribir(String texto) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String usuario = "sin sesión";
        if (Sesion.getUsuario() != null) {
            usuario = Sesion.getUsuario().getNombreUsuario();
        }

        // true = se añade al final del fichero en vez de borrar lo que había
        try (FileWriter fichero = new FileWriter(FICHERO_LOG, true)) {
            fichero.write(fecha + " | " + usuario + " | " + texto + "\n");
        } catch (IOException e) {
            System.out.println("No se pudo escribir en el log: " + e.getMessage());
        }
    }

    public static String leerLog() throws IOException {
        if (!Files.exists(Paths.get(FICHERO_LOG))) {
            return "";
        }
        return new String(Files.readAllBytes(Paths.get(FICHERO_LOG)));
    }
}
