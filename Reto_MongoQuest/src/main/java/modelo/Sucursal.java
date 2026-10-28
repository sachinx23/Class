package modelo;

public class Sucursal {

    private String id;
    private String nombre;
    private String horario;
    private String telefono;
    private String direccion;
    private String localidad;
    private int codigoPostal;

    public Sucursal(String id, String nombre, String horario, String telefono, String direccion, String localidad, int codigoPostal) {
        this.id = id;
        this.nombre = nombre;
        this.horario = horario;
        this.telefono = telefono;
        this.direccion = direccion;
        this.localidad = localidad;
        this.codigoPostal = codigoPostal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
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

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public int getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    // El ComboBox de alta de empleado muestra lo que devuelve toString()
    @Override
    public String toString() {
        return nombre;
    }
}
