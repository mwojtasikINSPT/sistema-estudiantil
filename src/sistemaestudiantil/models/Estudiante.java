package sistemaestudiantil.models;

public class Estudiante extends Persona {

    public Estudiante(String id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }

    @Override
    public String getTipoPersona() {
        return "ESTUDIANTE";
    }
}