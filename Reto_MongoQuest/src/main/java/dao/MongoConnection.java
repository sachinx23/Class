package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import exception.ErrorMessages;
import exception.OurException;
import log.LogApp;

public class MongoConnection {

    private static MongoClient client;
    private static MongoDatabase database;

    // Abre la conexión con MongoDB. Se llama una vez al arrancar la aplicación
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

    public static MongoDatabase getDatabase() throws OurException {
        if (database == null) {
            throw new OurException(ErrorMessages.DATABASE);
        }
        return database;
    }

    // Cierra la conexión. Se llama al cerrar la aplicación
    public static void close() {
        if (client != null) {
            client.close();
            client = null;
            database = null;
        }
    }
}
