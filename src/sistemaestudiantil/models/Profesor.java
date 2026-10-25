package sistemaestudiantil.models;

public class Profesor extends Persona {

    public Profesor(String id, String dni, String nombre, String apellido) {
        super(id, dni, nombre, apellido);
    }

    @Override
    public String getTipoPersona() {
        return "PROFESOR";
    }
}