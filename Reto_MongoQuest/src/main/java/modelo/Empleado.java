package modelo;

public class Empleado extends Usuario {

    // Puede ser null si el empleado no tiene sucursal
    private String idSucursal;

    public Empleado(String id, String nombre, String nombreUsuario, String contrasena, String correo,
            String cuentaBancaria, String idSucursal) {
        super(id, nombre, nombreUsuario, contrasena, correo, cuentaBancaria);
        this.idSucursal = idSucursal;
    }

    public String getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        this.idSucursal = idSucursal;
    }
}
