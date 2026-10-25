package sistemaestudiantil.dtos;

public class PersonaDTO {

    private final String id;
    private final String dni;
    private final String nombre;
    private final String apellido;
    private final boolean activo;

    public PersonaDTO(String id, String dni, String nombre, String apellido, boolean activo) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = activo;
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
}