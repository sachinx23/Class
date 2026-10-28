package com.mycompany.reto_1;

/**
 * Clase de arranque para el .jar ejecutable.
 * <p>
 * El .jar necesita una clase principal que NO herede de Application; esta solo llama a {@link Reto_1}.
 */
public class Launcher {

    /**
     * No se crean objetos de esta clase.
     */
    private Launcher() {
    }

    /**
     * Arranca la aplicación.
     *
     * @param args argumentos de la línea de comandos (no se usan)
     */
    public static void main(String[] args) {
        Reto_1.main(args);
    }
}
