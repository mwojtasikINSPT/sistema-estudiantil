package sistemaestudiantil.models;

public abstract class Persona {

    private final String id;
    private String dni;
    private String nombre;
    private String apellido;
    private boolean activo;

    public Persona(String id, String dni, String nombre, String apellido) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = true;
    }

    public String getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public abstract String getTipoPersona();

    @Override
    public String toString() {
        return "Persona {" + "id=" + id + ", dni=" + dni
                + ", nombre=" + nombre + ", apellido=" + apellido
                + ", activo=" + activo + '}';
    }
}