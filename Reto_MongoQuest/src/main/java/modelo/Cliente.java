package modelo;

public class Cliente extends Usuario {

    private String dni;
    private String telefono;
    private String direccion;
    private boolean dadoDeBaja;

    public Cliente(String id, String nombre, String nombreUsuario, String contrasena, String correo,
            String cuentaBancaria, String dni, String telefono, String direccion) {
        super(id, nombre, nombreUsuario, contrasena, correo, cuentaBancaria);
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.dadoDeBaja = false;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isDadoDeBaja() {
        return dadoDeBaja;
    }

    public void setDadoDeBaja(boolean dadoDeBaja) {
        this.dadoDeBaja = dadoDeBaja;
    }
}
