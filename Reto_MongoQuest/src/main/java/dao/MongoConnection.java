package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import exception.ErrorMessages;
import exception.OurException;
import log.LogApp;

/**
 * Conexión con MongoDB.
 * <p>
 * Solo hay una conexión para toda la aplicación: se abre al arrancar ({@link #connect})
 * y se cierra al salir ({@link #close}). El DAO la pide con {@link #getDatabase()}.
 */
public class MongoConnection {

    /** Cliente de MongoDB (la conexión). null si no está conectado. */
    private static MongoClient client;
    /** Base de datos con la que trabaja la aplicación. null si no está conectado. */
    private static MongoDatabase database;

    /** Esta clase solo tiene métodos estáticos: no se crean objetos de ella. */
    private MongoConnection() {
    }

    /**
     * Abre la conexión con MongoDB. Se llama una vez al arrancar la aplicación.
     *
     * @param uri             dirección de MongoDB, por ejemplo mongodb://localhost:27017
     * @param nombreBaseDatos nombre de la base de datos
     * @throws OurException si MongoDB no está arrancado o no se puede conectar
     */
    public static void connect(String uri, String nombreBaseDatos) throws OurException {
        try {
            if (client == null) {
                client = MongoClients.create(uri);
                database = client.getDatabase(nombreBaseDatos);

                // Fuerza la conexión real: si MongoDB está apagado, aquí salta el error
                database.listCollectionNames().first();
                LogApp.info("Conectado a MongoDB, base de datos " + nombreBaseDatos);
            }
        } catch (RuntimeException e) {
            LogApp.error("Conectar con MongoDB", e);
            close();
            throw new OurException(ErrorMessages.DATABASE);
        }
    }

    /**
     * Devuelve la base de datos.
     *
     * @return la base de datos
     * @throws OurException si todavía no se ha conectado
     */
    public static MongoDatabase getDatabase() throws OurException {
        if (database == null) {
            throw new OurException(ErrorMessages.DATABASE);
        }
        return database;
    }

    /**
     * Cierra la conexión. Se llama al cerrar la aplicación.
     */
    public static void close() {
        if (client != null) {
            client.close();
            client = null;
            database = null;
        }
    }
}
